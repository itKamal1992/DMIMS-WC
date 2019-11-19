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
import android.widget.Toast
import com.dmims.dmims.*
import com.dmims.dmims.activity.*
import com.dmims.dmims.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.admin_dashboard.*
import java.util.*
import kotlin.system.exitProcess


class AdminDashboard : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        when (p0!!.id) {
//                    noticeboardgrid
//                    notification
//                    noticeInboxGrid
//                    helpdiloadboad
//                    academicCalBoard
//                    academicCalUploadBoard

            R.id.academic_cal_board -> {
                val intent = Intent(this@AdminDashboard, AcademicCalender::class.java)
                startActivity(intent)
            }
            R.id.noticeboardgrid -> {
                val intent = Intent(this@AdminDashboard, AdminNoticeBoard::class.java)
                startActivity(intent)
            }
            R.id.notification -> {
                val intent = Intent(this@AdminDashboard, Activity_Notification_Admin::class.java)
                startActivity(intent)
            }
            R.id.noticeInboxGrid -> {
                val intent = Intent(this@AdminDashboard, Activity_Admin_Inbox_notice::class.java)
                startActivity(intent)
            }
            R.id.helpdiloadboad -> {
                displayhelpalert()
            }
            R.id.upload_aca_Cal -> {
                val intent = Intent(this@AdminDashboard, AcademicCalenderUploadA::class.java)
                startActivity(intent)
            }

        }
    }



    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    private var id_admin: String? = null
    private var roleadmin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)
        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        drawer_title.text = mypref.getString("key_drawer_title", null)
        user_role.text = "User : "+mypref.getString("key_userrole", null)
        txt_designation.text= "Designation : "+mypref.getString("key_designation", null)
        txt_Email.text= "E-mail: "+mypref.getString("key_email", null)
        txt_Mobile.text= "MB No : "+mypref.getString("key_editmob", null)
        enroll_no.text= "ID : "+mypref.getString("Stud_id_key", null)

        val mypref12 = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref12.edit()
        editor.putString("dashboard", "Admin Dashboard")
        editor.apply()

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

        noticeboardgrid.setOnClickListener(this)
        noticeInboxGrid.setOnClickListener(this)
        notification.setOnClickListener(this)
        academic_cal_board.setOnClickListener(this)
        upload_aca_Cal.setOnClickListener(this)
        helpdiloadboad.setOnClickListener(this)

        // Set navigation view navigation item selected listener
        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.action_calender -> {
                    val intent = Intent(this@AdminDashboard, AcademicCalender::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }
                R.id.action_noticeboard -> {
                    val intent = Intent(this@AdminDashboard, AdminNoticeBoard::class.java)
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent =
                        Intent(this@AdminDashboard, Activity_Notification_Admin::class.java)
                    startActivity(intent)
                }
                R.id.action_inbox -> {
                    val intent =
                        Intent(this@AdminDashboard, Activity_Admin_Inbox_notice::class.java)
                    intent.putExtra("info", "Notice board")
                    startActivity(intent)
                }

                R.id.action_calender_upload -> {
                    val intent = Intent(this@AdminDashboard, AcademicCalenderUploadA::class.java)
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
                    val intentlogout = Intent(
                        this@AdminDashboard,
                        com.dmims.dmims.activity.SplashScreen::class.java
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
            this@AdminDashboard.runOnUiThread(Runnable {
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



    private fun displayhelpalert() {
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
        exitDialog()
    }

    private fun exitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_exit, null)
        dialogBuilder.setView(dialogView)
            .setCancelable(false)
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