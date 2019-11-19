package com.dmims.dmims.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import kotlinx.android.synthetic.main.student_notice_data.*


class Common_Image_Viewer : AppCompatActivity(), View.OnClickListener, LoadImageTask.Listener {
    var imgv: ImageView? = null
    var url: String? = null
    var probar:ProgressBar? = null
    var txt_titile:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_image_viewer_layout)
        imgv = findViewById<ImageView>(R.id.imgvid)

        probar = findViewById<ProgressBar>(R.id.progressBar7)
        probar!!.visibility = View.VISIBLE

        txt_titile=findViewById(R.id.txt_titile)
//        setSupportActionBar(toolbar1)
        try{


            url = intent.getStringExtra("url")
            LoadImageTask(this).execute(url)
            try {
                var actionTitle: String = intent.getStringExtra("actionTitle")
                txt_titile!!.setText(actionTitle)
            }catch (ex:java.lang.Exception)
            {
                txt_titile!!.setText("Image Viewer")
            }

        }catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }

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
