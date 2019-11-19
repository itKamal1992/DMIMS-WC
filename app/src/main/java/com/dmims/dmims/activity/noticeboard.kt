package com.dmims.dmims.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.remote.IMyAPI
import kotlinx.android.synthetic.main.activity_noticeboard.*
import kotlinx.android.synthetic.main.faculty_dashboard.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


@Suppress("DEPRECATION")
class noticeboard : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private var READ_REQUEST_CODE = 300
    private var SERVER_PATH = "http://192.168.1.233/API_PUBLISH/UploadImage/"
    private var uri: Uri? = null
    private var fileToUpload: MultipartBody.Part? = null
    private lateinit var mServices: IMyAPI
    private var TAG = noticeboard::class.java.simpleName
    var noticetype = arrayOf("Administrative", "General")
    var sel_notice_type = ""
    var btnPickImage: Button? = null
    var btnPubNotice: Button? = null
    //var btnNoticeDate:Button?=null
    var editNoticeDate: TextView? = null
    var edit_notice_title: TextView? = null
    var edit_notice_desc: TextView? = null
    var RESOU_FLAG = "F"
    var cal = Calendar.getInstance()

    private val REQUEST_GALLERY_CODE = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticeboard)
        btnPickImage = findViewById<Button>(R.id.notice_upload) as Button
        btnPubNotice = findViewById<Button>(R.id.btn_publish_notice) as Button
        editNoticeDate = findViewById(R.id.select_date)
        edit_notice_title = findViewById<EditText>(R.id.edit_notice_title)
        edit_notice_desc = findViewById<EditText>(R.id.notice_descr)
        spinner_sample.adapter = ArrayAdapter(this, R.layout.my_spinner, noticetype)
        spinner_sample.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sel_notice_type = noticetype[p2]
            }
        }


        mServices = Common.getAPI()
        editNoticeDate!!.setOnClickListener {
            selectDate()
        }

        btnPickImage!!.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), REQUEST_GALLERY_CODE)
        }

        btnPubNotice!!.setOnClickListener {
            sendNotice()
        }

    }

    private fun selectDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                editNoticeDate!!.text = "$dayOfMonth/$monthOfYear/$year"
            },
            year,
            month + 1,
            day
        )
        dpd.show()
    }


    private fun sendNotice() {
        if (editNoticeDate!!.text.toString().isEmpty()) {
            editNoticeDate!!.error = "Please select notice date"
            return
        } else {
            var DATE = editNoticeDate!!.text.toString()
        }
        if (edit_notice_title!!.text.toString().isEmpty()) {
            edit_notice_title!!.error = "Please input notice board title"
            return
        } else {
            var notice_title = edit_notice_title!!.text.toString()
        }
        if (edit_notice_desc!!.text.toString().isEmpty()) {
            edit_notice_desc!!.error = "Please input notice board description"
            return
        } else {
            var notice_desc = edit_notice_desc!!.text.toString()
        }
        if (enroll_no!!.text.toString().isEmpty()) {
            edit_notice_desc!!.error = "Please relogin again"
            return
        } else {
            var faculty_id = enroll_no!!.text.toString()
        }



        if (RESOU_FLAG == "T") {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {

        val result: String?
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(contentURI, filePathColumn, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(filePathColumn[0])
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            uri = data!!.data
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                var filePath: String = getRealPathFromURI(uri!!).toString()
                var file: File = File(filePath)
                Log.d(TAG, "Filename " + file.name.toString())
                var mFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
                var filename: RequestBody = RequestBody.create(MediaType.parse("text/plain"), file.name)
                Toast.makeText(this@noticeboard, "File title" + filename + "uploded sucessfully", Toast.LENGTH_LONG)
                    .show()
                RESOU_FLAG = "T"
            } else {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.read_file),
                    READ_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        Toast.makeText(this@noticeboard, "Permission has been denied", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        if (uri != null) {
            val filePath = getRealPathFromURIPath(uri!!, this@noticeboard)
            val file = File(filePath)
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val filename = RequestBody.create(MediaType.parse("text/plain"), file.name)
            RESOU_FLAG = "T"
            Toast.makeText(this@noticeboard, "Permission to media access granted sucessfully", Toast.LENGTH_LONG).show()
            Toast.makeText(this@noticeboard, "File title" + filename + "uploded sucessfully", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String? {
        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            return contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }


}
