package com.dmims.dmims.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.model.*
import com.dmims.dmims.remote.Api
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_greivance_stud_file.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class GreivanceStudFile : AppCompatActivity() {
    private var mediaPath: String? = null
    private var drawerTitler: String = ""
    lateinit var fileUrl: String
    var dialogCommon: android.app.AlertDialog? = null

    lateinit var et_NameGriev: EditText
    lateinit var et_SubOfComplaintGriev: EditText
    lateinit var et_ComplaintAgainstDetailGriev: EditText
    lateinit var et_DetailDescriGriev: EditText
    lateinit var et_DateGriev: EditText

    lateinit var spinner_CategoryGriev: Spinner
    lateinit var spinner_Name: Spinner
    lateinit var spinner_ComplaintToGriev: Spinner

    lateinit var btnPickerFile: Button

    var cal = Calendar.getInstance()
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var current_date: String = "-"
    private lateinit var mServices: IMyAPI


    private var selectedImage: Uri? = null
    private var confirmStatus = "F"
    var filename: String = "-"

    lateinit var STUD_ID: String
    lateinit var course_id: String
    lateinit var roll_no: String
    var instituteName: String = ""
    // lateinit var course_id:String


    lateinit var btnViewGriev: Button

    var listsinstz: Int = 0
    var instituteName1 = ArrayList<String>()
    var instituteData = ArrayList<String>()
    var hodName = ArrayList<String>()
    var deptName = ArrayList<String>()
    var DeanPrinciName = ArrayList<String>()
    var PrincipalDeanClg = ArrayList<String>()
    var ASSING_ID = ArrayList<String>()
    var REQUEST_CODE: Int = 0
    var type: String? = null
    private var uri: Uri? = null
    var PdfPathHolder: String? = null
    private var PdfID: String? = null
    private var random: Int? = null




    var Grev_Filename: String = ""
    var G_TICKETNO: String = ""
    var G_ATTACHMENT: String = ""
    var G_STATUS: String = ""
    var U_ID: String = ""
    var ASSING_TO_ID: String = ""
    var REMINDER: String = ""
    var Status: String = ""
    var G_Department: String = ""
    var G_SUBJECT: String = ""
    var G_CATEGORY: String = ""
    var G_AGAINST: String = ""
    var G_DISCRIPTION: String = ""
    var selected_Department: String = ""
    var str_NameGriev: String = ""
    var str_CollegeNameGrievGriev: String = ""
    var str_ComplaintToGriev: String = ""
    var str_DateGriev: String = ""


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greivance_stud_file)
        dialogCommon = SpotsDialog.Builder().setContext(this).build()

        et_NameGriev = findViewById(R.id.et_namegrivienceStud)

        et_SubOfComplaintGriev = findViewById(R.id.et_subComplaintGrive)
        spinner_CategoryGriev = findViewById(R.id.spinner_studGriveCategory)
        et_ComplaintAgainstDetailGriev = findViewById(R.id.et_DetailsComplaintGrive)
        et_DetailDescriGriev = findViewById(R.id.et_DetailsDescriGrive)
        spinner_Name = findViewById(R.id.spinner_Name)
        spinner_ComplaintToGriev = findViewById(R.id.spinner_ComplaintToGriev)
        et_DateGriev = findViewById(R.id.et_dateGriev)
        btnPickerFile = findViewById(R.id.admin_griev_upload)

        btnViewGriev = findViewById(R.id.btn_ViewSubmitGriev)
        btnViewGriev.setOnClickListener {

            val intent = Intent(this@GreivanceStudFile, StudentSubmittedGrievance::class.java)
            startActivity(intent)
        }

        instituteName1.add("Select ")

        btnPickerFile.setOnClickListener {
            //            val intent = Intent(applicationContext, ImageUpload::class.java)
//            startActivityForResult(intent, 1)
            var CustDialog = Dialog(this)
            CustDialog.setContentView(R.layout.dialog_select_uploadtype_custom_popup)
            var ivNegClose1: ImageView = CustDialog.findViewById(R.id.cross_image) as ImageView
            var btnCamera: ImageButton = CustDialog.findViewById(R.id.btnCamera) as ImageButton
            var btnGallary: ImageButton = CustDialog.findViewById(R.id.btnGallary) as ImageButton
            var btnpdf: ImageButton = CustDialog.findViewById(R.id.btnPdf) as ImageButton

//    GenericPublicVariable.CustDialog.setCancelable(false)
            ivNegClose1.setOnClickListener {
                CustDialog.dismiss()

            }
            btnCamera.setOnClickListener {
                CustDialog.dismiss()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, 100)
                }
            }
            btnGallary.setOnClickListener {
                CustDialog.dismiss()
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                galleryIntent.type = "*/*"
                startActivityForResult(galleryIntent, 400)
            }
            btnpdf.setOnClickListener {
                CustDialog.dismiss()
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                galleryIntent.type = "*/*"
                startActivityForResult(galleryIntent, 300)
            }
