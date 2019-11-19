package com.dmims.dmims.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.dmims.dmims.R
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_academic__calender.*

class FacultyAcademicCalender : AppCompatActivity() {
    var btnAcademicCalender: Button? = null
    var pdf: PDFView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.faculty_content_academic__calender)
        setSupportActionBar(toolbar)
        btnAcademicCalender = findViewById<Button>(R.id.btn_academic_calender)
        pdf = findViewById<PDFView>(R.id.pdfid)
        btnAcademicCalender!!.setOnClickListener {
            btnAcademicCalender!!.visibility = View.INVISIBLE
            pdf!!.fromAsset("CAAC_2018_19.pdf").load()
        }
    }

}
