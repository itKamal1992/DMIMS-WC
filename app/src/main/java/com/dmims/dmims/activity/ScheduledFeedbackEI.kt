package com.dmims.dmims.activity


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.dmims.dmims.R
import com.dmims.dmims.adapter.EmergencyContactAdapter
import com.dmims.dmims.adapter.ScheduledFeedbackAdapter
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.ListScheduledFeedback
import com.dmims.dmims.model.FeedBackSchedule
import com.dmims.dmims.model.FeedBackScheduleField
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduledFeedbackEI : AppCompatActivity()
{
    lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var current_date: String = "-"
    var feedbacknames = ArrayList<String>()
    var feedbacdsates = ArrayList<String>()
    var feedbacedates = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_feedback_ei)
        mServices = Common.getAPI()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_schedledfeedback)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBar.visibility = View.VISIBLE

        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
        var submitdate: Call<FeedBackSchedule> = phpApiInterface.CurrentDateSubmit(current_date)
        submitdate.enqueue(object :Callback<FeedBackSchedule>{
            override fun onFailure(call: Call<FeedBackSchedule>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<FeedBackSchedule>, response: Response<FeedBackSchedule>)
            {
                val result: List<FeedBackScheduleField>? = response.body()!!.Data
                println("Response1 >> "+result!![0].id)
                if (result!![0].id == "error") {
                    Toast.makeText(
                        this@ScheduledFeedbackEI,
                        "No Data in faculty Master.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else
                {
                    val userdata= ArrayList<ListScheduledFeedback>()
                    var listSize = result!!.size
                    for (i in 0..listSize - 1)
                    {
                        userdata.add(
                            ListScheduledFeedback(
                                result!![i].FEEDBACK_NAME,
                                result!![i].SCHEDULE_DATE,
                                result!![i].START_DATE,
                                result!![i].END_DATE
                            )
                        )
                    }
                    val adapter = ScheduledFeedbackAdapter(userdata)
                    recyclerView.adapter = adapter
                }
            }
        })

    }
}