//            CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            CustDialog.show()
        }

        //  checkFileFlag()


//            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            println("path >> " + i.data)
//            startActivityForResult(i, 100)


        var dateCurrent: Date
        mServices = Common.getAPI()

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)






        current_date = sdf.format(cal.time)
        from_date_sel = sdf.format(cal.time)

        println("current_date $current_date")
        et_DateGriev.setHint(current_date)
        et_DateGriev.isEnabled = false


        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)


        STUD_ID = mypref.getString("Stud_id_key", null)!!
        course_id = mypref.getString("course_id", null)!!
        roll_no = mypref.getString("roll_no", null)!!
        drawerTitler = mypref.getString("key_drawer_title", null)!!
        instituteName = mypref.getString("key_institute_stud", null)!!
        et_NameGriev.setText(drawerTitler)

        et_NameGriev.isEnabled=false

            btn_submit_grievance.setOnClickListener {
            if (et_NameGriev.text.toString().equals("")) {
                et_NameGriev.setError("Please insert your name")
            } else if (et_SubOfComplaintGriev.text.toString().equals("")) {
                et_SubOfComplaintGriev.setError("Please insert your concern subject")
            } else if (spinner_CategoryGriev.selectedItem.toString().equals("--Select Grievance Category--")) {
                Toast.makeText(this, "Select Grievance Category", Toast.LENGTH_LONG).show()

            } else if (et_ComplaintAgainstDetailGriev.text.toString().equals("")) {
                et_ComplaintAgainstDetailGriev.setError("Plese fill information")
                et_ComplaintAgainstDetailGriev?.isFocusable = true
            } else if (et_DetailDescriGriev.text.toString().equals("")) {
                et_DetailDescriGriev.setError("Please fill information")
            } else if (spinner_ComplaintToGriev.selectedItem.toString().equals("--Select Complaint To--")) {
                Toast.makeText(this, "Select Complaint To", Toast.LENGTH_LONG).show()
            } else if ((spinner_Name.selectedItem.toString().equals("Select")) || (spinner_Name.selectedItem == null)) {
                Toast.makeText(this, "Select", Toast.LENGTH_LONG).show()
            } else {

                G_SUBJECT = et_SubOfComplaintGriev.text.toString()
                G_CATEGORY = spinner_CategoryGriev.selectedItem.toString()
                G_AGAINST = et_ComplaintAgainstDetailGriev.text.toString()
                G_DISCRIPTION = et_DetailDescriGriev.text.toString()
                str_NameGriev = et_NameGriev.text.toString()

                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView


                tvMsg.text = "Are you sure, You want to register this grievance?"
                btnOk.setOnClickListener {
                    CustDialog.dismiss()
//                    uploadFunction()
                    if (confirmStatus == "T" && type == "pdf" && mediaPath!=null)
                     {
//                        uploadFile()
                         uploadFileImg()
                    }
                    if (confirmStatus == "T" && type == "image" && mediaPath!=null) {
                        uploadFileImg()
                }else  if (confirmStatus == "F")
                    {
                        var CustDialog = Dialog(this)
                        CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
                        var ivNegClose1: ImageView =
                            CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                        var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                        var btnCustomDialogCancel: Button =
                            CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                        var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView


                        tvMsg.text = "Are you sure, You want to submit this grievance without Attachment?"
//    GenericPublicVariable.CustDialog.setCancelable(false)
                        btnOk.setOnClickListener {
                            CustDialog.dismiss()
                            noAttachGrievanceFunction()

                        }
                        btnCustomDialogCancel.setOnClickListener {
                            CustDialog.dismiss()
                        }
                        ivNegClose1.setOnClickListener {
                            CustDialog.dismiss()

                        }
                        CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        CustDialog.show()

                    }
                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()


                println(
                    "G_SUBJECT >> " + et_SubOfComplaintGriev.text.toString()
                            + "\n" + "G_CATEGORY >> " + spinner_CategoryGriev.selectedItem.toString()
                            + "\n" + "G_AGAINST >> " + et_ComplaintAgainstDetailGriev.text.toString()
                            + "\n" + "G_DISCRIPTION >> " + et_DetailDescriGriev.text.toString()
                            + "\n" + "G_DATE >> " + current_date
                            + "\n" + "G_ATTACHMENT " + confirmStatus
                            + "\n" + "G_STATUS >> Open"
                            + "\n" + "ASSING_TO_ID >> " + ASSING_TO_ID
                            + "\n" + "REMINDER >> REMINDER not in use"
                            + "\n" + "STUD_ID >> " + STUD_ID
                            + "\n" + "course_id >> " + course_id
                            + "\n" + "roll_no >> " + roll_no
                            + "\n" + "Grev_name >> " + et_NameGriev.text.toString()
                            + "\n" + "Inst_Name >> " + instituteName
                            + "\n" + "Comp_To >> " + str_ComplaintToGriev

                            + "\n" + "Grev_Filename >> " + filename
                            + "\n" + "str_Department >> " + selected_Department
                            + "\n" + "ATTACHMENT_url >> Get from Multipart"


                )

            }
        }

        spinner_ComplaintToGriev.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    instituteName1.clear()
                    getGrievanceData()

                }
            }


    }

    fun pickImage() {
        REQUEST_CODE = 100
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        println("path >> " + i.data)
        startActivityForResult(i, 101)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                rImage = data!!.getStringExtra("notice_image")
//                rTitle = data.getStringExtra("notice_content")
//                RESOU_FLAG = "T"
//                btnPickImage.text = "Notice Uploaded Successfully"
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this@AdminNoticeBoard, "Nothing Selected", Toast.LENGTH_SHORT)
//            }
//        }

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            type = "image"
            //the image URI
            selectedImage = data.getData()
            var bitmap: Bitmap? = getThumbnail(selectedImage!!)
            //calling the upload file method after choosing the file
            if (selectedImage != null) {
                println("Selected Image >>> " + selectedImage)
                var CustDialog2 = Dialog(this)
                CustDialog2.setContentView(R.layout.dialog_image_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    CustDialog2.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog2.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button =
                    CustDialog2.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog2.findViewById(R.id.tvMsgCustomDialog) as TextView
                var image: ImageView = CustDialog2.findViewById(R.id.dialog_image) as ImageView
                image.setImageBitmap(bitmap)

                tvMsg.text = "Do you want to Submit Selected Image?"
                //    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    CustDialog2.dismiss()
                    confirmStatus = "T"

                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog2.dismiss()
                    confirmStatus = "F"
                }
                ivNegClose1.setOnClickListener {
                    CustDialog2.dismiss()
                    confirmStatus = "F"
                }
                CustDialog2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog2.show()

            }
        }

        else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            var bitmap: Bitmap = data.getExtras()!!.get("data") as Bitmap
            type = "image"
