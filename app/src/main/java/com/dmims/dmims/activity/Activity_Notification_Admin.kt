package com.dmims.dmims.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.NoticeAdapterCurrent
import com.dmims.dmims.adapter.NoticeDeleteAdapterCurrent
import com.dmims.dmims.adapter.StudentNotificationAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI

import kotlinx.android.synthetic.main.activity_notification__admin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Activity_Notification_Admin : AppCompatActivity() {
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var search_id: Button? = null
    var k: Int = 0
    var btn_current_id: Button? = null
    var current_date: String? = "-"
    var COURSE_ID: String? = "-"
    var STUD_ID: String? = "-"
    var progressBar: ProgressBar? = null
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_admin_notification)
        setSupportActionBar(toolbar)
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref.getString("course_id", null)
        STUD_ID = mypref.getString("STUD_ID", null)

        to_date = findViewById(R.id.select_to_date)
        from_date = findViewById(R.id.select_from_date)
        search_id = findViewById<Button>(R.id.search_id)
        btn_current_id = findViewById<Button>(R.id.btn_currentattend)
        progressBar = findViewById<ProgressBar>(R.id.progressBar9)
        mServices = Common.getAPI()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        to_date!!.text = sdf.format(cal.time).toString()
        from_date!!.text = sdf.format(cal.time).toString()
        to_date_sel = sdf.format(cal.time)
        current_date = sdf.format(cal.time)
        from_date_sel = sdf.format(cal.time)

        val recyclerView = findViewById<RecyclerView>(R.id.attendance_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        if (InternetConnection.checkConnection(this@Activity_Notification_Admin)) {
            try {
                mServices.GetNotice(to_date_sel, from_date_sel)
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(
                                this@Activity_Notification_Admin,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar!!.visibility = View.INVISIBLE
                            progressBar!!.visibility = View.GONE

                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            val result: APIResponse? = response.body()
                            println("result 1 >>> " + result.toString())
                            if (result!!.Status == "ok") {
                                println("result >>> $result")
                                println("result.Data14!! >>> " + result.Data14!!)
                                var listSize = result.Data14!!.size
                                val users = ArrayList<NoticeStudCurrent>()
                                println("result 4>>> " + users)

                                for (i in 0..listSize - 1) {
                                    if (result.Data14!![i].ADMIN_FLAG == "T") {
                                        //    if (result!!.Data14!![i].COURSE_ID == "All" || result!!.Data14!![i].COURSE_ID == COURSE_ID) {
                                        if (result.Data14!![i].RESOU_FLAG == "T") {
                                            k = R.drawable.ic_notice_yes
                                        } else {
                                            k = R.drawable.ic_anotice_no
                                        }
                                        users.add(
                                            NoticeStudCurrent(
                                                result.Data14!![i].NOTICE_TITLE,
                                                result.Data14!![i].USER_ROLE,
                                                result.Data14!![i].USER_TYPE,
                                                result.Data14!![i].NOTICE_TYPE,
                                                result.Data14!![i].NOTICE_DESC,
                                                result.Data14!![i].NOTICE_DATE,
                                                result.Data14!![i].INSTITUTE_NAME,
                                                result.Data14!![i].COURSE_NAME,
                                                result.Data14!![i].COURSE_ID,
                                                result.Data14!![i].DEPT_NAME,
//                                                result!!.Data14!![i].DEPT_ID,
                                                result.Data14!![i].ID,// in this we have pass ROW ID instead of DEPT_ID() to delete perticular notice
                                                result.Data14!![i].RESOU_FLAG,
                                                result.Data14!![i].FILENAME,
                                                result.Data14!![i].YEAR,

                                                result.Data14!![i].STUDENT_FLAG,
                                                result.Data14!![i].FACULTY_FLAG,
                                                result.Data14!![i].ADMIN_FLAG,
                                                k
                                            )
                                        )
                                        // }
                                    }
                                }
                                progressBar!!.visibility = View.INVISIBLE
                                progressBar!!.visibility = View.GONE
                                val adapter = NoticeDeleteAdapterCurrent(
                                    users,
                                    this@Activity_Notification_Admin
                                )//StudentNotificationAdapter
                                recyclerView.adapter = adapter

                            } else {
                                if (result.Status.equals("No data found", ignoreCase = true)) {

                                    progressBar!!.visibility = View.INVISIBLE
                                    progressBar!!.visibility = View.GONE
                                    GenericUserFunction.showOopsError(
                                        this@Activity_Notification_Admin,
                                        "No Notices found for the current request"
                                    )
                                } else {
                                    progressBar!!.visibility = View.INVISIBLE
                                    progressBar!!.visibility = View.GONE
                                    println("result 3>>>" + result.Status)
                                    Toast.makeText(
                                        this@Activity_Notification_Admin,
                                        result.Status,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    })

            } catch (ex: Exception) {

                ex.printStackTrace()
                progressBar!!.visibility = View.INVISIBLE
                progressBar!!.visibility = View.GONE
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }else {
            progressBar!!.visibility = View.INVISIBLE
            progressBar!!.visibility = View.GONE
        GenericUserFunction.showInternetNegativePopUp(
            this@Activity_Notification_Admin,
            getString(R.string.failureNoInternetErr)
        )
    }

        btn_current_id!!.setOnClickListener {
            val intent =
                Intent(this@Activity_Notification_Admin, Admin_CurrentNotification::class.java)
            startActivity(intent)
        }

        search_id!!.setOnClickListener {

            validateDate()
            if (InternetConnection.checkConnection(this)) {
            progressBar!!.visibility = View.VISIBLE
            try {
                mServices.GetNotice(to_date_sel, from_date_sel)
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                            Toast.makeText(
                                this@Activity_Notification_Admin,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar!!.visibility = View.INVISIBLE
                            progressBar!!.visibility = View.GONE

                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            val result: APIResponse? = response.body()
                            println("result 1 >>> " + result.toString())
                            if (result!!.Status == "ok") {
                                var listSize = result.Data14!!.size
                                val users = ArrayList<NoticeStudCurrent>()
                                println("result 4>>> " + users)

                                for (i in 0..listSize - 1) {
                                    if (result.Data14!![i].ADMIN_FLAG == "T") {
                                        // if (result!!.Data14!![i].COURSE_ID == "All" || result!!.Data14!![i].COURSE_ID == COURSE_ID) {
                                        if (result.Data14!![i].RESOU_FLAG == "T") {
                                            k = R.drawable.ic_notice_yes
                                        } else {
                                            k = R.drawable.ic_anotice_no
                                        }
                                        users.add(
                                            NoticeStudCurrent(
                                                result.Data14!![i].NOTICE_TITLE,
                                                result.Data14!![i].USER_ROLE,
                                                result.Data14!![i].USER_TYPE,
                                                result.Data14!![i].NOTICE_TYPE,
                                                result.Data14!![i].NOTICE_DESC,
                                                result.Data14!![i].NOTICE_DATE,
                                                result.Data14!![i].INSTITUTE_NAME,
                                                result.Data14!![i].COURSE_NAME,
                                                result.Data14!![i].COURSE_ID,
                                                result.Data14!![i].DEPT_NAME,
//                                                    result!!.Data14!![i].DEPT_ID,
                                                result.Data14!![i].ID,// in this we have pass ROW ID instead of DEPT_ID() to delete perticular notice
                                                result.Data14!![i].RESOU_FLAG,
                                                result.Data14!![i].FILENAME,
                                                result.Data14!![i].YEAR,

                                                result.Data14!![i].STUDENT_FLAG,
                                                result.Data14!![i].FACULTY_FLAG,
                                                result.Data14!![i].ADMIN_FLAG,
                                                k
                                            )
                                        )
                                        // }
                                    }
                                }
                                progressBar!!.visibility = View.INVISIBLE
                                progressBar!!.visibility = View.GONE
//                                    val adapter = StudentNotificationAdapter(users)
                                val adapter = NoticeDeleteAdapterCurrent(
                                    users,
                                    this@Activity_Notification_Admin
                                )
                                recyclerView.adapter = adapter

                            } else {
                                if (result.Status.equals("No data found", ignoreCase = true)) {

                                    progressBar!!.visibility = View.INVISIBLE
                                    progressBar!!.visibility = View.GONE
                                    GenericUserFunction.showOopsError(
                                        this@Activity_Notification_Admin,
                                        "No Notices found for the current request"
                                    )
                                } else {
                                    progressBar!!.visibility = View.INVISIBLE
                                    progressBar!!.visibility = View.GONE
                                    println("result 3>>>" + result.Status)
                                    Toast.makeText(
                                        this@Activity_Notification_Admin,
                                        result.Status,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        }
                    })

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }else {
//                progressBar!!.visibility = View.INVISIBLE
//                progressBar!!.visibility = View.GONE
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

        }
        /* DatePicker Listener --End*/
    }

    fun clickFromDataPicker(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                // Display Selected date in Toast
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                from_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun clickToDataPicker(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                // Display Selected date in Toast
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                to_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun validateDate() {
        if (to_date!!.text.isEmpty()) {
            to_date!!.error = "Please select to date"
            return
        } else {
            to_date_sel = to_date!!.text.toString()
        }

        if (from_date!!.text.isEmpty()) {
            to_date!!.error = "Please select from date"
            return
        } else {
            from_date_sel = from_date!!.text.toString()
        }
    }

}