/* @created By Umesh Gaidhane along with XML
*/
package com.dmims.dmims.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mServices
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.MyResponse
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_mcq__examcell.*
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadNotificationConfig
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ExamMcqUpload : AppCompatActivity() {
    private var mediaPath: String? = null
    private val TAG1: String = "AndroidUploadService"
    var dialogCommon: android.app.AlertDialog ?= null
    
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner

    private lateinit var from_date_layout: LinearLayout
    private lateinit var start_date: TextView
    private lateinit var start_date_send: String
    private lateinit var to_date_layout: LinearLayout
    private lateinit var end_date: TextView
    private lateinit var end_date_send: String
    private lateinit var btn_gallary: Button
    private lateinit var btn_Submit: Button
//    private lateinit var btn_GetMCQ: Button

    private lateinit var txt_fileStart: TextView


    private var PdfID: String? = null
    private lateinit var McqUploadDate: String
    var SelectButton: Button? = null
    var UploadButton: Button? = null

    var PdfNameEditText: EditText? = null

    var uri: Uri? = null

    var PDF_REQ_CODE = 1

    var PdfNameHolder: String? = null
    var PdfPathHolder: String? = null
    var listsinstz: Int = 0
    var instituteName1 = ArrayList<String>()
    var stud_year = ArrayList<String>()
    var courselist = ArrayList<String>()
    var deptlist = ArrayList<String>()
    private lateinit var selectedInstituteName: String
    private lateinit var selectedcourselist: String
    private lateinit var selecteddeptlist: String
    private lateinit var selectedStudyear: String
    var course_id: String = "All"
    var dept_id: String = "All"
    var cal = Calendar.getInstance()
    var checkDate: Int = 0

    private lateinit var UserName: String
    private lateinit var UserMobileNo: String
    private lateinit var UserRole: String
    private lateinit var UserEmail: String
    private lateinit var UserDesig: String
    private lateinit var UserID: String

    var studYearArray = arrayOf("1st", "2nd", "3rd", "Final Year","All ( First to Final Year )")
    internal var mUserItems = java.util.ArrayList<Int>()
    internal var mUserDeptItems = java.util.ArrayList<Int>()


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mcq__examcell)
        dialogCommon= SpotsDialog.Builder().setContext(this).build()
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_courselist = findViewById(R.id.spinner_courselist)

        from_date_layout = findViewById(R.id.from_date_layout)
        start_date = findViewById(R.id.txt_from_date)
        to_date_layout = findViewById(R.id.to_date_layout)
        end_date = findViewById(R.id.txt_to_date)
        btn_gallary = findViewById(R.id.btn_gallary)
        btn_Submit = findViewById(R.id.btn_Submit)
        txt_fileStart = findViewById(R.id.txt_fileStart)
