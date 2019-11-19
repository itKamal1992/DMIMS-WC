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
        try {
            mServices.GetProgressiveAttend(stud_k, to_date_sel, from_date_sel, COURSE_ID!!)
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@Attendance, t.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
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
                                var fperatte: Double = String.format("%.2f", per_daycount).toDouble()
                                if (fperatte.isNaN()) {
                                    fperatte = 0.0
                                }
                                if(fperatte != 0.0) {
                                    users.add(
                                        AttendanceStudCurrent(
                                            "DEPT NAME: " + result.Data13!![i].DEPT_NAME,
                                            "DEPT ID: " + result.Data13!![i].DEPT_ID,
                                            "THEORY: " + result.Data13!![i].THEORY,
                                            "PRACTICAL: " + result.Data13!![i].PRACTICAL,
                                            "CLINICAL: " + result.Data13!![i].CLINICAL,
                                            "THEORY ABSENT: " + result.Data13!![i].THEORY_ABSENT,
                                            "PRACTICAL ABSENT: " + result.Data13!![i].PRACTICAL_ABSENT,
                                            "CLINICAL ABSENT: " + result.Data13!![i].CLINICAL_ABSENT,
                                            "NO LECTURE: " + result.Data13!![i].NO_LECTURER,
                                            "PERCENTAGE: " + fperatte.toString(),
                                            R.drawable.ic_attendence
                                        )
                                    )
                                }
                            }
                            progressBar.visibility = View.INVISIBLE
                            progressBar.visibility = View.GONE
                            if(users.isEmpty()){
                                    GenericUserFunction.showApiError(this@Attendance,"No Attendance found for the current request.")
                            }else {
                                val adapter = AttendanceAdapterCurrent(users)
                                recyclerView.adapter = adapter
                            }
                        } else {
                            progressBar.visibility = View.INVISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@Attendance, result.Status, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
        btn_current_id!!.setOnClickListener {
            val intent = Intent(this@Attendance, CurrentAttendance::class.java)
            intent.putExtra("stud_k2", stud_k.toString())
            startActivity(intent)
        }
        search_id!!.setOnClickListener {
            validateDate()
            progressBar.visibility = View.VISIBLE
            try {
                mServices.GetProgressiveAttend(stud_k, to_date_sel, from_date_sel, COURSE_ID!!)
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            progressBar.visibility = View.INVISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@Attendance, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            val result: APIResponse? = response.body()
                            if (result!!.Status == "ok") {
                                var listSize = result.Data13!!.size
                                val users = ArrayList<AttendanceStudCurrent>()
                                for (i in 0..listSize - 1) {
                                    val per_daycount: Double =
                                        ((result.Data13!![i].THEORY.toDouble() + result.Data13!![i].PRACTICAL.toDouble() + result.Data13!![i].CLINICAL.toDouble()) / (result.Data13!![i].NO_LECTURER.toDouble())) * 100.toFloat()
                                    var fperatte: Double = String.format("%.2f", per_daycount).toDouble()
                                    if (fperatte.isNaN()) {
                                        fperatte = 0.0
                                    }
                                    if(fperatte != 0.0) {
                                        users.add(
                                            AttendanceStudCurrent(
                                                "DEPT NAME: " + result.Data13!![i].DEPT_NAME,
                                                "DEPT ID: " + result.Data13!![i].DEPT_ID,
                                                "THEORY: " + result.Data13!![i].THEORY,
                                                "PRACTICAL: " + result.Data13!![i].PRACTICAL,
                                                "CLINICAL: " + result.Data13!![i].CLINICAL,
                                                "THEORY ABSENT: " + result.Data13!![i].THEORY_ABSENT,
                                                "PRACTICAL ABSENT: " + result.Data13!![i].PRACTICAL_ABSENT,
                                                "CLINICAL ABSENT: " + result.Data13!![i].CLINICAL_ABSENT,
                                                "NO LECTURE: " + result.Data13!![i].NO_LECTURER,
                                                "PERCENTAGE: " + fperatte.toString(),
                                                R.drawable.ic_attendence
                                            )
                                        )
                                    }
                                }

                                progressBar.visibility = View.INVISIBLE
                                progressBar.visibility = View.GONE
                                if(users.isEmpty()){
                                    GenericUserFunction.showApiError(this@Attendance,"No Attendance found for the current request.")
                                }else {
                                    val adapter = AttendanceAdapterCurrent(users)
                                    recyclerView.adapter = adapter
                                }
                            } else {
                                progressBar.visibility = View.INVISIBLE
                                progressBar.visibility = View.GONE
                                Toast.makeText(this@Attendance, result.Status, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
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
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                println(view)
                println(year)
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                from_date!!.text = sdf.format(date).toString()
            }, year, month, day
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
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                println(view)
                println(year)
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                to_date!!.text = sdf.format(date).toString()
            }, year, month, day
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