//        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            var tempUri: Uri? = getImageUri(getApplicationContext(), bitmap)
            selectedImage = tempUri
            var bitmaps: Bitmap? = getThumbnail(tempUri!!)
//        // CALL THIS METHOD TO GET THE ACTUAL PATH
//            var finalFile: File = File(getRealPathFromURI(tempUri!!))
//            println("finalFile >>>> " + finalFile.toURI())


            if (selectedImage != null) {
                println("Selected Image >>> " + selectedImage)
                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_image_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView =
                    CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                var image: ImageView = CustDialog.findViewById(R.id.dialog_image) as ImageView
                image.setImageBitmap(bitmaps)

                tvMsg.text = "Do you want to Submit Selected Image?"
                //    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "T"
//                    finish()
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
                    cursor.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    println("mediaPath >>> $mediaPath")
                    //str1.setText(mediaPath)
                    // Set the Image in ImageView for Previewing the Media
                    //imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()
                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()
//
            }
        }
        else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            type = "pdf"
            uri = data!!.data
            if (uri.toString().isNotEmpty()) {
                confirmStatus = "T"

            } else {
                confirmStatus = "F"
            }
//            println("file uri here " + fileUri)
//            extras = data?.extras!!

        }
        else if (requestCode == 300 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            type = "pdf"

            if (selectedImage.toString().isNotEmpty()) {
                confirmStatus = "T"

            } else {
                confirmStatus = "F"
            }
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            println("mediaPath >>> $mediaPath")
            //str1.setText(mediaPath)
            // Set the Image in ImageView for Previewing the Media
            //imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
            cursor.close()

        }
        else if (requestCode == 400 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            var bitmaps :Bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            if (selectedImage != null) {
                println("Selected Image >>> " + selectedImage)
                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_image_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView =
                    CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                var image: ImageView = CustDialog.findViewById(R.id.dialog_image) as ImageView
                image.setImageBitmap(bitmaps)

                tvMsg.text = "Do you want to Submit Selected Image?"
                //    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    CustDialog.dismiss()
                    type = "image"

                    if (selectedImage.toString().isNotEmpty()) {
                        confirmStatus = "T"

                    } else {
                        confirmStatus = "F"
                    }
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
                    cursor.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    println("mediaPath >>> $mediaPath")
                    //str1.setText(mediaPath)
                    // Set the Image in ImageView for Previewing the Media
                    //imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()
                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                    confirmStatus = "F"
                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()
//
            }



        }
    }

    private fun noAttachGrievanceFunction() {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are sending your notice")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()
        try {
            if (InternetConnection.checkConnection(this)) {



                mServices.InsertStudentGrievance(
                    G_SUBJECT,
                    G_CATEGORY,
                    G_AGAINST,
                    G_DISCRIPTION,
                    current_date,
                    "F",
                    "Open",
                    ASSING_TO_ID.toInt(),
                    "-",
                    STUD_ID.toInt(),
                    course_id,
                    roll_no,
                    str_NameGriev,
                    instituteName,
                    str_ComplaintToGriev,
                    "-",
                    selected_Department,
                    "-",
                    "-"
                ).enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dialogCommon!!.dismiss()
                        Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        dialogCommon!!.dismiss()
//                        Toast.makeText(this@GreivanceStudFile, result!!.Status, Toast.LENGTH_SHORT)
//                            .show()
                        if (result!!.Responsecode == 200) {
                            GenericPublicVariable.CustDialog = Dialog(this@GreivanceStudFile)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                            var ivPosClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                            var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                            var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                            tvMsg.text = "Grievance Registered Successfully"
                            GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GreivanceStudFile)

                            }
                            ivPosClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GreivanceStudFile)

                            }

                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                            GenericPublicVariable.CustDialog.show()
                        } else {
                            GenericUserFunction.showApiError(
                                this@GreivanceStudFile, "Sorry for inconvenience\n" +
                                        "Server seems to be busy,\n" +
                                        "Please try after some time."
                            )
                        }
                    }
                })
            } else {
                dialogCommon!!.dismiss()
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr)
                )
            }
        } catch (ex: java.lang.Exception) {
            dialogCommon!!.dismiss()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }

    companion object {
        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
//        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/pdfupload/pdfnoticeupload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/imageupload/grieupload.php"
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        var byte: ByteArrayOutputStream = ByteArrayOutputStream(100000)
        inImage.compress(Bitmap.CompressFormat.PNG, 100, byte)
        var path: String =
            MediaStore.Images.Media.insertImage(inContext!!.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getGrievanceData() {
        str_ComplaintToGriev = spinner_ComplaintToGriev.selectedItem.toString()

        if (str_ComplaintToGriev.equals("--Select Complaint To--")) {

        } else {
            if (str_ComplaintToGriev.equals("Principal/Dean")) {
                if (instituteName == "SRMMCON" || instituteName == "RNPC") {
                    str_ComplaintToGriev = "Principal"

                } else {
                    str_ComplaintToGriev = "Dean"
                }
                GetDepartmentRole()
            } else if (str_ComplaintToGriev.equals("HOD")) {
                str_ComplaintToGriev = "HOD"
                GetDepartmentRole()
            }
            else if (str_ComplaintToGriev.equals("Convener")) {
                instituteName1.add("Grievance Cell Admin")

                var DepartmentAdap: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@GreivanceStudFile,
                    R.layout.support_simple_spinner_dropdown_item, instituteName1
                )
                spinner_Name!!.adapter = DepartmentAdap
                ASSING_TO_ID="0"
                str_ComplaintToGriev="GREVIANCE_CELL"
                selected_Department= "Grievance Cell Admin"


            }
        }
    }

    private fun GetDepartmentRole() {
        mServices.GetGreivanceData(instituteName, str_ComplaintToGriev)
            .enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    GenericUserFunction.showNegativePopUp(
                        this@GreivanceStudFile,
                        "Please wait sever is busy"
                    )
                }


                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    instituteName1.add("Select")
                    val result: APIResponse? = response.body()

                    listsinstz = result?.Data18!!.size



                    ASSING_ID.add("0")
                    if (str_ComplaintToGriev.equals("HOD")) {
                        for (i in 0..listsinstz - 1) {

                            if (result.Data18!![i].MNAME.isEmpty() && result.Data18!![i].LNAME.isEmpty()) {
                                hodName!!.add(result.Data18!![i].FNAME)
                                deptName!!.add(result.Data18!![i].DEPNAM01)
                                instituteName1.add(result.Data18!![i].FNAME + "(" + result.Data18!![i].DEPNAM01 + ")")

                            } else {

                                hodName!!.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME)
                                deptName!!.add(result.Data18!![i].DEPNAM01)
                                instituteName1.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME + "(" + result.Data18!![i].DEPNAM01 + ")")
                            }

                            ASSING_ID.add(result.Data18!![i].UFMID)
                        }

                    } else if (str_ComplaintToGriev.equals("Principal") || str_ComplaintToGriev.equals(
                            "Dean"
                        )
                    ) {
                        for (i in 0..listsinstz - 1) {
                            if (result.Data18!![i].MNAME.isEmpty() && result.Data18!![i].LNAME.isEmpty()) {
                                DeanPrinciName!!.add(result.Data18!![i].FNAME)
                                PrincipalDeanClg!!.add(result.Data18!![i].DEPNAM01)
                                deptName!!.add(result.Data18!![i].INST_NAME)
                                instituteName1.add(result.Data18!![i].FNAME + "(" + result.Data18!![i].INST_NAME + ")")


                            } else {
                                DeanPrinciName!!.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME)
                                PrincipalDeanClg!!.add(result.Data18!![i].DEPNAM01)
                                deptName!!.add(result.Data18!![i].INST_NAME)
                                instituteName1.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME + "(" + result.Data18!![i].INST_NAME + ")")

                            }
                            ASSING_ID.add(result.Data18!![i].UFMID)
                        }
                    }
                    println("result  " + result?.Data18!![0].FNAME + result?.Data18!![0].MNAME + result?.Data18!![0].LNAME)
                    var DepartmentAdap: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@GreivanceStudFile,
                        R.layout.support_simple_spinner_dropdown_item, instituteName1
                    )
                    /* var DepartmentAdap2: ArrayAdapter<String> = ArrayAdapter<String>(
                         this@GreivanceStudFile,
                         R.layout.support_simple_spinner_dropdown_item, instituteData
                     )*/


                    spinner_Name!!.adapter = DepartmentAdap



                    spinner_Name.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                var position1 = spinner_Name.selectedItemPosition
                                println("positipon 1 " + position1)
                                if (position1 == 0) {

                                } else {

                                    println("position code " + ASSING_ID[position])
                                    ASSING_TO_ID = ASSING_ID[position]
                                    selected_Department = deptName[position - 1]

//                                    println("selected_Department code " + deptName[position])
                                }


                            }
                        }



                    println(" Data found")


                }

            })


