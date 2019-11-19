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
import kotlinx.android.synthetic.main.faculty_dashboard.*
import java.util.*
import kotlin.system.exitProcess

class FacultyDashboard : AppCompatActivity() {
    lateinit var academic_cal_board: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var time_table_grid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
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
        setFeedback(feedback_grid)
        setAppaisal(appraisal_grid)
        setHelpalertEvent(helpdiloadboad)

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
                    val intent = Intent(this@FacultyDashboard, FacultyNoticeBoard::class.java)
                    intent.putExtra("info", "Notice Board Activity")
                    startActivity(intent)
                }

                R.id.action_calender -> {
                    val intent = Intent(this@FacultyDashboard, FacultyAcademicCalender::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_time_table -> {
                    val intent = Intent(this@FacultyDashboard, Activity_time_table_faculty::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent = Intent(this@FacultyDashboard, Notification_Faculty::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }

                R.id.action_feedback -> {
                    val intent = Intent(this@FacultyDashboard, Feedback_faculty::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }//action_Apprisal
                R.id.action_Apprisal -> {
                    val intent = Intent(this@FacultyDashboard, Activity_Appraisal_faculty::class.java)
                    intent.putExtra("info", "Notice board")
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
            val intent = Intent(this@FacultyDashboard, FacultyNoticeBoard::class.java)
            intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        })

    }

    private fun setacdemicCalEvent(academic_cal_board: LinearLayout) {
        academic_cal_board.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, FacultyAcademicCalender::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)

        })
    }

    private fun setTimeTable(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, Activity_time_table_faculty::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })

    }

    private fun setNotice(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, Notification_Faculty::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })

    }

    private fun setFeedback(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, Feedback_faculty::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })

    }

    private fun setAppaisal(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@FacultyDashboard, Activity_Appraisal_faculty::class.java)
            intent.putExtra("info", "Attendance Activity")
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