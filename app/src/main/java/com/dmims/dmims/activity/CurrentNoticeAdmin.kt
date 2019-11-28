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
import com.dmims.dmims.adapter.NoticeAdapterCurrent
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CurrentNoticeAdmin : AppCompatActivity() {
    var date_of_admiss_k: String = "-"
    var stud_k2: String? = null
    var COURSE_ID: String = "-"
    var k:Int = 0

    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_notice)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        mServices = Common.getAPI()
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
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

//        Toast.makeText(this@CurrentNoticeAdmin, begining.toString() + "  and   " + end.toString(), Toast.LENGTH_SHORT).show()
        if (InternetConnection.checkConnection(this)) {
            progressBar.visibility = View.VISIBLE
        try {
            mServices.GetNotice(begining.toString(), end.toString())
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {

                        Toast.makeText(this@CurrentNoticeAdmin, t.message, Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.INVISIBLE
                        progressBar.visibility = View.GONE

                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        println("result 1 >>> "+result.toString())
                        if (result!!.Status == "ok") {
                            var listSize = result.Data14!!.size
                            val users = ArrayList<NoticeStudCurrent>()
                            println("result 4>>> "+users)

                                for (i in 0..listSize - 1) {
                                    if (result.Data14!![i].ADMIN_FLAG == "T") {
                                        // if (result!!.Data14!![i].COURSE_ID == "All" || result!!.Data14!![i].COURSE_ID == COURSE_ID) {
                                        if (result.Data14!![i].RESOU_FLAG == "T") {

                                            result.Data14!![i].ID

                                            if(result.Data14!![i].FILENAME.contains(".jpg",ignoreCase = true)||result.Data14!![i].FILENAME.contains(".jpeg",ignoreCase = true)||result.Data14!![i].FILENAME.contains(".png",ignoreCase = true))
                                            {
                                                k = R.drawable.ic_jpg
                                            }else
                                                if(result.Data14!![i].FILENAME.contains(".pdf",ignoreCase = true)) {
                                                    k = R.drawable.icon_pdf
                                                }
//                                        ???

                                        } else {
                                            k = R.drawable.ic_anotice_no
                                        }
                                        users.add(
                                            NoticeStudCurrent(
                                                "Notice Title : " + result.Data14!![i].NOTICE_TITLE,
                                                "Sender : " + result.Data14!![i].USER_ROLE,
                                                "Notice For : " + result.Data14!![i].USER_TYPE,
                                                "Notice Type : " + result.Data14!![i].NOTICE_TYPE,
                                                "Notice Desc : " + result.Data14!![i].NOTICE_DESC,
                                                "Notice Date : " + result.Data14!![i].NOTICE_DATE,
                                                "Institute : " + result.Data14!![i].INSTITUTE_NAME,
                                                "Course Name : " + result.Data14!![i].COURSE_NAME,
                                                "Course ID : " + result.Data14!![i].COURSE_ID,
                                                "Dept Name : " + result.Data14!![i].DEPT_NAME,
                                                "Dept ID : " + result.Data14!![i].DEPT_ID,
                                                "ATTACHMENT STATUS: " + result.Data14!![i].RESOU_FLAG,
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
                                progressBar.visibility = View.GONE
                                val adapter = NoticeAdapterCurrent(users, this@CurrentNoticeAdmin)
                                recyclerView.adapter = adapter

                        }
                        else {
                            if (result.Status.equals("No data found", ignoreCase = true)) {

                                progressBar!!.visibility = View.INVISIBLE
                                progressBar!!.visibility = View.GONE

                                var msg="No Notices found for the current request"
                                GenericPublicVariable.CustDialog = Dialog(this@CurrentNoticeAdmin)
                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
                                var ivNegClose1: ImageView =
                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
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
                                progressBar!!.visibility = View.INVISIBLE
                                progressBar!!.visibility = View.GONE
                                println("result 3>>>" + result.Status)
                                Toast.makeText(
                                    this@CurrentNoticeAdmin,
                                    result.Status,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                })

        }catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
    }else {
            progressBar!!.visibility = View.INVISIBLE
            progressBar.visibility = View.GONE
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr)
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