//        btn_GetMCQ = findViewById(R.id.btn_GetMCQ)

        var single: SingleUploadBroadcastReceiver
        instituteName1.add("Select Institute")
      

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)!!
        UserID = mypref.getString("Stud_id_key", null)!!
        UserName = mypref.getString("key_drawer_title", null)!!
        UserMobileNo = mypref.getString("key_editmob", null)!!
        UserRole = mypref.getString("key_userrole", null)!!
        UserEmail = mypref.getString("key_email", null)!!
        UserDesig = mypref.getString("key_designation", null)!!

        var checkedItems = BooleanArray(studYearArray.size)
        txt_year.text = "Select Year"
        linear_year.setOnClickListener {

            val mBuilder = AlertDialog.Builder(this)
            mBuilder.setTitle("Select year to send notice")
            mBuilder.setMultiChoiceItems(
                studYearArray, checkedItems
            ) { dialogInterface, position, isChecked ->

                if (isChecked) {
                    mUserItems.add(position)
                } else {
                    mUserItems.remove(Integer.valueOf(position))
                }
            }

            mBuilder.setCancelable(false)
            mBuilder.setPositiveButton(
                "Ok"
            ) { dialogInterface, which ->
                var item = ""
                for (i in mUserItems.indices) {
                    item = item + studYearArray[mUserItems.get(i)]
                    if (i != mUserItems.size - 1) {
                        item = "$item, "
                    }
                }
                if (item.contains("All ( First to Final Year )")) {
                    txt_year.text = "All ( First to Final Year )"
                } else {
                    txt_year.setText(item)
                }

                if (txt_year.text.toString()==""){
                    txt_year.text = "Select Year"
                }
            }

            val negativeButton = mBuilder.setNegativeButton(
                "Dismiss"
            ) { dialogInterface, i -> dialogInterface.dismiss() }

            mBuilder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, which ->
                for (i in checkedItems.indices) {
                    checkedItems[i] = false
                    mUserItems.clear()
                    txt_year.setText("Select Year")
                }
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }


        txt_Dept.text = "Select Department"
        selfMultipleDept.setOnClickListener {
            if(!txt_Dept.text.toString().equals("Select Department")){
                var checkedDeptItems = BooleanArray(deptlist.size)
                val mBuilder = AlertDialog.Builder(this)
                mBuilder.setTitle("Select Department to send notice")
                var deptArray = deptlist.toArray(arrayOfNulls<String>(deptlist.size))
                mBuilder.setMultiChoiceItems(deptArray, checkedDeptItems)
                { dialogInterface, position, isChecked ->
                    if (isChecked) {
                        mUserDeptItems.add(position)
                    } else {
                        mUserDeptItems.remove(Integer.valueOf(position))
                    }
                }

                mBuilder.setCancelable(false)
                mBuilder.setPositiveButton(
                    "Ok"
                ) { dialogInterface, which ->


                    var item = ""
                    txt_Dept.setText("")
                    for (i in mUserDeptItems.indices) {
                        item = item + deptArray[mUserDeptItems.get(i)]
                        if (i != mUserDeptItems.size - 1) {
                            item = "$item, "
                        }
                    }

                    txt_Dept.text = item
                    if (txt_Dept.text.toString().contains("All Department")) {
                        dept_id = "D000000"
                        txt_Dept.text = "All Department"
                    }

                    if (txt_Dept.text.toString()==""){
                        txt_Dept.text = "Select Department"
                    }
                    mUserDeptItems.removeAll(mUserDeptItems)

                    if (InternetConnection.checkConnection(this)) {
                        val dialog: android.app.AlertDialog =
                            SpotsDialog.Builder().setContext(this).build()
                        dialog.setMessage("Please Wait!!! \nwhile we are getting Department ID's")
                        dialog.setCancelable(false)
                        dialog.show()
                        try {
//                        selecteddeptlist = p0!!.getItemAtPosition(p2) as String
                            selecteddeptlist = txt_Dept.text as String
                            mServices.GetInstituteData()
                                .enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        dialog.dismiss()
                                        Toast.makeText(
                                            this@ExamMcqUpload,
                                            t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onResponse(
                                        call: Call<APIResponse>,
                                        response: Response<APIResponse>
                                    ) {
                                        dialog.dismiss()
                                        val result: APIResponse? = response.body()
                                        if (result!!.Responsecode == 204) {
                                            Toast.makeText(
                                                this@ExamMcqUpload,
                                                result.Status,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val listsinstz: Int = result.Data6!!.size
                                            dept_id = ""
                                            var selected_dept = selecteddeptlist.split(",")
                                            var selected_dept_size = selected_dept.size
                                            for (i in 0..listsinstz - 1) {
                                                if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                                    val listscoursez: Int =
                                                        result.Data6!![i].Courses!!.size
                                                    for (j in 0..listscoursez - 1) {
                                                        if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                            val listsdeptz: Int =
                                                                result.Data6!![i].Courses!![j].Departments!!.size
                                                            for (k in 0 until listsdeptz) {
                                                                for (m in 0 until selected_dept_size) {
//                                                                println(" i >> $i , j >> $j, k >> $k, m >> $m Department >>> $dept_id")

                                                                    if (result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME.equals(
                                                                            selected_dept[m].trim()
                                                                        )
                                                                    ) {
                                                                        dept_id =
                                                                            dept_id + "_" + result.Data6!![i].Courses!![j].Departments!![k].DEPT_ID

                                                                    }
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            println("Department >>> " + dept_id)

                                        }
                                    }
                                })
                        } catch (ex: Exception) {
                            dialog.dismiss()
                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                this@ExamMcqUpload,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    } else {
                        GenericUserFunction.showInternetNegativePopUp(
                            this@ExamMcqUpload,
                            getString(R.string.failureNoInternetErr)
                        )
                    }

                }

                val negativeButton = mBuilder.setNegativeButton(
                    "Dismiss"
                ) { dialogInterface, i -> dialogInterface.dismiss() }

                mBuilder.setNeutralButton(
                    "Clear All"
                ) { dialogInterface, which ->
                    for (i in checkedDeptItems.indices) {
                        checkedDeptItems[i] = false
                        mUserDeptItems.clear()
                        txt_Dept.setText("Select Department")
                    }
                }

                val mDialog2 = mBuilder.create()
                mDialog2.show()


            }else {
                GenericUserFunction.DisplayToast(
                    this, "Please Choose Course then Departments to proceed"
                )
            }

        }

//        btn_GetMCQ.setOnClickListener {
//            val intent = Intent(this@ExamMcqUpload, EXAM_GET_UploadMCQ::class.java)
//            startActivity(intent)
//        }


//        AllowRunTimePermission()

//        SelectButton = findViewById<View>(R.id.button) as Button
//        UploadButton = findViewById<View>(R.id.button2) as Button
        PdfNameEditText = findViewById<View>(R.id.edit_upload_fname) as EditText


        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        end_date!!.text = sdf.format(cal.time).toString()
        start_date!!.text = sdf.format(cal.time).toString()

        val myFormat_send = "yyyy-MM-dd" // mention the format you need
        val sdf_send = SimpleDateFormat(myFormat_send, Locale.US)
        start_date_send=sdf_send.format(cal.time).toString()
        end_date_send=sdf_send.format(cal.time).toString()



        McqUploadDate= sdf.format(cal.time).toString()
//        to_date_sel = sdf.format(cal.time)
//        current_date = sdf.format(cal.time)
//        from_date_sel = sdf.format(cal.time)

//        SelectButton!!.setOnClickListener {
//            // PDF selection code start from here .
//
//            val intent = Intent()
//
//            intent.type = "application/pdf"
//
//            intent.action = Intent.ACTION_GET_CONTENT
//
//            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE)
//        }

        btn_Submit.setOnClickListener {

            if (selectedInstituteName=="Select Institute") {
                Toast.makeText(this, "Please select Institute to proceed", Toast.LENGTH_SHORT).show()
                GenericUserFunction.DisplayToast(this, "Please select Institute to proceed")
                return@setOnClickListener
            }
            if (selectedcourselist=="Select Course")
            {
                //course_id="C000000"
                GenericUserFunction.DisplayToast(
                    this, "Please select course to proceed"
                )
                return@setOnClickListener
            }
            if (txt_year.text.toString()=="Select Year") {
                txt_year.error = "Please select year"
                GenericUserFunction.DisplayToast(this, "Please select year to proceed")
                return@setOnClickListener
            } else {
                txt_year.error = null
                if (txt_year.text.contains("All ( First to Final Year )")) {
                    txt_year.text = "All ( First to Final Year )"
                }
            }

            if (txt_Dept.text.toString()=="All Department") {
                selecteddeptlist="All Department"
                dept_id = "D000000"
            } else
                if (txt_Dept.text.toString()=="Select Department") {
                    txt_Dept.error = "Please Choose Departments to proceed"
                    GenericUserFunction.DisplayToast(
                        this, "Please Choose Departments to proceed"
                    )
                    return@setOnClickListener
                } else
                    if (dept_id != null || dept_id != "") {
                        txt_Dept.error = null
                        dept_id = dept_id!!.removeRange(0, 1)

                    } else {

                        txt_Dept.error = "Please Choose Departments to proceed"
                        GenericUserFunction.DisplayToast(
                            this, "Please Choose Departments to proceed"
                        )
                        return@setOnClickListener
                    }



            if (mediaPath != null) {
//        PdfPathHolder = FilePath.getPath(this, uri)
//
//        if (PdfPathHolder == null) {
//
//            GenericUserFunction.showOopsError(this, "Please select your PDF")
////        Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show()
//            return@setOnClickListener
//        }
            } else {
                GenericUserFunction.showOopsError(this, "Please select your PDF")
                return@setOnClickListener

            }

            if (edit_upload_fname.text.toString().length < 1) {
                GenericUserFunction.showOopsError(this, "Please write your PDF Name")
//        Toast.makeText(this, "Please select your PDF.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            if (txt_to_date.text.toString().equals(txt_from_date.text.toString())) {


                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
                var ivNegClose1: ImageView = CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button = CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView


                tvMsg.text = "Start Date and End Date are same,\nStill you want to submit this Answer Key?"
//    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    CustDialog.dismiss()
//                    PdfUploadFunction()
                    uploadFile()
                    checkDate = 1

                }
                btnCustomDialogCancel.setOnClickListener {
                    CustDialog.dismiss()
                    checkDate = 0
                }
                ivNegClose1.setOnClickListener {
                    CustDialog.dismiss()
                    checkDate = 0

                }
                CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                CustDialog.show()

                return@setOnClickListener


            }
            if (checkDate == 1) {
                checkDate = 0
            } else {
//                PdfUploadFunction()
                uploadFile()
            }

        }

        btn_gallary!!.setOnClickListener {
            // PDF selection code start from here .

//            val intent = Intent()
//
//            intent.type = "application/pdf"
//
//            intent.action = Intent.ACTION_GET_CONTENT
//
//            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE)

            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "*/*"
            startActivityForResult(galleryIntent, 300)
        }

//        UploadButton!!.setOnClickListener { PdfUploadFunction() }
//        UploadButton!!.setOnClickListener {
////            PdfUploadFunction()
//        }

        if (InternetConnection.checkConnection(this)) {
        try {
            mServices.GetInstituteData()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@ExamMcqUpload, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            Toast.makeText(this@ExamMcqUpload, result.Status, Toast.LENGTH_SHORT).show()
                        } else {
                            listsinstz = result.Data6!!.size
                            for (i in 0..listsinstz - 1) {
                                instituteName1.add(result.Data6!![i].Course_Institute)
                            }
                        }
                    }
                })
        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }}
        else
        {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

        var institueAdap: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this@ExamMcqUpload,
                R.layout.support_simple_spinner_dropdown_item, instituteName1
            )
        spinner_institue!!.adapter = institueAdap
        spinner_institue!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@ExamMcqUpload)) {
                    try {
                        selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                        if (selectedInstituteName=="JNMC"){
                            studYearArray = arrayOf("1st", "2nd", "Final MBBS Part 1","Final MBBS Part 2", "All ( First to Final Year )")
                        }else{
                            studYearArray = arrayOf("1st", "2nd", "3rd", "Final Year","All ( First to Final Year )")
                        }
                        courselist.clear()
                        mServices.GetInstituteData()
                            .enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@ExamMcqUpload,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<APIResponse>,
                                    response: Response<APIResponse>
                                ) {
                                    val result: APIResponse? = response.body()
                                    if (result!!.Responsecode == 204) {
                                        Toast.makeText(
                                            this@ExamMcqUpload,
                                            result.Status,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val listsinstz: Int = result.Data6!!.size
                                        for (i in 0..listsinstz - 1) {
                                            if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                                val listscoursez: Int =
                                                    result.Data6!![i].Courses!!.size
                                                for (j in 0..listscoursez - 1) {
                                                    courselist.add(result.Data6!![i].Courses!![j].COURSE_NAME)
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        courselist.add("Select Course")
                        var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                            this@ExamMcqUpload,
                            R.layout.support_simple_spinner_dropdown_item,
                            courselist
                        )
                        spinner_courselist.adapter = usercourselistadp
                    } catch (ex: Exception) {

                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@ExamMcqUpload,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                }else
                {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@ExamMcqUpload,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@ExamMcqUpload)) {
                    try {
                        selectedcourselist = p0!!.getItemAtPosition(p2) as String
                        deptlist.clear()
                        mServices.GetInstituteData()
                            .enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@ExamMcqUpload,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<APIResponse>,
                                    response: Response<APIResponse>
                                ) {
                                    val result: APIResponse? = response.body()
                                    if (result!!.Responsecode == 204) {
                                        Toast.makeText(
                                            this@ExamMcqUpload,
                                            result.Status,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val listsinstz: Int = result.Data6!!.size
                                        for (i in 0..listsinstz - 1) {
                                            if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                                val listscoursez: Int =
                                                    result.Data6!![i].Courses!!.size
                                                for (j in 0..listscoursez - 1) {
                                                    if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                        course_id =
                                                            result.Data6!![i].Courses!![j].COURSE_ID
                                                        val listsdeptz: Int =
                                                            result.Data6!![i].Courses!![j].Departments!!.size
                                                        for (k in 0 until listsdeptz) {
                                                            deptlist.add(result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME)
                                                        }
                                                    }

                                                }
                                            }
                                        }

                                    }
                                }
                            })
                        deptlist.add(0, "All Department")
                        txt_Dept.text="All Department"
                        
                    } catch (ex: Exception) {

                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@ExamMcqUpload,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                }else
                {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@ExamMcqUpload,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

//        spinner_deptlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                try {
//                    selecteddeptlist = p0!!.getItemAtPosition(p2) as String
//
//                } catch (ex: Exception) {
//
//                    ex.printStackTrace()
//                    GenericUserFunction.showApiError(
//                        this@ExamMcqUpload,
//                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//                    )
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }

      

