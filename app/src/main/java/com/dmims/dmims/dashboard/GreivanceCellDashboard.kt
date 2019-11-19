package com.dmims.dmims.dashboard

import android.app.ProgressDialog
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
import com.dmims.dmims.model.DeptListStudDataRef
import kotlinx.android.synthetic.main.faculty_dashboard.*
import java.util.*

class GreivanceCellDashboard : AppCompatActivity() {
    lateinit var greivanceGrid: LinearLayout
    lateinit var notificationGrid: LinearLayout
    lateinit var helpdiloadboad: LinearLayout
    lateinit var View_Griv_Princ: LinearLayout
    lateinit var drawerTitle: TextView
    lateinit var enrollNo: TextView
    lateinit var user_role: TextView
    lateinit var viewPager: ViewPager
    lateinit var sliderDotsPanel: LinearLayout
    private var dotscount: Int = 0
    private var dots: Array<ImageView?>? = null
    private lateinit var id_admin: String
    private lateinit var roleadmin: String



    lateinit var time_table_grid: LinearLayout
    lateinit var noticeboardgrid: LinearLayout
    lateinit var notification: LinearLayout
    lateinit var emergencygrid: LinearLayout



    lateinit var title_Mobile: TextView
    lateinit var title_Institute: TextView
    lateinit var title_Course: TextView

    private var dotsCount: Int = 0
    private var dateOfAdmission: String? = "-"
    var COURSE_ID: String? = null
    private var Deptlist: ArrayList<DeptListStudDataRef>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.greivanancecell_dashboard)
        greivanceGrid = findViewById<View>(R.id.greivanceGrid) as LinearLayout
        notificationGrid = findViewById<View>(R.id.notificationGrid) as LinearLayout
        helpdiloadboad = findViewById<View>(R.id.helpdiloadboad) as LinearLayout
        View_Griv_Princ = findViewById<View>(R.id.view_Griv_Princ) as LinearLayout
        drawerTitle = findViewById(R.id.drawer_title)
        enrollNo = findViewById(R.id.enroll_no)
        user_role = findViewById(R.id.user_role)
        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        var drawer_titler = mypref.getString("key_drawer_title", null)
        var enroll_nor = mypref.getString("key_enroll_no", null)
        id_admin = mypref.getString("Stud_id_key", null)!!
        roleadmin = mypref.getString("key_userrole", null)!!
        drawerTitle.text = drawer_titler
        enrollNo.text = enroll_nor
        user_role.text = roleadmin
        //Set Event
        setSingleEvent(greivanceGrid)
        setViewGrievance(View_Griv_Princ)
        setNoticeEvent(notificationGrid)
        setHealerEvent(helpdiloadboad)
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
                R.id.action_r_grev -> {
                    val intent = Intent(this@GreivanceCellDashboard, Activity_GetGreivance::class.java)
                    intent.putExtra("info", "Attendance Activity")
                    startActivity(intent)
                }
                R.id.action_notification -> {
                    val intent = Intent(this@GreivanceCellDashboard, Activity_Greivance_Notification::class.java)
                    intent.putExtra("roleadmin", roleadmin)
                    intent.putExtra("id_admin", id_admin)
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
                    val intentlogout =
                        Intent(this@GreivanceCellDashboard, com.dmims.dmims.activity.SplashScreen::class.java)
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
            this@GreivanceCellDashboard.runOnUiThread(Runnable {
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
            val intent = Intent(this@GreivanceCellDashboard, Activity_GetGreivance::class.java)
            intent.putExtra("info", "Get Greivance Activity")
            startActivity(intent)
        })
    }

    private fun setViewGrievance(View_Griv_Princ:LinearLayout)
    {View_Griv_Princ.setOnClickListener {
        val intent = Intent(this@GreivanceCellDashboard, ViewGrievance::class.java)
        startActivity(intent)
    }

    }

    private fun setNoticeEvent(notificationGrid: LinearLayout) {
        notificationGrid.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@GreivanceCellDashboard, Activity_Greivance_Notification::class.java)
            intent.putExtra("roleadmin", roleadmin)
            intent.putExtra("id_admin", id_admin)
            startActivity(intent)
        })
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

    override fun onBackPressed()
    {
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
                finishAffinity()
                System.exit(0)

            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        // show alert dialog
        alert.show()
    }
}