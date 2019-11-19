package com.dmims.dmims.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class ProgressiveAttendance : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.dmims.dmims.R.layout.activity_progressive_attendance)
        var admission_date = intent.getStringExtra("date_of_admiss_k2")
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormatter.parse(admission_date)
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        Toast.makeText(this, sdf.format(admission_date.toString()), Toast.LENGTH_SHORT).show()
    }
}