//        spinner_yearlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                try {
//                    selectedStudyear = p0!!.getItemAtPosition(p2) as String
//
//                } catch (ex: java.lang.Exception) {
//
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == PDF_REQ_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//
//            uri = data.data
//            //btn_gallary!!.text = "PDF is Selected"
//            txt_fileStart.text =
//                selectedInstituteName + "_" + selectedcourselist + "_" + selecteddeptlist + "_" + selectedStudyear
//
//        }
//
//        else
            if (requestCode == 300 && resultCode == Activity.RESULT_OK && null != data) {
                if (txt_Dept.text.toString()=="All Department") {
                    selecteddeptlist="All Department"
                    dept_id = "D000000"
                }
                if (txt_year.text.toString()=="Select Year") {
                    txt_year.error = "Please select year"
                    GenericUserFunction.DisplayToast(this, "Please select year to proceed")

                }else {

                    val selectedImage = data.data
                    txt_fileStart.text =
                        selectedInstituteName + "_" + selectedcourselist + "_" + selecteddeptlist + "_" + txt_year.text.toString()

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)!!
                    cursor.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    println("mediaPath >>> $mediaPath")
                    //str1.setText(mediaPath)
                    // Set the Image in ImageView for Previewing the Media
                    //imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()
                }

        }

    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    fun PdfUploadFunction() {
//
//
//        PdfNameHolder = txt_fileStart.text.toString() + "_" + edit_upload_fname!!.text.toString().trim()
//
//        PdfPathHolder = FilePath.getPath(this, uri)
//
//        if (PdfPathHolder == null) {
//
//            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show()
//
//        } else {
//            //Dialog Start
//
//
//            try {
//                dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
//                dialogCommon!!.setCancelable(false)
//                dialogCommon!!.show()
//                //Dialog End
//
//
//                PdfID = UUID.randomUUID().toString()
//
////                uploadReceiver.setDelegate(this)
////                uploadReceiver.setUploadID(PdfID!!)
//
//                MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
//                    .addFileToUpload(PdfPathHolder, "pdf")
//                    .addParameter("name", PdfNameHolder)
//                    .addParameter("institute", selectedInstituteName)
//
//                    .addParameter("UserName", UserName)
//                    .addParameter("UserMobileNO", UserMobileNo)
//                    .addParameter("UserRole", UserRole)
//                    .addParameter("UserEmail", UserEmail)
//                    .addParameter("UserDesig", UserDesig)
//                    .addParameter("Course", selectedcourselist)
//                    .addParameter("Department", selecteddeptlist)
//                    .addParameter("Year", selectedStudyear)
//                    .addParameter("McqUploadDate", McqUploadDate)
//                    .addParameter("StartDate", start_date_send)
//                    .addParameter("EndDate", end_date_send)
//                    .setNotificationConfig(UploadNotificationConfig())
//                    .setMaxRetries(5)
//                    .startUpload()
//
//
//
//            } catch (exception: Exception) {
//                dialogCommon!!.dismiss()
//
//                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }

    fun UpdateNotice(): Boolean {

        var success: Boolean = false
        if (InternetConnection.checkConnection(this)) {
        var filename = "-"
        //Dialog Start
//        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
        try {

//            dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
//            dialog.setCancelable(false)
//            dialog.show()
            //Dialog End
            mServices.UploadNotice(
                start_date.text.toString(),
                "MCQ Key",
                "Dear Student Please find Uploaded MCQ Answer Key",
                selectedInstituteName!!,
                selectedcourselist,
                selecteddeptlist,
                "General",
                "Student",
                "F",
                UserRole,
                UserID,
                filename,
                course_id,
                dept_id,
                "T",
                "T",
                "T",
                txt_year.text.toString()
            ).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    dialogCommon!!.dismiss()
                    Toast.makeText(this@ExamMcqUpload, t.message, Toast.LENGTH_SHORT).show()
                    success = false
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    dialogCommon!!.dismiss()
                    val result: APIResponse? = response.body()
                                 if(result!!.Responsecode==200){
                                     GenericPublicVariable.CustDialog = Dialog(this@ExamMcqUpload)
                                     GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                                     var ivPosClose1: ImageView =
                                         GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                                     var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                     var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                     tvMsg.text = "MCQ Key Uploaded Successfully"
                                     GenericPublicVariable.CustDialog.setCancelable(false)
                                     btnOk.setOnClickListener {
                                         GenericPublicVariable.CustDialog.dismiss()
                                         callSelf(this@ExamMcqUpload)

                                     }
                                     ivPosClose1.setOnClickListener {
                                         GenericPublicVariable.CustDialog.dismiss()
                                         callSelf(this@ExamMcqUpload)

                                     }

                                     GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                     GenericPublicVariable.CustDialog.show()
                                     success = true
                                 }else
                                 {
                                     GenericUserFunction.showApiError(
                                         applicationContext,
                                         "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                     )
                                 }

                }
            })
        }
        catch (ex: Exception) {
            dialogCommon!!.dismiss()

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                applicationContext,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
            success = false
        }
    }
    else {
        dialogCommon!!.dismiss()
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr)
        )
    }
        return success
    }

