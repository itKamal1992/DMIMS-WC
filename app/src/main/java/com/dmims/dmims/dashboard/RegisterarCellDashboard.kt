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
import com.dmims.dmims.*
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.faculty_dashboard.*
import java.util.*

class RegisterarCellDashboard : AppCompatActivity() {
    lateinit var attendanceGrid: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var emergencygrid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var feedbackScheduleGrid: LinearLayout
    lateinit var academicCalBoard: LinearLayout
    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView
    lateinit var user_role: TextView
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    private var id_admin: String? = null
    private var roleadmin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registararcell_dashboard)
        attendanceGrid = findViewById<View>(R.id.attendanceGrid) as LinearLayout
        noticeboardgrid = findViewById<View>(R.id.noticeboardgrid) as LinearLayout
        emergencygrid = findViewById<View>(R.id.emergencygrid) as LinearLayout
        helpdiloadboad = findViewById<View>(R.id.helpdiloadboad) as LinearLayout
        feedbackScheduleGrid = findViewById<View>(R.id.feedbackScheduleGrid) as LinearLayout
        academicCalBoard = findViewById<View>(R.id.academic_cal_board) as LinearLayout
        drawerTitle = findViewById(R.id.drawer_title)
        enrollNo = findViewById(R.id.enroll_no)
        user_role = findViewById(R.id.user_role)
//        var drawer_titler = intent.getStringExtra("NAME")
//        var enroll_nor = intent.getStringExtra("STUD_INFO")
        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        var drawer_titler = mypref.getString("key_drawer_title", null)
        var enroll_nor = mypref.getString("key_enroll_no", null)
        id_admin = mypref.getString("Stud_id_key", null)
        roleadmin = mypref.getString("key_userrole", null)
        drawerTitle.text = drawer_titler
        enrollNo.text = enroll_nor
        user_role.text = roleadmin
        //Set Event
        setSingleEvent(attendanceGrid)
        setNoticeEvent(noticeboardgrid)
        setEmergencyEvent(emergencygrid)
        setHelpalertEvent(helpdiloadboad)
        setfeedbackScheduleEvent(feedbackScheduleGrid)
        setacdemicCalEvent(academicCalBoard)
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
                R.id.action_atten -> {
                    val intent = Intent(this@RegisterarCellDashboard, Attendance::class.java)
                    intent.putExtra("info", "Attendance Activity")
                    startActivity(intent)
                }
                R.id.action_copy -> toast("Copy clicked")
                R.id.action_noticeboard -> {
                    val intent = Intent(this@RegisterarCellDashboard, AdminNoticeBoard::class.java)
                    intent.putExtra("roleadmin", roleadmin)
                    intent.putExtra("id_admin", id_admin)
                    startActivity(intent)
                }
                R.id.action_emergency -> {
                    val intent = Intent(this@RegisterarCellDashboard, EmergencyContact::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_help -> {
                    displayhelpalert()
                }
                R.id.action_hostel -> {
                    displayhelphostelalert()
                }
                R.id.action_calender -> {
                    val intent = Intent(this@RegisterarCellDashboard, AcademicCalender::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_logout -> {
                    var sharepref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
                    var editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                    val intentlogout = Intent(this@RegisterarCellDashboard, com.dmims.dmims.activity.SplashScreen::class.java)
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
            this@RegisterarCellDashboard.runOnUiThread(Runnable {
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
    private fun setSingleEvent(attendanceGrid: LinearLayout) {
        attendanceGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterarCellDashboard, Attendance::class.java)
            intent.putExtra("info", "Attendance Activity")
            startActivity(intent)
        })
    }

    private fun setNoticeEvent(noticeboardgrid: LinearLayout) {
        noticeboardgrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterarCellDashboard, AdminNoticeBoard::class.java)
            intent.putExtra("roleadmin", roleadmin)
            intent.putExtra("id_admin", id_admin)
            startActivity(intent)
        })
    }

    private fun setEmergencyEvent(emergencygrid: LinearLayout) {
        emergencygrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterarCellDashboard, EmergencyContact::class.java)
            intent.putExtra("info", "Notice Board Activity")
            startActivity(intent)
        })
    }

    private fun setacdemicCalEvent(academic_cal_board: LinearLayout) {
        academic_cal_board.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterarCellDashboard, AcademicCalender::class.java)
            intent.putExtra("info", "Notice board")
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

    private fun displayhelphostelalert() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_hostle, null)
       // val txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl2)
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

    private fun setHelpHostelalertEvent(hostelboard: LinearLayout) {
        hostelboard.setOnClickListener(View.OnClickListener {
            displayhelphostelalert()

        })
    }
    private fun setfeedbackScheduleEvent(feedbackScheduleGrid: LinearLayout) {
        feedbackScheduleGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterarCellDashboard, RegistrarFeedbackSchdule::class.java)
            intent.putExtra("info", "Notice board")
            startActivity(intent)

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
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                println(dialog)
                println(id)
                finishAffinity()
                System.exit(0)

            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                println(dialog)
                println(id)
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box

        // show alert dialog
        alert.show()
    }
}