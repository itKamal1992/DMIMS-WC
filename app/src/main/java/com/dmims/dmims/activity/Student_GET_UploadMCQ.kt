/* Created by Umesh Gaidhane*/
package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.adapter.ExamGetMCQAdapter
import com.dmims.dmims.adapter.StudentGetMCQAdapter
import com.dmims.dmims.adapter.StudentNotificationAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.dataclass.McqFields
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.MCQListUpload
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import kotlinx.android.synthetic.main.activity_student_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Student_GET_UploadMCQ : AppCompatActivity() {
    var date_of_admiss_k: String = "-"
    var stud_k2: String? = null
    var COURSE_ID: String? = "-"
    var Stud_ID: String? = "-"
    var Stud_Name: String? = "-"
    var Stud_Course: String? = "-"
    var Stud_Institute: String? = "-"


    var instname: String = "-"
    var k: Int = 0
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var cal2 = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_get_mcq_upload)
        stud_k2 = intent.getStringExtra("stud_k2")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        mServices = Common.getAPI()


        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref.getString("course_id", null)
        Stud_ID = mypref.getString("Stud_id_key", null)
        Stud_Name = mypref.getString("key_drawer_title", null)
        Stud_Course = mypref.getString("key_institute", null)
        Stud_Institute = mypref.getString("key_institute_stud", null)
        val recyclerView = findViewById<RecyclerView>(R.id.attendance_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        cal.set(
            Calendar.DAY_OF_MONTH,
            cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        )
        setTimeToBeginningOfDay(cal)
        var begining = sdf.format(cal.time).toString()
        // Toast.makeText(this, begining.toString(), Toast.LENGTH_SHORT)

        cal.set(
            Calendar.DAY_OF_MONTH,
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        setTimeToEndofDay(cal)
        var end = sdf.format(cal.time).toString()

        val myFormat_send = "yyyy-MM-dd" // mention the format you need
        val sdf_send = SimpleDateFormat(myFormat_send, Locale.US)
        var current_date_send:String =sdf_send.format(cal2.time).toString()

//        Toast.makeText(this@Student_CurrentNotification, begining.toString() + "  and   " + end.toString(),Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.VISIBLE
        try {
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            var call2: Call<MCQListUpload> = phpApiInterface.GetUploadMCQbyDate(current_date_send)
            call2.enqueue(object : Callback<MCQListUpload> {
                override fun onFailure(call: Call<MCQListUpload>, t: Throwable) {
                    Toast.makeText(this@Student_GET_UploadMCQ, t.message, Toast.LENGTH_SHORT).show()
                    progressBar!!.visibility = View.INVISIBLE
                    progressBar.visibility = View.GONE
                }

                override fun onResponse(call: Call<MCQListUpload>, response: Response<MCQListUpload>) {
                    val result: MCQListUpload? = response.body()
                    println("result 1 >>> " + result!!.Data!![0])
                    var listSize = result.Data!!.size
                    if (result!!.Data!!.isEmpty()) {

                    } else {
                        val users = ArrayList<McqFields>()
                        for (i in 0..listSize - 1) {
                            if (result.Data!![i].Institute == "All" || result.Data!![i].Institute == Stud_Institute) {

                                if (result.Data!![i].Course == "All" || result.Data!![i].Course == Stud_Course){

                                    k = R.drawable.ic_notice_yes
                                    users.add(
                                        McqFields(
                                            result.Data!![i].id,
                                            result.Data!![i].UserName,
                                            result.Data!![i].UserMobileNO,
                                            result.Data!![i].UserRole,
                                            result.Data!![i].UserEmail,
                                            result.Data!![i].UserDesig,
                                            result.Data!![i].Institute,
                                            result.Data!![i].Course,
                                            result.Data!![i].Department,
                                            result.Data!![i].Year,
                                            result.Data!![i].McqUploadDate,
                                            result.Data!![i].StartDate,
                                            result.Data!![i].EndDate,
                                            result.Data!![i].FileUrl,
                                            result.Data!![i].FileName,
                                            k
                                        )
                                    )

                                }

                            }

                        }

                        progressBar!!.visibility = View.INVISIBLE
                        progressBar!!.visibility = View.GONE
                        if (users.isEmpty()) {
                            var msg = "Exam Answer Key Not given from Exam Admin"
                            GenericPublicVariable.CustDialog = Dialog(this@Student_GET_UploadMCQ)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
                            var ivNegClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                            var btnOk: Button =
                                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                            var tvMsg: TextView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                            tvMsg.text = msg
                            GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                onBackPressed()

                            }
                            ivNegClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                onBackPressed()
                            }
                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            GenericPublicVariable.CustDialog.show()
                        } else {

                            val adapter = StudentGetMCQAdapter(users, this@Student_GET_UploadMCQ)
                            recyclerView.adapter = adapter
                        }

                    }
                }

            }
            )

        } catch (ex: Exception) {
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }

    }

    fun setTimeToBeginningOfDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }

    fun setTimeToEndofDay(calendar: Calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
    }
  
}