//    fun AllowRunTimePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                this@ExamMcqUpload,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        ) {
//
////            Toast.makeText(this@ExamMcqUpload, "READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG)
////                .show()
//
//        } else {
//
//            ActivityCompat.requestPermissions(this@ExamMcqUpload, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//
//        }
//    }

    override fun onRequestPermissionsResult(RC: Int, per: Array<String>, Result: IntArray) {

        when (RC) {

            1 ->

                if (Result.size > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this@ExamMcqUpload, "Permission Granted", Toast.LENGTH_LONG).show()

                } else {

                    Toast.makeText(this@ExamMcqUpload, "Permission Canceled", Toast.LENGTH_LONG).show()

                }
        }
    }

    companion object {

        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/pdfupload/upload.php"
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
                println(view)
                println(year)

                val myFormat_send = "yyyy-MM-dd" // mention the format you need
                val sdf_send = SimpleDateFormat(myFormat_send, Locale.US)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                start_date!!.text = sdf.format(date).toString()
                start_date_send=sdf_send.format(date).toString()
            }, year, month, day
        )
        dpd.show()
    }

    fun clickToDataPicker(view: View)
    {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)

                val myFormat_send = "yyyy-MM-dd" // mention the format you need
                val sdf_send = SimpleDateFormat(myFormat_send, Locale.US)

                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                end_date!!.text = sdf.format(date).toString()
                end_date_send=sdf_send.format(date).toString()
            }, year, month, day
        )
        dpd.show()
    }

    private fun validateDate() {
        if (end_date!!.text.isEmpty()) {
            end_date!!.error = "Please select to date"
            return
        } else {
            // to_date_sel = end_date!!.text.toString()
        }

        if (start_date!!.text.isEmpty()) {
            end_date!!.error = "Please select from date"
            return
        } else {
            // from_date_sel = start_date!!.text.toString()
        }
    }


    private fun uploadFile() {
        if (InternetConnection.checkConnection(this)) {

            dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
        dialogCommon!!.setCancelable(false)
        dialogCommon!!.show()

        PdfNameHolder = txt_fileStart.text.toString() + "_" + PdfNameEditText!!.text.toString().trim()
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)


        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName())

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =phpApiInterface.uploadMcqPdf(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(call: Call<ServerResponse>,responsee: Response<ServerResponse>)
            {
                val serverResponse3 = responsee.body()
                if (serverResponse3 != null) {
                    if (serverResponse3!!.success) {
//                        Toast.makeText(this@ExamMcqUpload,serverResponse.message,Toast.LENGTH_SHORT).show()

                        var phpApiInterface2: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                        var call2: Call<ServerResponse> =
                            phpApiInterface2.InsertMcqKeyInfo(
                                selectedInstituteName,
                                UserName,UserMobileNo,
                                UserRole,UserEmail,
                                UserDesig,
                                selectedcourselist,
                                selecteddeptlist,
                                txt_year.text.toString(),
                                McqUploadDate,
                                start_date_send,
                                end_date_send,
                                serverResponse3.message!!,
                                PdfNameHolder!!
                                )
                        call2.enqueue(object : Callback<ServerResponse> {
                            override fun onResponse(call: Call<ServerResponse>,responseee: Response<ServerResponse>
                            ) {
                                val serverResponse2 = responseee.body()
                                if (serverResponse2 != null) {
                                    if (serverResponse2!!.success) {
                                        UpdateNotice()

                                    }
                                }else
                                {
                                    dialogCommon!!.dismiss()
                                    GenericUserFunction.showApiError(
                                        this@ExamMcqUpload,
                                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                    )

                                }
                            }
                            override fun onFailure(call4: Call<ServerResponse>, t: Throwable) {
                                dialogCommon!!.dismiss()
                                GenericUserFunction.showApiError(
                                    this@ExamMcqUpload,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
                        })
                    } else {
                        dialogCommon!!.dismiss()
                        GenericUserFunction.showApiError(
                            this@ExamMcqUpload,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    dialogCommon!!.dismiss()
                    assert(serverResponse3 != null)
                    Log.v("Response", serverResponse3!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable)
            {
                dialogCommon!!.dismiss()
                GenericUserFunction.showApiError(
                    this@ExamMcqUpload,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        })
        }
        else
        {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr))
        }
    }

    fun callSelf (ctx:Context){
        val intent = Intent(ctx, ctx::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }


}