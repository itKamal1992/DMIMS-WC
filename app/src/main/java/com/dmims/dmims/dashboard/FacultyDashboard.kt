package com.dmims.dmims.dashboard

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_student_dashboard.*
import kotlinx.android.synthetic.main.faculty_dashboard.*
import kotlinx.android.synthetic.main.faculty_dashboard.drawer_layout
import kotlinx.android.synthetic.main.faculty_dashboard.navigation_view
import kotlinx.android.synthetic.main.faculty_dashboard.toolbar
import kotlinx.android.synthetic.main.faculty_dashboard.txt_versionName
import java.util.*
import kotlin.system.exitProcess

class FacultyDashboard : AppCompatActivity() {
    lateinit var academic_cal_board: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var time_table_grid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var emergencuContact: LinearLayout
    lateinit var notification_grid: LinearLayout
    lateinit var feedback_grid: LinearLayout
    lateinit var appraisal_grid: LinearLayout

    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faculty_dashboard)
        noticeboardgrid = findViewById<View>(R.id.noticeboardgrid) as LinearLayout//notification_grid
        academic_cal_board = findViewById<View>(R.id.academic_cal_board) as LinearLayout
        time_table_grid = findViewById<View>(R.id.time_table_grid) as LinearLayout
        notification_grid = findViewById<View>(R.id.notification_grid) as LinearLayout
        feedback_grid = findViewById<View>(R.id.feedback_grid) as LinearLayout
        appraisal_grid = findViewById<View>(R.id.appraisal_grid) as LinearLayout
        helpdiloadboad = findViewById<View>(R.id.helpdiloadboad) as LinearLayout
        emergencuContact = findViewById<View>(R.id.emergencygrid) as LinearLayout

        var pinfo=packageManager.getPackageInfo(packageName, 0)
        var versionName = pinfo.versionName
        txt_versionName.text="App Version : $versionName"

        drawerTitle = findViewById<TextView>(R.id.drawer_title) as TextView
        enrollNo = findViewById<TextView>(R.id.enroll_no) as TextView
        var drawer_titler = intent.getStringExtra("NAME")
        var enroll_nor = intent.getStringExtra("STUD_INFO")
        if (drawer_titler == null || enroll_nor == null) {
            val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            drawer_titler = mypref.getString("key_drawer_title", null)
            enroll_nor = mypref.getString("key_enroll_no", null)
        }
        drawerTitle.text = drawer_titler
        enrollNo.text = enroll_nor
        //Set Event
        setNoticeEvent(noticeboardgrid)
        setacdemicCalEvent(academic_cal_board)
        setTimeTable(time_table_grid)
        setHelpalertEvent(helpdiloadboad)
        setNotice(notification_grid)
      /*  setFeedback(feedback_grid)
        setAppaisal(appraisal_grid)*/
        setEmergencyEvent(emergencuContact)

        //setToggleEvent(mainGrid);

        // Configure action bar


        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "DMIMS DU"


        // Initialize the action bar drawer toggle instance
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {

        }


        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_noticeboard -> {
                   /* val intent = Intent(this@FacultyDashboard, FacultyNoticeBoard::class.java)
                    intent.putExtra("info", "Notice Board Activity")
                    startActivity(intent)*/


                    val intent = Intent(this@FacultyDashboard, Activity_student_notice::class.java)
                   /* val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var student_id_key = mypref.getString("Stud_id_key", null)*/
                    intent.putExtra("stud_k", "6202")
              intent.putExtra("date_of_admiss_k", "07/06/2018")
                    startActivity(intent)

                }

                R.id.action_calender -> {
                    val intent = Intent(this@FacultyDashboard, AcademicCalender::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_time_table -> {
                  /*  val intent = Intent(this@FacultyDashboard, Activity_time_table_faculty::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
*/

                    val intent = Intent(this@FacultyDashboard, Activity_time_table_student::class.java)
              /*      val mypref22 = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var student_id_key22 = mypref22.getString("Stud_id_key", null)*/
                    intent.putExtra("stud_k", "")
                    startActivity(intent)
                }
                R.id.action_notification -> {
                 /*   val intent = Intent(this@FacultyDashboard, Notification_Faculty::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)*/


                    val intent = Intent(this@FacultyDashboard, Activity_Notification_Student::class.java)
                    intent.putExtra("info", "Notice Board Activity")
               /*     val mypref13 = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var student_id_key13 = mypref13.getString("Stud_id_key", null)*/
                    intent.putExtra("stud_k", "1234")
                    intent.putExtra("date_of_admiss_k","12/06/18")
                    startActivity(intent)
                }

                R.id.action_emergency ->{
                    val intent = Intent(this@FacultyDashboard, EmergencyContact::class.java)
                    intent.putExtra("info", "Notice Board Activity")
                    startActivity(intent)
                }

                R.id.action_help -> {
                    displayhelpalert()
                }
                R.id.action_logout -> {
                    var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                    val intentlogout = Intent(this@FacultyDashboard, com.dmims.dmims.activity.SplashScreen::class.java)
                    startActivity(intentlogout)
                }

            }
            // Close the drawer


            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }


        //ViewPager
        viewPager = findViewById<ViewPager>(R.id.viewPager) as ViewPager
        sliderDotsPanel = findViewById<LinearLayout>(R.id.SliderDots) as LinearLayout
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        dotscount = viewPagerAdapter.count
        dots = arrayOfNulls<ImageView>(dotscount)

        for (i in 0 until dotscount) {

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

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                for (i in 0 until dotscount) {
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
        //ViewPager
    }


    // Extension function to show toast message easily
    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //ViewPager - Page Slider
    inner class MyTimerTask : TimerTask() {

        override fun run() {

            this@FacultyDashboard.runOnUiThread(Runnable {
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
    private fun setNoticeEvent(noticeboardgrid: LinearLayout) {
        noticeboardgrid.setOnClickListener(View.OnClickListener {
           /* val intent = Intent(this@FacultyDashboard, FacultyNoticeBoard::class.java)
            intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)*/


            val intent = Intent(this@FacultyDashboard, Activity_student_notice::class.java)
            /*val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
            var student_id_key = mypref.getString("Stud_id_key", null)*/
            intent.putExtra("stud_k", "6202")
            intent.putExtra("date_of_admiss_k", "27/06/17")
            startActivity(intent)

        })

    }

    private fun setacdemicCalEvent(academic_cal_board: LinearLayout) {
        academic_cal_board.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, AcademicCalender::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)

        })
    }

    private fun setTimeTable(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, Activity_time_table_student::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })

    }

    private fun setNotice(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
          /*  val intent = Intent(this@FacultyDashboard, Notification_Faculty::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)*/

            val intent = Intent(this@FacultyDashboard, Activity_Notification_Student::class.java)
            intent.putExtra("info", "Notice Board Activity")
            intent.putExtra("stud_k", "1135")
            intent.putExtra("date_of_admiss_k", "01/09/2017")
            startActivity(intent)

        })

    }




    private fun setEmergencyEvent(emergencygrid: LinearLayout) {
        emergencygrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, EmergencyContact::class.java)
            intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        })

    }


    private fun displayhelpalert() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
//        val txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl)
//        val textviewadmincnt = dialogView.findViewById<TextView>(R.id.txtcontact)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
        }
        dialog.show()

    }


    private fun setHelpalertEvent(helpdiloadboad: LinearLayout) {
        helpdiloadboad.setOnClickListener(View.OnClickListener {
            displayhelpalert()
        })

    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        // val txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl2)
        dialogBuilder.setView(dialogView)
            // set message of alert dialog
            // dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes") { dialog, id ->
                finishAffinity()
                exitProcess(0)
            }
            // negative button text and action
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        // show alert dialog
        alert.show()
    }

}