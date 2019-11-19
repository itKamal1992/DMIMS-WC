package com.dmims.dmims.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.remote.IMyAPI

class ViewGrievance : AppCompatActivity()
{
    lateinit var mServices: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_grievance)

        mServices = Common.getAPI()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_grievance)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)



    }
}
