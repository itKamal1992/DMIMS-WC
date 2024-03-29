package com.dmims.dmims.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.AttendanceAdapterCurrent
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.AttendanceStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CurrentAttendance : AppCompatActivity() {
    var date_of_admiss_k: String = "-"
    var stud_k2: String? = null
    var COURSE_ID: String = "-"
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_attendance)
        stud_k2 = intent.getStringExtra("stud_k2")
        val progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        mServices = Common.getAPI()
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref.getString("course_id", null)!!
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

//        Toast.makeText(this@CurrentAttendance, begining.toString() + "  and   " + end.toString(), Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.VISIBLE
        if (InternetConnection.checkConnection(this)) {
        try {
            mServices.GetProgressiveAttend(stud_k2!!.toInt(), begining.toString(), end.toString(), COURSE_ID)
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@CurrentAttendance, t.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Status == "ok") {
                            var listSize = result.Data13!!.size
                            val users = ArrayList<AttendanceStudCurrent>()
                            for (i in 0..listSize - 1) {
                                val per_daycount: Double =
                                    ((result.Data13!![i].THEORY.toDouble() + result.Data13!![i].PRACTICAL.toDouble() + result.Data13!![i].CLINICAL.toDouble()) / (result.Data13!![i].NO_LECTURER.toDouble())) * 100.toFloat()
                                var fperatte: Double =
                                    String.format("%.2f", per_daycount).toDouble()
                                if (fperatte.isNaN()) {
                                    fperatte = 0.0
                                }
                                if (fperatte != 0.0) {
                                    var TheoryTotal=result.Data13!![i].THEORY
                                    var TheoryAbsent=result.Data13!![i].THEORY_ABSENT
                                    var cal_one=TheoryTotal.toDouble()-TheoryAbsent.toDouble()
                                    var TheoryPercentage=cal_one/TheoryTotal.toDouble()*100
                                    TheoryPercentage=ConvertToPoint(TheoryPercentage)

                                    var PracticalTotal=result.Data13!![i].PRACTICAL
                                    var PracticalAbsent=result.Data13!![i].PRACTICAL_ABSENT
                                    var cal_two=PracticalTotal.toDouble()-PracticalAbsent.toDouble()
                                    var PracticalPercentage=cal_two/TheoryTotal.toDouble()*100
                                    PracticalPercentage=ConvertToPoint(PracticalPercentage)

                                    var ClinicalTotal=result.Data13!![i].CLINICAL
                                    var ClinicalAbsent=result.Data13!![i].CLINICAL_ABSENT
                                    var cal_three=ClinicalTotal.toDouble()-ClinicalAbsent.toDouble()
                                    var ClinicalPercentage=cal_three/ClinicalTotal.toDouble()*100
                                    ClinicalPercentage=ConvertToPoint(ClinicalPercentage)

                                    users.add(
                                        AttendanceStudCurrent(
                                            "Dept Name : " + result.Data13!![i].DEPT_NAME,
                                            "Dept ID : " + result.Data13!![i].DEPT_ID,
                                            "Theory : " + result.Data13!![i].THEORY,
                                            "Practical : " + result.Data13!![i].PRACTICAL,
                                            "Clinical : " + result.Data13!![i].CLINICAL,
                                            "Theory Absent : " + result.Data13!![i].THEORY_ABSENT,
                                            "Practical Absent : " + result.Data13!![i].PRACTICAL_ABSENT,
                                            "Clinical Absent : " + result.Data13!![i].CLINICAL_ABSENT,
                                            "No Lecture : " + result.Data13!![i].NO_LECTURER,
                                            "Total Theory Percentage : " + TheoryPercentage+" %",
                                            "Total Practical Percentage : " + PracticalPercentage+" %",
                                            "Total Clinical Percentage : " + ClinicalPercentage+" %",
                                            "Total Percentage : " + fperatte.toString()+" %",
                                            R.drawable.attendance_thumb
                                        )
                                    )
                                }
                            }
                            progressBar.visibility = View.GONE
                            if(users.isEmpty()){
                                GenericUserFunction.showApiError(this@CurrentAttendance,"No Attendance found for the current request.")
                            }else {
                                val adapter = AttendanceAdapterCurrent(users)
                                recyclerView.adapter = adapter
                            }
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@CurrentAttendance, result.Status, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }}
        else
        {
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
    fun ConvertToPoint(percent:Double):Double{
        var fperatte: Double =
            String.format("%.2f", percent)
                .toDouble()
        if (fperatte.isNaN()) {
            fperatte = 0.0
        }
        return fperatte
    }

}
