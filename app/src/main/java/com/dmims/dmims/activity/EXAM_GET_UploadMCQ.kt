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
import com.dmims.dmims.adapter.ExamGetMCQAdapter
import com.dmims.dmims.adapter.StudentNotificationAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.McqFields
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.MCQListUpload
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class EXAM_GET_UploadMCQ : AppCompatActivity() {
    var date_of_admiss_k: String = "-"
    var stud_k2: String? = null
    var COURSE_ID: String? = "-"
    var instname : String = "-"
    var k: Int = 0
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_get_mcq_upload)
        stud_k2 = intent.getStringExtra("stud_k2")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        mServices = Common.getAPI()
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
//        COURSE_ID = mypref.getString("course_id", null)
//        instname = mypref.getString("key_institute_stud", null)
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

//        Toast.makeText(this@Student_CurrentNotification, begining.toString() + "  and   " + end.toString(),Toast.LENGTH_SHORT).show()
        if (InternetConnection.checkConnection(this)) {
        progressBar.visibility = View.VISIBLE
        try {
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            var call2: Call<MCQListUpload> = phpApiInterface.GetUploadMCQ()
            call2.enqueue(object : Callback<MCQListUpload>{
                override fun onFailure(call: Call<MCQListUpload>, t: Throwable) {
                    Toast.makeText(this@EXAM_GET_UploadMCQ, t.message, Toast.LENGTH_SHORT).show()
//                        progressBar!!.visibility = View.INVISIBLE
                    progressBar.visibility = View.GONE
                }

                override fun onResponse(call: Call<MCQListUpload>, response: Response<MCQListUpload>) {
                    val result: MCQListUpload? = response.body()
                    println("result 1 >>> " + result!!.Data!![0])
                    var listSize = result.Data!!.size
                    progressBar!!.visibility = View.GONE
                    if (result!!.Data!![0].id=="error")
                    {
                        var msg="MCQ Answer Key Not found for the Exam Admin"

                        GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
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

                    }
                    else
                    {
                        val users = ArrayList<McqFields>()
                        if (listSize!=0) {
                            for (i in 0..listSize - 1)
                            {
                                if(result.Data!![i].FileUrl.contains(".jpg",ignoreCase = true)||result.Data!![i].FileUrl.contains(".jpeg",ignoreCase = true)||result.Data!![i].FileUrl.contains(".png",ignoreCase = true))
                                {
                                    k = R.drawable.ic_jpg
                                }else
                                    if(result.Data!![i].FileUrl.contains(".pdf",ignoreCase = true)) {
                                        k = R.drawable.icon_pdf
                                    }
                                    else {
                                        k = R.drawable.ic_anotice_no
                                    }
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

                            val adapter = ExamGetMCQAdapter(users,this@EXAM_GET_UploadMCQ)
                            recyclerView.adapter = adapter
                        }
                        //else

//                            progressBar!!.visibility = View.INVISIBLE

//                            if(listSize==0){
//                                progressBar!!.visibility = View.GONE
//
//                            }

                    }
//                        {UserName
//                            var listSize = result.Data14!!.size
//                            val users = ArrayList<McqFields>()
//                            println("result 4>>> " + users)
//
//                                for (i in 0..listSize - 1) {
//                                    if (result.Data14!![i].STUDENT_FLAG == "T") {
//                                        if (result.Data14!![i].COURSE_ID == "All" || result.Data14!![i].COURSE_ID == COURSE_ID) {
//                                            if (result.Data14!![i].INSTITUTE_NAME == instname || result.Data14!![i].INSTITUTE_NAME == "ADMIN") {
//
//                                                if (result.Data14!![i].RESOU_FLAG == "T") {
//                                                    k = R.drawable.ic_notice_yes
//                                                } else {
//                                                    k = R.drawable.ic_anotice_no
//                                                }
//                                                users.add(
//                                                    McqFields(
//                                                        result.Data14!![i].NOTICE_TITLE,
//                                                        result.Data14!![i].USER_ROLE,
//                                                        result.Data14!![i].USER_TYPE,
//                                                        result.Data14!![i].NOTICE_TYPE,
//                                                        result.Data14!![i].NOTICE_DESC,
//                                                        result.Data14!![i].NOTICE_DATE,
//                                                        result.Data14!![i].INSTITUTE_NAME,
//                                                        result.Data14!![i].COURSE_NAME,
//                                                        result.Data14!![i].COURSE_ID,
//                                                        result.Data14!![i].DEPT_NAME,
//                                                        result.Data14!![i].DEPT_ID,
//                                                        result.Data14!![i].RESOU_FLAG,
//                                                        result.Data14!![i].FILENAME,
//                                                        k
//                                                    )
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            progressBar!!.visibility = View.INVISIBLE
//                            progressBar!!.visibility = View.GONE
//                            if(users.isEmpty()){
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            }else {
//
//                                val adapter = ExamGetMCQAdapter(users)
//                                recyclerView.adapter = adapter
//                            }
//
//                        }
//                        else
//                        {
//                            if (result.Status.equals("No data found", ignoreCase = true)) {
//
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//
//                                var msg="No Notices found for the current request"
//                                GenericPublicVariable.CustDialog = Dialog(this@EXAM_GET_UploadMCQ)
//                                GenericPublicVariable.CustDialog.setContentView(R.layout.api_oops_custom_popup)
//                                var ivNegClose1: ImageView =
//                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//                                tvMsg.text = msg
//                                GenericPublicVariable.CustDialog.setCancelable(false)
//                                btnOk.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//
//                                }
//                                ivNegClose1.setOnClickListener {
//                                    GenericPublicVariable.CustDialog.dismiss()
//                                    onBackPressed()
//                                }
//                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                                GenericPublicVariable.CustDialog.show()
//                            } else {
//                                progressBar!!.visibility = View.INVISIBLE
//                                progressBar!!.visibility = View.GONE
//                                println("result 3>>>" + result.Status)
//                                Toast.makeText(
//                                    this@EXAM_GET_UploadMCQ,
//                                    result.Status,
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//                        }
                }
//
//                }
            }


//            {
//                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//
//
//
//                    }
//
//                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
//                         }

            )

        }
        catch (ex: Exception) {
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
