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
import com.dmims.dmims.adapter.AttendanceAdapterCurrent
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.AttendanceStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.GetAcad_Start_Date
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Attendance : AppCompatActivity() {
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var search_id: Button? = null
    var btn_current_id: Button? = null
    var stud_k: Int = 0
    var date_of_admiss_k: String = "-"
    var current_date: String = "-"
    var COURSE_ID: String? = "-"
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var calsdb = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendance)
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        COURSE_ID = mypref.getString("course_id", null)

        stud_k = Integer.parseInt(intent.getStringExtra("stud_k"))
        date_of_admiss_k = intent.getStringExtra("date_of_admiss_k")
        to_date = findViewById<TextView>(R.id.select_to_date)
        from_date = findViewById<TextView>(R.id.select_from_date)
        search_id = findViewById<Button>(R.id.search_id)
        btn_current_id = findViewById<Button>(R.id.btn_currentattend)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        mServices = Common.getAPI()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calsdb.add(Calendar.DAY_OF_YEAR, -14) //Week 2 before attendance
        to_date!!.text = sdf.format(calsdb.time).toString()
        from_date!!.text = sdf.format(cal.time).toString()
        to_date_sel = sdf.format(cal.time)
        current_date = sdf.format(cal.time)
        from_date_sel = sdf.format(cal.time)
        val recyclerView = findViewById<RecyclerView>(R.id.attendance_list)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBar.visibility = View.VISIBLE
        if (InternetConnection.checkConnection(this)) {
//            val dialog: android.app.AlertDialog =
//                SpotsDialog.Builder().setContext(this).build()
//            dialog.setMessage("Please Wait!!! \nwhile we are getting your Attendance")
//            dialog.setCancelable(false)
//            dialog.show()
            try {


                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                    PhpApiInterface::class.java
                )
                var call3: Call<GetAcad_Start_Date> =
                    phpApiInterface.get_start_date()
                call3.enqueue(object : Callback<GetAcad_Start_Date> {
                    override fun onFailure(call: Call<GetAcad_Start_Date>, t: Throwable) {
//                        dialog.dismiss()
                        progressBar.visibility = View.GONE
                        GenericUserFunction.showOopsError(
                            this@Attendance,
                            t.message.toString().capitalize()
                        )
                    }

                    override fun onResponse(
                        call: Call<GetAcad_Start_Date>,
                        responsee: Response<GetAcad_Start_Date>
                    ) {

                        val results: GetAcad_Start_Date? = responsee.body()

                        var listSize = results!!.Data!!.size
                        if (listSize>0) {


                            to_date!!.text = results.Data!![0].ac_start_date

                                mServices.GetProgressiveAttend(
                                    stud_k,
                                    to_date!!.text.toString(),
                                    from_date_sel,
                                    COURSE_ID!!
                                )
                                    .enqueue(object : Callback<APIResponse> {
                                        override fun onFailure(
                                            call: Call<APIResponse>,
                                            t: Throwable
                                        ) {
//                                            dialog.dismiss()
                                            Toast.makeText(
                                                this@Attendance,
                                                t.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            progressBar.visibility = View.INVISIBLE
                                            progressBar.visibility = View.GONE
                                        }

                                        override fun onResponse(
                                            call: Call<APIResponse>,
                                            response: Response<APIResponse>
                                        ) {
//                                            dialog.dismiss()
                                            val result: APIResponse? = response.body()
                                            if (result!!.Status == "ok") {
                                                var listSize = result.Data13!!.size
                                                val users = ArrayList<AttendanceStudCurrent>()
                                                for (i in 0..listSize - 1) {
                                                    val per_daycount: Double =
                                                        ((result.Data13!![i].THEORY.toDouble() + result.Data13!![i].PRACTICAL.toDouble() + result.Data13!![i].CLINICAL.toDouble()) / (result.Data13!![i].NO_LECTURER.toDouble())) * 100.toFloat()
                                                    var fperatte: Double =
                                                        String.format("%.2f", per_daycount)
                                                            .toDouble()
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
                                                progressBar.visibility = View.INVISIBLE
                                                progressBar.visibility = View.GONE
                                                if (users.isEmpty()) {

                                                    GenericUserFunction.showApiError(
                                                        this@Attendance,
                                                        "No Attendance found for the current request."
                                                    )
                                                } else {
                                                    val adapter = AttendanceAdapterCurrent(users)
                                                    recyclerView.adapter = adapter
                                                }
                                            } else {
                                                progressBar.visibility = View.INVISIBLE
                                                progressBar.visibility = View.GONE
                                                Toast.makeText(
                                                    this@Attendance,
                                                    result.Status,
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }
                                    })

                        }


                    }
                }
                )


            } catch (ex: Exception) {
//                dialog.dismiss()
                progressBar.visibility = View.GONE
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
        btn_current_id!!.setOnClickListener {
            val intent = Intent(this@Attendance, CurrentAttendance::class.java)
            intent.putExtra("stud_k2", stud_k.toString())
            startActivity(intent)
        }
        search_id!!.setOnClickListener {
            validateDate()
            progressBar.visibility = View.VISIBLE
            if (InternetConnection.checkConnection(this)) {
                try {
                    mServices.GetProgressiveAttend(stud_k, to_date_sel, from_date_sel, COURSE_ID!!)
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                progressBar.visibility = View.INVISIBLE
                                progressBar.visibility = View.GONE
                                Toast.makeText(this@Attendance, t.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<APIResponse>,
                                response: Response<APIResponse>
                            ) {
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
                                                    "Total Clinical Percentage : " + ClinicalPercentage +" %",
                                                    "Total Percentage : " + fperatte.toString()+" %",
                                                    R.drawable.attendance_thumb
                                                )
                                            )
                                        }
                                    }

                                    progressBar.visibility = View.INVISIBLE
                                    progressBar.visibility = View.GONE
                                    if (users.isEmpty()) {
                                        GenericUserFunction.showApiError(
                                            this@Attendance,
                                            "No Attendance found for the current request."
                                        )
                                    } else {
                                        val adapter = AttendanceAdapterCurrent(users)
                                        recyclerView.adapter = adapter
                                    }
                                } else {
                                    progressBar.visibility = View.INVISIBLE
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this@Attendance,
                                        result.Status,
                                        Toast.LENGTH_SHORT
                                    ).show()
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
            } else {
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
                // Display Selected date in Toast
                println(view)
                println(year)
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
                // Display Selected date in Toast
                println(view)
                println(year)
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
