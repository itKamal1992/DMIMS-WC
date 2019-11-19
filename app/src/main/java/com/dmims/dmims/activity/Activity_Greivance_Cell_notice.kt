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
import com.dmims.dmims.R
import com.dmims.dmims.adapter.AttendanceAdapterCurrent
import com.dmims.dmims.adapter.NoticeAdapterCurrent
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.AttendanceStudCurrent
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.GetNotice
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Activity_Greivance_Cell_notice : AppCompatActivity() {
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var search_id: Button? = null
    var btn_current_id: Button? = null
    var stud_k: Int = 0
    var date_of_admiss_k: String = "-"
    var current_date: String = "-"
    var COURSE_ID: String = "-"
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_greivance_notice)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
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
                print(year)
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
}