//spinner_Name!!.adapter = DepartmentAdap2


    }

    private fun DialogWithoutFile() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_withoutfilenotice, null)
        // val txtviewlbl = dialogView.findViewById<TextView>(R.id.txt_labl2)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("Yes") { dialog: DialogInterface, i: Int ->
            println(dialog)
            println(i)
            //  sendNotice()
        }

        dialog.setNegativeButton("No") { dialog, id ->
            println(dialog)
            println(id)
            dialog.cancel()

        }
        dialog.show()
    }


    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri): Bitmap? {
        var input = this.contentResolver.openInputStream(uri)

        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1)
            return null

        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth

        val ratio = if (originalSize > 100.0) originalSize / 100.0 else 1.0

        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inDither = true//optional

        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888//optional
        input = this.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return bitmap
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0)
            1
        else
            k
    }


    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

    private fun uploadFile() {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()

//        PdfNameHolder = txt_fileStart.text.toString() + "_" + PdfNameEditText!!.text.toString().trim()
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)


        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", STUD_ID.trim(), requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), STUD_ID.trim())
//        val std_Id = RequestBody.create(MediaType.parse("text/plain"), STUD_ID.trim())

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =phpApiInterface.uploadGrievancePdf(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>,responsee: Response<ServerResponse>)
            {
                val serverResponse3 = responsee.body()
                if (serverResponse3 != null) {
                    if (serverResponse3!!.success)
                    {
                        var fileurl:String=serverResponse3.message.toString()
                        var filename:String=serverResponse3.filename.toString()
                        try {
                            if (InternetConnection.checkConnection(this@GreivanceStudFile)) {


                                mServices.InsertStudentGrievance(
                                    G_SUBJECT,
                                    G_CATEGORY,
                                    G_AGAINST,
                                    G_DISCRIPTION,
                                    current_date,
                                    "F",
                                    "Open",
                                    ASSING_TO_ID.toInt(),
                                    "-",
                                    STUD_ID.toInt(),
                                    course_id,
                                    roll_no,
                                    str_NameGriev,
                                    instituteName,
                                    str_ComplaintToGriev,
                                    filename,
                                    selected_Department,
                                   fileurl,
                                    "-"
                                ).enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        dialogCommon!!.dismiss()
                                        Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(
                                        call: Call<APIResponse>,
                                        response: Response<APIResponse>
                                    ) {
                                        val result: APIResponse? = response.body()
                                        dialogCommon!!.dismiss()
                                        Toast.makeText(this@GreivanceStudFile, result!!.Status, Toast.LENGTH_SHORT)
                                            .show()
                                        GenericUserFunction.showPositivePopUp(
                                            this@GreivanceStudFile,
                                            "Notice Send Successfully"
                                        )
                                    }
                                })
                            }else
                            {
                                GenericUserFunction.showInternetNegativePopUp(
                                    this@GreivanceStudFile,
                                    getString(R.string.failureNoInternetErr)
                                )
                            }
                        }
                        catch (ex:java.lang.Exception){
                            dialogCommon!!.dismiss()
                            GenericUserFunction.showApiError(
                                this@GreivanceStudFile,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                    else
                    {
                        dialogCommon!!.dismiss()
                        GenericUserFunction.showApiError(
                            this@GreivanceStudFile,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    dialogCommon!!.dismiss()
                    assert(serverResponse3 != null)
//                    Log.v("Response", serverResponse3!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable)
            {
                dialogCommon!!.dismiss()
                GenericUserFunction.showApiError(
                    this@GreivanceStudFile,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        })
    }

    private fun uploadFileImg() {
        dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()

//        PdfNameHolder = txt_fileStart.text.toString() + "_" + PdfNameEditText!!.text.toString().trim()
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)

        var longString=file.name+"@cut"+STUD_ID.trim()
        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", longString, requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), longString)
//        val std_Id = RequestBody.create(MediaType.parse("text/plain"), STUD_ID.trim())

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =phpApiInterface.uploadGrievanceImg(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>,responsee: Response<ServerResponse>)
            {
                val serverResponse3 = responsee.body()
                if (serverResponse3 != null) {
                    if (serverResponse3!!.success)
                    {
                        var fileurl:String=serverResponse3.message.toString()
                        var filename:String=serverResponse3.filename.toString()
                        try {
                            if (InternetConnection.checkConnection(this@GreivanceStudFile)) {


                                mServices.InsertStudentGrievance(
                                    G_SUBJECT,
                                    G_CATEGORY,
                                    G_AGAINST,
                                    G_DISCRIPTION,
                                    current_date,
                                    confirmStatus,
                                    "Open",
                                    ASSING_TO_ID.toInt(),
                                    "-",
                                    STUD_ID.toInt(),
                                    course_id,
                                    roll_no,
                                    str_NameGriev,
                                    instituteName,
                                    str_ComplaintToGriev,
                                    filename,
                                    selected_Department,
                                    fileurl,
                                    "-"
                                ).enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        dialogCommon!!.dismiss()
                                        Toast.makeText(this@GreivanceStudFile, t.message, Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onResponse(
                                        call: Call<APIResponse>,
                                        response: Response<APIResponse>
                                    ) {
                                        val result: APIResponse? = response.body()
                                        dialogCommon!!.dismiss()
//                                        Toast.makeText(this@GreivanceStudFile, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()Responsecode
                                        if (result!!.Responsecode==200){

                                            GenericPublicVariable.CustDialog = Dialog(this@GreivanceStudFile)
                                            GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                                            var ivPosClose1: ImageView =
                                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                                            var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                            var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                            tvMsg.text = "Grievance Registered Successfully"
                                            GenericPublicVariable.CustDialog.setCancelable(false)
                                            btnOk.setOnClickListener {
                                                GenericPublicVariable.CustDialog.dismiss()
                                                callSelf(this@GreivanceStudFile)

                                            }
                                            ivPosClose1.setOnClickListener {
                                                GenericPublicVariable.CustDialog.dismiss()
                                                callSelf(this@GreivanceStudFile)

                                            }

                                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                            GenericPublicVariable.CustDialog.show()

                                        }else{
                                            GenericUserFunction.showApiError(
                                                this@GreivanceStudFile,
                                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                            )
                                        }
                                        mediaPath=null
                                        confirmStatus="F"
                                    }
                                })
                            }else
                            {
                                GenericUserFunction.showInternetNegativePopUp(
                                    this@GreivanceStudFile,
                                    getString(R.string.failureNoInternetErr)
                                )
                            }
                        }
                        catch (ex:java.lang.Exception){
                            dialogCommon!!.dismiss()
                            GenericUserFunction.showApiError(
                                this@GreivanceStudFile,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                    else
                    {
                        dialogCommon!!.dismiss()
                        GenericUserFunction.showApiError(
                            this@GreivanceStudFile,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    dialogCommon!!.dismiss()
                    assert(serverResponse3 != null)
//                    Log.v("Response", serverResponse3!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable)
            {
                dialogCommon!!.dismiss()
                GenericUserFunction.showApiError(
                    this@GreivanceStudFile,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        })
    }

    fun callSelf (ctx:Context){
        val intent = Intent(ctx, ctx::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }

//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    private fun uploadFunction() {
//
//
//        if (confirmStatus == "T") {
//            // PdfNameHolder = txt_fileStart.text.toString() + "_" + edit_upload_fname!!.text.toString().trim()
//            dialogCommon!!.setMessage("Please Wait!!! \nwhile we are sending your notice")
//            dialogCommon!!.setCancelable(false)
//            dialogCommon!!.show()
//
//            PdfPathHolder = FilePath.getPath(this, uri)
//
//            if (PdfPathHolder == null) {
//                dialogCommon!!.dismiss()
//
//                Toast.makeText(
//                    this,
//                    "Please move your PDF file to internal storage & try again.",
//                    Toast.LENGTH_LONG
//                ).show()
//
//            } else {
//                //Dialog Start
////            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
//                try {
//
//
//                    PdfID = UUID.randomUUID().toString()
//                    random = Random().nextInt(61) + 20
//                    uploadReceiver.setDelegate(this)
//                    uploadReceiver.setUploadID(PdfID!!)
//                    MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
//                        .addFileToUpload(PdfPathHolder, "pdf")
//                        .addParameter("name", STUD_ID.trim())//PdfID + random.toString())
//                        .setNotificationConfig(UploadNotificationConfig())
//                        .setMaxRetries(5)
//                        .startUpload()
////                dialog.dismiss()
//
//                } catch (exception: Exception) {
//                    dialogCommon!!.dismiss()
//
//                    Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        } else
//        {
//            var CustDialog = Dialog(this)
//            CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
//            var ivNegClose1: ImageView =
//                CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
//            var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
//            var btnCustomDialogCancel: Button =
//                CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
//            var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
//
//
//            tvMsg.text = "Are you sure, You want to submit this grievance without Attachment?"
////    GenericPublicVariable.CustDialog.setCancelable(false)
//            btnOk.setOnClickListener {
//                CustDialog.dismiss()
//                noAttachGrievanceFunction()
////                checkDate = 1
//
//            }
//            btnCustomDialogCancel.setOnClickListener {
//                CustDialog.dismiss()
////                checkDate = 0
//            }
//            ivNegClose1.setOnClickListener {
//                CustDialog.dismiss()
////                checkDate = 0
//
//            }
//            CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            CustDialog.show()
//        }
//    }

}
