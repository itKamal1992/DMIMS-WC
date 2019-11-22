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
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.GrievanceAdapterNewStud
import com.dmims.dmims.adapter.GrievanceAdapterStudShow
import com.dmims.dmims.adapter.NoticeAdapterCurrent
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.dataclass.StudentGrievanceGet
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CurrentGrievance : AppCompatActivity()
{
    var date_of_admiss_k: String = "-"
    var stud_k2: String? = null
    var COURSE_ID: String? = null
    var instname : String? = null
    var k:Int = 0

    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var student_id_key:String = ""
    var roll:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_grievance)
//        stud_k2 = intent.getStringExtra("stud_k2")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        mServices = Common.getAPI()
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref.getString("course_id", null)
        mypref.getString("course_id", null)
        instname = mypref.getString("key_institute_stud", null)
        student_id_key = mypref.getString("Stud_id_key", null)!!
        roll=mypref.getString("key_userrole", null)

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
        cal.set(
            Calendar.DAY_OF_MONTH,
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        setTimeToEndofDay(cal)
        var end = sdf.format(cal.time).toString()

//        Toast.makeText(this@CurrentNotice, begining.toString() + "  and   " + end.toString(), Toast.LENGTH_SHORT).show()
        if (InternetConnection.checkConnection(this)) {
        progressBar.visibility = View.VISIBLE
        try {
            mServices.GetDatewiseOnlineGrievanceReport(begining.toString(), end.toString())
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

                mServices.GetDatewiseOnlineGrievanceReport(begining.toString(), end.toString())
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            progressBar.visibility = View.GONE
                            GenericUserFunction.showOopsError(
                                this@CurrentGrievance,
                                t.message.toString().capitalize()
                            )

                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            val result: APIResponse? = response.body()
                            println("result 1 >>> " + result.toString())
                            if (result!!.Status == "ok") {
                                var listSize = result.Data17!!.size
                                val users = ArrayList<StudentGrievanceGet>()
                                println("result 4>>> " + users)

                                for (i in 0..listSize - 1) {
                                    if (result.Data17!![i].STUD_ID.equals(student_id_key)) {
                                    if (result.Data17!![i].G_ATTACHMENT == "T") {
//                                        k = R.drawable.ic_notice_yes
//                                    result.Data17!![i].ID

                                        if (result.Data17!![i].ATTACHMENT_URL.contains(
                                                ".jpg",
                                                ignoreCase = true
                                            ) || result.Data17!![i].ATTACHMENT_URL.contains(
                                                ".jpeg",
                                                ignoreCase = true
                                            )|| result.Data17!![i].ATTACHMENT_URL.contains(
                                                ".png",
                                                ignoreCase = true
                                            )
                                        ) {
                                            k = R.drawable.ic_jpg
                                        } else
                                            if (result.Data17!![i].ATTACHMENT_URL.contains(
                                                    ".pdf",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                k = R.drawable.icon_pdf
                                            }
                                    } else {
                                        k = R.drawable.ic_anotice_no
                                    }


                                    users.add(
                                        StudentGrievanceGet(
                                            result.Data17!![i].G_ID,
                                            result.Data17!![i].STUD_ID,
                                            result.Data17!![i].course_id,
                                            result.Data17!![i].roll_no,
                                            result.Data17!![i].Grev_name,
                                            result.Data17!![i].Inst_Name,
                                            result.Data17!![i].Comp_To,
                                            result.Data17!![i].Grev_Filename,
                                            result.Data17!![i].DEPARTMENT,
                                            result.Data17!![i].ATTACHMENT_URL,
                                            result.Data17!![i].G_SUBJECT,
                                            result.Data17!![i].G_CATEGORY,
                                            result.Data17!![i].G_AGAINST,
                                            result.Data17!![i].G_DISCRIPTION,
                                            result.Data17!![i].G_DATE,
                                            result.Data17!![i].G_ATTACHMENT,
                                            result.Data17!![i].G_STATUS,
                                            result.Data17!![i].ASSING_TO_ID,
                                            result.Data17!![i].REMINDER,
                                            result.Data17!![i].G_COMMENT,
                                            result.Data17!![i].TO_DATE,
                                            result.Data17!![i].FROM_DATE,
                                            k,
                                            false

                                        )
                                    )
                                }
                                }
                                if (users.isEmpty()) {
                                    GenericUserFunction.showOopsError(
                                        this@CurrentGrievance,
                                        "No Grievance found for the current request"
                                    )
                                } else {
                                    val adapter =
                                        GrievanceAdapterNewStud(
                                            users,
                                            this@CurrentGrievance
                                        )
                                    recyclerView.adapter = adapter
                                }
                            }
                            progressBar.visibility = View.GONE
                        }
                    })

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }

    }
    else
    {
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr))
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
