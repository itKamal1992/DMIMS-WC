package com.dmims.dmims

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.dmims.dmims.activity.AdminNoticeBoard
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.NoticeStudCurrent
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ImageUpload : AppCompatActivity(), View.OnClickListener {
    private lateinit var img_title: TextView
    private lateinit var btn_choose: Button
    private lateinit var btn_upload: Button
    private lateinit var img: ImageView
    private val IMG_REQUEST = 777
    private lateinit var bitmap: Bitmap
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var current_date: String = "-"
    var current_time: String = "-"
    private var max_id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_image_upload)
        img = findViewById<ImageView>(R.id.imageView)
        btn_upload = findViewById<Button>(R.id.btn_uploadimg)
        img_title = findViewById<TextView>(R.id.img_tilte)
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)
        mServices = Common.getAPI()
        btn_choose.setOnClickListener(this)
        btn_upload.setOnClickListener(this)

        btn_upload.visibility=View.GONE

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btn_chooseimg -> {
                selectImage()
                //return
            }
            R.id.btn_uploadimg -> {
                uploadImage()

            }
        }
    }

    fun uploadImage() {
        var Image: String = imageToString()
        var Title: String = img_title.text.toString()
        var sendIntent = Intent(applicationContext, AdminNoticeBoard::class.java)
        sendIntent.putExtra("notice_content", Title)
        sendIntent.putExtra("notice_image", Image)
        setResult(Activity.RESULT_OK, sendIntent)
        super.onBackPressed()


//        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
//        var call: Call<ImageClass> = phpApiInterface.uploadImage(Title, Image)
//        call.enqueue(object : Callback<ImageClass> {
//            override fun onFailure(call: Call<ImageClass>, t: Throwable) {
//                Toast.makeText(this@ImageUpload, "Server Response" + t.message, Toast.LENGTH_SHORT)
//            }
//            override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
//                var imageClass: ImageClass? = response!!.body()
//               img_title.setText(imageClass!!.getResponse())
//            }
//
//        })
    }

    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMG_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            var path: Uri? = data.data
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, path)
            img.setImageBitmap(bitmap)
            noticeTitle()

            img.visibility = View.VISIBLE
            img_title.visibility = View.VISIBLE
            btn_choose.isEnabled = false
            btn_upload.isEnabled = true
            btn_upload.visibility=View.VISIBLE
            btn_choose.visibility=View.GONE

        }
    }

    fun imageToString(): String {
        val byteArrayOutputString = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputString)
        val imageBytes = byteArrayOutputString.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun noticeTitle(): Unit {
        try {
            mServices.GetNotice("01-08-2019",current_date)
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@ImageUpload, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Status == "ok") {
                            var listSize = result.Data14!!.size
                            var list = ArrayList<Int>()
                            list.clear()
                            for (i in 0..listSize - 1) {
                                list.add(result.Data14!![i].ID.toInt())
                            }

                            img_title.text = "Notice_No_"+(list.max()!!+1).toString()+"_"+current_date

                        }
                    }
                })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
