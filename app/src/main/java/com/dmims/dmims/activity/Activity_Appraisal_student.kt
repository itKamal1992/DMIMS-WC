package com.dmims.dmims.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.dmims.dmims.R

import kotlinx.android.synthetic.main.activity_notification__admin.*

class Activity_Appraisal_student : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appraisal_student)
        setSupportActionBar(toolbar)
    }

}
