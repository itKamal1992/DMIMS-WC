package com.dmims.dmims.dashboard

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.R
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.examcell_dashboard.*

import java.util.*
import kotlin.system.exitProcess

class ExamCellDashboard : AppCompatActivity() {
    lateinit var attendanceGrid: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var notification: LinearLayout
    lateinit var Mcq_grid_Notification: LinearLayout
    lateinit var Mcq_grid_Inbox: LinearLayout
    lateinit var noticeInboxGrid: LinearLayout

    lateinit var feddbackSchedule_Ei: LinearLayout
    lateinit var feddbackAnalysis_Ei: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var Mcq_grid: LinearLayout
    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView
    lateinit var user_role: TextView
    lateinit var txt_Mobile: TextView
    lateinit var txt_Email: TextView
    lateinit var txt_designation: TextView

    
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    private lateinit var id_admin: String
    private lateinit var roleadmin: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.examcell_dashboard)
        noticeboardgrid = findViewById<View>(R.id.noticeboardgrid) as LinearLayout
        helpdiloadboad = findViewById<View>(R.id.helpdiloadboad) as LinearLayout
        notification = findViewById<View>(R.id.notification) as LinearLayout
        noticeInboxGrid = findViewById<View>(R.id.noticeInboxGrid) as LinearLayout
        Mcq_grid_Notification = findViewById<View>(R.id.Mcq_grid_Notification) as LinearLayout
        Mcq_grid_Inbox = findViewById<View>(R.id.Mcq_grid_Inbox) as LinearLayout
        feddbackSchedule_Ei = findViewById<View>(R.id.ll_feedbackSchedular_Ei) as LinearLayout
        feddbackAnalysis_Ei = findViewById<View>(R.id.ll_feedbackAnalysis) as LinearLayout


        Mcq_grid = findViewById<View>(R.id.Mcq_grid) as LinearLayout

        drawerTitle = findViewById(R.id.drawer_title)

        user_role = findViewById(R.id.user_role)
        txt_Mobile= findViewById(R.id.txt_Mobile)
        txt_Email= findViewById(R.id.txt_Email)
        txt_designation= findViewById(R.id.txt_designation)
        enrollNo = findViewById(R.id.enroll_no)

        var pinfo=packageManager.getPackageInfo(packageName, 0)
        var versionName = pinfo.versionName
        txt_versionName.text="App Version : $versionName"

        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        var drawer_titler = mypref.getString("key_drawer_title", null)
        id_admin = mypref.getString("Stud_id_key", null)!!
        roleadmin = mypref.getString("key_userrole", null)!!

        drawerTitle.text = drawer_titler
        user_role.text = "User : "+roleadmin
        txt_designation.text="Designation : "+mypref.getString("key_designation", null)
        txt_Email.text="E-mail : "+mypref.getString("key_email", null)
        txt_Mobile.text="MB No : "+mypref.getString("key_editmob", null)
        enrollNo.text = "ID : "+id_admin

        //Set Event
        setNoticeEvent(noticeboardgrid)
        setHealerEvent(helpdiloadboad)
        setNotice(notification)
        setMcq(Mcq_grid)
        setMcq_grid_Notification_Event(Mcq_grid_Notification)
        setMcq_grid_Inbox_Event(Mcq_grid_Inbox)
        set_grid_Inbox_Event(noticeInboxGrid)
        setFeedbackScheduler(feddbackSchedule_Ei)
        setFeedbackAnalysis(feddbackAnalysis_Ei)

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
                    val intent = Intent(this@ExamCellDashboard, ExamAdminNoticeBoard::class.java)
                    intent.putExtra("roleadmin", roleadmin)
                    intent.putExtra("id_admin", id_admin)
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent = Intent(this@ExamCellDashboard, Notification_ExamCell::class.java)
                    intent.putExtra("info", "Attendance Activity")
                    startActivity(intent)
                }
                R.id.action_noticeInbox -> {
                    val intent =
                        Intent(this@ExamCellDashboard, Activity_Exam_Admin_Inbox_notice::class.java)
                    startActivity(intent)
                }

                R.id.action_mcq -> {
                    val intent = Intent(this@ExamCellDashboard, ExamMcqUpload::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_mcq_notification -> {
                    val intent = Intent(this@ExamCellDashboard, EXAM_GET_UploadMCQ::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_mcq_inbox -> {
                    val intent =
                        Intent(this@ExamCellDashboard, EXAM_InBox_GET_UploadMCQ::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_fScheduler -> {
                    val intent =
                        Intent(this@ExamCellDashboard, RegistrarFeedbackSchdule::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }

                R.id.action_feddbackAnalysis -> {
                    val intent = Intent(this@ExamCellDashboard, FeedbackAnalysis1::class.java)
                    //  intent.putExtra("info", "Notice Board Activity")
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
                    val intentlogout = Intent(
                        this@ExamCellDashboard,
                     SplashScreen::class.java
                    )
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
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
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
            this@ExamCellDashboard.runOnUiThread(Runnable {
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
    private fun setNotice(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, Notification_ExamCell::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })
    }

    private fun setMcq(Mcq_grid: LinearLayout) {
        Mcq_grid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, ExamMcqUpload::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })
    }

    private fun setNoticeEvent(noticeboardgrid: LinearLayout) {
        noticeboardgrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, ExamAdminNoticeBoard::class.java)
            intent.putExtra("roleadmin", roleadmin)
            intent.putExtra("id_admin", id_admin)
            startActivity(intent)
        })
    }


    private fun setMcq_grid_Notification_Event(Mcq_grid_Notification: LinearLayout) {
        Mcq_grid_Notification.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, EXAM_GET_UploadMCQ::class.java)
            startActivity(intent)
        })
    }

    private fun setMcq_grid_Inbox_Event(Mcq_grid_Inbox: LinearLayout) {
        Mcq_grid_Inbox.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, EXAM_InBox_GET_UploadMCQ::class.java)
            startActivity(intent)
        })
    }

    private fun set_grid_Inbox_Event(noticeInboxGrid: LinearLayout) {
        noticeInboxGrid.setOnClickListener(View.OnClickListener {
            val intent =
                Intent(this@ExamCellDashboard, Activity_Exam_Admin_Inbox_notice::class.java)
            startActivity(intent)
        })
    }

    //feedback aj schedule
    private fun setFeedbackScheduler(emergencygrid: LinearLayout) {
        emergencygrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, RegistrarFeedbackSchdule::class.java)
            //  intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        })
    }


    private fun setFeedbackAnalysis(feddbackAnalysis_Ei: LinearLayout) {
        feddbackAnalysis_Ei.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@ExamCellDashboard, FeedbackAnalysis1::class.java)
            //  intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        })
    }

    //EXAM_InBox_GET_UploadMCQ

    private fun displayhelpalert() {
//        val dialog = AlertDialog.Builder(this)
//        val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
//        dialog.setView(dialogView)
//        dialog.setCancelable(false)
//        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
//            println(dialog)
//            println(i)
//        }
//        dialog.show()


        //////////Start///////////////
        GenericPublicVariable.CustDialog = Dialog(this)
        GenericPublicVariable.CustDialog.setContentView(R.layout.custom_dialog_help)
//        var ivNegClose1: ImageView =
//            GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
        var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//        var btnCancel: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
//        var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//        tvMsg.text = "Do you really want to delete this Exam Key?"
//        GenericPublicVariable.CustDialog.setCancelable(false)
        btnOk.setOnClickListener {
            GenericPublicVariable.CustDialog.dismiss()
        }
//        btnCancel.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
//
//        }
//        ivNegClose1.setOnClickListener {
//            GenericPublicVariable.CustDialog.dismiss()
//        }
        GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT)
        )
        GenericPublicVariable.CustDialog.show()
        //////////End//////////////
    }

    private fun displayhelphostelalert() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_hostle, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
        }
        dialog.show()
    }

    private fun setHealerEvent(helpdiloadboad: LinearLayout) {
        helpdiloadboad.setOnClickListener {
            displayhelpalert()
        }
    }

    override fun onBackPressed() {
        exitDialog()
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

}