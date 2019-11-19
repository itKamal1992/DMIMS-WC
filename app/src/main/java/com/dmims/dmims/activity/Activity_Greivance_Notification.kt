package com.dmims.dmims.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.adapter.GreivanceNotificationAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.GreivanceCellData
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Activity_Greivance_Notification : AppCompatActivity() {
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var search_id: Button? = null
    var btn_current_id: Button? = null
    //    var btn_progressive_id: Button? = null
    var stud_k: Int = 0
    var date_of_admiss_k: String = "-"
    //    var present_day: TextView? = null
//    var absent_day: TextView? = null
//    var percent_day: TextView? = null
//    var total_days: TextView? = null
    var current_date: String = "-"
    var COURSE_ID: String = "-"
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_greivance_notification)
        to_date = findViewById<TextView>(R.id.select_to_date)
        from_date = findViewById<TextView>(R.id.select_from_date)
        search_id = findViewById<Button>(R.id.search_id)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
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


        progressBar.visibility = View.VISIBLE
        try {
            mServices.GetRegisteredGreivance()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@Activity_Greivance_Notification, t.message, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
                        progressBar.visibility = View.GONE

                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Status == "ok") {
                            var listSize = result.Data12!!.size
                            val users = ArrayList<GreivanceCellData>()
                            for (i in 0..listSize - 1) {

                                users.add(
                                    GreivanceCellData(
                                        result.Data12!![i].ID,
                                        result.Data12!![i].INFORMATION,
                                        result.Data12!![i].NAME_GRIE,
                                        result.Data12!![i].PHONE_NO,
                                        result.Data12!![i].EMAIL_ID,
                                        result.Data12!![i].ADDRESS,
                                        result.Data12!![i].PLACE_WORK,
                                        result.Data12!![i].JOB_TITLE,
                                        result.Data12!![i].TYPE_GRIE,
                                        result.Data12!![i].DT_TIME_PLACE,
                                        result.Data12!![i].DETAIL_DESC,
                                        result.Data12!![i].OTHER_INFO,
                                        result.Data12!![i].PRO_SOL_GRIE,
                                        result.Data12!![i].OTHER_DETAILS,
                                        result.Data12!![i].INSERT_DATE,
                                        result.Data12!![i].COURSE_NAME,
                                        result.Data12!![i].COURSE_ID,
                                        result.Data12!![i].COURSE_INSTITUTE,
                                        result.Data12!![i].STUDENT_ID,
                                        R.drawable.ic_attendence
                                    )
                                )
                            }
                            progressBar.visibility = View.INVISIBLE
                            progressBar.visibility = View.GONE
                            val adapter = GreivanceNotificationAdapter(users)
                            recyclerView.adapter = adapter
                        } else {
                            progressBar.visibility = View.INVISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@Activity_Greivance_Notification, result.Status, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
    }

}
