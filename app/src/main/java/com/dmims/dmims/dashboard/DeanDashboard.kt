@file:Suppress("DEPRECATION")

package com.dmims.dmims.dashboard

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.R.string
import com.dmims.dmims.R.string.Enrol_No
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.DeptListStudDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_dashboard.drawer_layout
import kotlinx.android.synthetic.main.activity_dashboard.navigation_view
import kotlinx.android.synthetic.main.activity_dashboard.toolbar
import kotlinx.android.synthetic.main.activity_student_dashboard.*

import java.util.*
import kotlin.system.exitProcess

class DeanDashboard : AppCompatActivity()
{
    lateinit var time_table_grid: LinearLayout
    lateinit var greviancegrid: LinearLayout
    lateinit var View_Griv_Princ: LinearLayout
    lateinit var exam_grid: LinearLayout
    lateinit var appraisal_grid: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var notification: LinearLayout
    lateinit var emergencygrid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var feedback_grid: LinearLayout
    lateinit var academicCalBoard: LinearLayout
    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView

    lateinit var title_Mobile: TextView
    lateinit var title_Institute: TextView
    lateinit var title_Course: TextView
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotsCount: Int = 0
    private var dateOfAdmission: String? = "-"
    var COURSE_ID: String? = null
    private var dots: Array<ImageView?>? = null
    private lateinit var progressDiag: ProgressDialog
    private var Deptlist: ArrayList<DeptListStudDataRef>? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dean_dashboard)
        time_table_grid = findViewById<View>(R.id.view_Griv_Princ) as LinearLayout
        View_Griv_Princ = findViewById<View>(R.id.view_Griv_Princ) as LinearLayout
        noticeboardgrid = findViewById<View>(R.id.noticeboardgrid) as LinearLayout


        setViewGrievance(View_Griv_Princ)

        //ViewPager
        viewPager = findViewById<ViewPager>(R.id.viewPager) as ViewPager
        sliderDotsPanel = findViewById<LinearLayout>(R.id.SliderDots) as LinearLayout
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        dotsCount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotsCount)





        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(this)
            dots!![i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.nonactive_dots
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            sliderDotsPanel.addView(dots!![i], params)
            dots!![i]?.setOnClickListener { viewPager.currentItem = i }

        }

        dots!![0]?.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.active_dots
            )
        )
        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            string.drawer_open,
            string.drawer_close
        ) {

        }

        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_viewGriev -> {

                }


                R.id.action_time_table -> {
                    val intent = Intent(this@DeanDashboard, Activity_time_table_student::class.java)
                    val mypref22 = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var student_id_key22 = mypref22.getString("Stud_id_key", null)
                    intent.putExtra("stud_k", student_id_key22?.toString())
                    startActivity(intent)
                }

                R.id.action_help -> {
                    displayHelpAlert()
                }


                R.id.action_logout -> {
                    var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                    val intentlogout = Intent(this@DeanDashboard, SplashScreen::class.java)
                    startActivity(intentlogout)
                }


            }
            // Close the drawer


            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }



        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots!![i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.nonactive_dots
                        )
                    )
                }

                dots!![position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dots
                    )
                )

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        val timer = Timer()
        timer.scheduleAtFixedRate(MyTimerTask(), 2000, 4000)

    }

    fun getInsDetails()
    {
        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java)
        var call3: Call<DeptListStudData> = phpApiInterface.InstDetailsStudYear(COURSE_ID!!)
        call3.enqueue(object : Callback<DeptListStudData> {
            override fun onFailure(call: Call<DeptListStudData>, t: Throwable) {
                Toast.makeText(this@DeanDashboard, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<DeptListStudData>, response: Response<DeptListStudData>) {
                var users = ArrayList<FeedBackDataC>()

                if (response.isSuccessful) {
                    users.clear()
                   Deptlist = response.body()!!.Data
                    if (Deptlist!!.size>0)
                    txt_Institute.text="Ins Name: "+Deptlist!![0].COURSE_INSTITUTE
                    txt_Course.text="Course Name: "+Deptlist!![0].COURSE_NAME

                    val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    val editor = mypref.edit()
                    editor.putString("key_institute_stud", Deptlist!![0].COURSE_INSTITUTE)
                    editor.putString("key_institute", Deptlist!![0].COURSE_INSTITUTE)
                    editor.apply()

                }
            }


        })
        progressDiag.dismiss()

    }

    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //ViewPager - Page Slider
    inner class MyTimerTask : TimerTask() {

        override fun run() {
            this@DeanDashboard.runOnUiThread(Runnable {
                if (viewPager.currentItem == 0) {
                    viewPager.currentItem = 1
                } else if (viewPager.currentItem == 1) {
                    viewPager.currentItem = 2
                } else if (viewPager.currentItem == 2) {
                    viewPager.currentItem = 3
                } else if (viewPager.currentItem == 3) {
                    viewPager.currentItem = 4
                } else if (viewPager.currentItem == 4) {
                    viewPager.currentItem = 5
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = 0
                }
            })

        }
    }

    //ViewPager end
    private fun setTimeTable(time_table_grid: LinearLayout) {
        time_table_grid.setOnClickListener {
            val intent = Intent(this@DeanDashboard, Activity_time_table_student::class.java)
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)
            intent.putExtra("stud_k", student_id_key?.toString())
            startActivity(intent)
        }
    }

    private fun setViewGrievance(View_Griv_Princ: LinearLayout) {
        View_Griv_Princ.setOnClickListener {
            val intent=Intent(this,ViewGrievPrincipal::class.java)
            startActivity(intent)
        }
    }

    private fun setNoticeEvent(noticeboardgrid: LinearLayout) {
        noticeboardgrid.setOnClickListener {
            val intent = Intent(this@DeanDashboard, Activity_student_notice::class.java)
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)
            intent.putExtra("stud_k", student_id_key?.toString())
            intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
            startActivity(intent)
        }
    }

    private fun setNotice(notification: LinearLayout) {
        notification.setOnClickListener {
            val intent = Intent(this@DeanDashboard, Activity_Notification_Student::class.java)
            intent.putExtra("info", "Notice Board Activity")
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)
            intent.putExtra("stud_k", student_id_key?.toString())
            intent.putExtra("date_of_admiss_k", dateOfAdmission.toString())
            startActivity(intent)
        }
    }

    private fun setExam(exam_grid: LinearLayout) {
        exam_grid.setOnClickListener {
            val intent = Intent(this@DeanDashboard, Student_GET_UploadMCQ::class.java)
            startActivity(intent)
        }
    }

    private fun setEmergencyEvent(emergencygrid: LinearLayout) {
        emergencygrid.setOnClickListener {
            val intent = Intent(this@DeanDashboard, EmergencyContact::class.java)
            intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        }
    }

    private fun setAppraisalEvent(emergencygrid: LinearLayout) {
        emergencygrid.setOnClickListener {
//            val intent = Intent(this@StudentDashboard, Activity_Appraisal_student::class.java)
//            intent.putExtra("info", "Notice Board Activity")
//            startActivity(intent)
            this@DeanDashboard.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://103.68.25.22:81/aap")
                )
            )
        }

    }

    private fun setHelpalertEvent(helpdiloadboad: LinearLayout) {
        helpdiloadboad.setOnClickListener {
            displayHelpAlert()
        }
    }


    private fun septicaemicCalEvent(academic_cal_board: LinearLayout) {
        academic_cal_board.setOnClickListener {
            val intent = Intent(this@DeanDashboard, AcademicCalender::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)
        }
    }

    private fun setGrievanceGridEvent(greviancegrid: LinearLayout) {
        greviancegrid.setOnClickListener {
            val intent = Intent(this@DeanDashboard, GreivanceStudFile::class.java)
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)
            intent.putExtra("stud_k", student_id_key?.toString())
            startActivity(intent)


        }
    }
    private fun setFeedbackGridEvent(feedback_grid: LinearLayout) {
        feedback_grid.setOnClickListener {
//            val intent = Intent(this@StudentDashboard, FeedbackDackOptionsForStud::class.java)
            val intent = Intent(this@DeanDashboard, FeedbackOptionActivity::class.java)
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)
            intent.putExtra("stud_k", student_id_key?.toString())
            startActivity(intent)
        }
    }


    private fun displayHelpAlert() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
        }
        dialog.show()
    }

    override fun onBackPressed() {
       // exitDialog()
        super.onBackPressed()
    }

    private fun exitDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        dialogBuilder.setView(dialogView)
            // set message of alert dialog
            // dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes") { dialog, id ->
                println(dialog)
                println(id)
                finishAffinity()
                exitProcess(0)

            }
            // negative button text and action
            .setNegativeButton("No") { dialog, id ->
                println(dialog)
                println(id)
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box

        // show alert dialog
        alert.show()
    }

    private fun dialogCall(Title:String ) {
        val builder = android.app.AlertDialog.Builder(this@DeanDashboard)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        var txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl)
        builder.setView(dialogView)
        //builder.setTitle("An Update is Available")
        txtviewlbl.text = Title
        builder.setPositiveButton("Ok") { dialog, which ->
            //Click button action
                       dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
}