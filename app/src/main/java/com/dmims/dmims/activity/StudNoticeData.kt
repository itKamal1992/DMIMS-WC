package com.dmims.dmims.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.R
import kotlinx.android.synthetic.main.student_notice_data.*


class StudNoticeData : AppCompatActivity(), View.OnClickListener, LoadImageTask.Listener {
    var imgv: ImageView? = null
    var url: String? = null
    var probar:ProgressBar? = null
    var txt_titile:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_notice_data)
        imgv = findViewById<ImageView>(R.id.imgvid)
        url = intent.getStringExtra("urlimg")
        LoadImageTask(this).execute(url)
        probar = findViewById<ProgressBar>(R.id.progressBar7)
        probar!!.visibility = View.VISIBLE

        txt_titile=findViewById(R.id.txt_titile)
//        setSupportActionBar(toolbar1)
    }

    override fun onClick(v: View?) {
    }

    override fun onImageLoaded(bitmap: Bitmap) {
        probar!!.visibility = View.GONE
        imgv!!.setImageBitmap(bitmap)
    }
    override fun onError() {
        Toast.makeText(this, "Error Loading Image !", Toast.LENGTH_SHORT).show()
    }

}
