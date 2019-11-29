package com.dmims.dmims.activity

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
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
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch
import com.androidbuts.multispinnerfilter.SpinnerListener
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.adapter.MultiSelectAdap_U
import com.dmims.dmims.broadCasts.SingleUploadBroadcastReceiver
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.MultiSelectData
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.MyResponse
import com.dmims.dmims.model.ServerResponse
import com.dmims.dmims.remote.Api
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_exam_admin_notice_board.*
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
import kotlin.collections.ArrayList

class ExamAdminNoticeBoard : AppCompatActivity() {
    private var Dept2Array: Array<String>? = null
    private var mediaPath: String? = null
    var dialogCommon: android.app.AlertDialog? = null
    private var selectedImage: Uri? = null
    private var READ_REQUEST_CODE = 300
    private var confirmStatus = "F"
    private var SERVER_PATH = "http://103.68.25.26/dmims/UploadImage/"
    private var uri: Uri? = null
    private lateinit var mServices: IMyAPI
    private var TAG = ExamAdminNoticeBoard::class.java.simpleName
    var deptlist = ArrayList<String>()
    var noticetype = arrayOf("Administrative", "General")
    var facultystud = arrayOf("All", "Faculty", "Student")
    var studYearArray = arrayOf("1st", "2nd", "3rd", "4th", "5th", "All ( First to Final Year )")
    internal var mUserItems = java.util.ArrayList<Int>()
    internal var mUserDeptItems = java.util.ArrayList<Int>()

    private lateinit var btnPickImage: Button
    private lateinit var btnPubNotice: Button
    private lateinit var spinner_noticetype: Spinner
    private lateinit var spinner_facultystud: Spinner


    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner
    //    private lateinit var spinner_deptlist: Spinner
    private lateinit var rImage: String
    private lateinit var rTitle: String
    var instituteName1 = ArrayList<String>()//Creating an empty arraylist
    var courselist = ArrayList<String>()

    private lateinit var sel_notice_type: String
    var filename: String = "-"
    var course_id: String = "All"
    var dept_id: String = ""
    private lateinit var student_flag: String
    private lateinit var faculty_flag: String
    private lateinit var admin_flag: String
    private lateinit var selectedInstituteName: String
    private lateinit var selectedcourselist: String
    private lateinit var selecteddeptlist: String
    private lateinit var selectedNoticeType: String
    private lateinit var selectedFacultyStud: String
    private lateinit var notice_date: String
    private lateinit var notice_title: String
    private lateinit var notice_desc: String
    private lateinit var editNoticeDate: TextView
    private lateinit var edit_notice_title: TextView
    private lateinit var edit_notice_desc: TextView

    var RESOU_FLAG: String = "F"
    var Institute_Name: String? = null

    var cal = Calendar.getInstance()
    var listsinstz: Int = 0
    private lateinit var UserName: String
    private lateinit var UserMobileNo: String
    private lateinit var UserRole: String
    private lateinit var UserEmail: String
    private lateinit var UserDesig: String
    private lateinit var UserID: String

    private val REQUEST_GALLERY_CODE = 111
    var REQUEST_CODE: Int = 0
    var type: String? = null
    var PdfPathHolder: String? = null
    private var PdfID: String? = null
    private var random: Int? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_admin_notice_board)
        dialogCommon = SpotsDialog.Builder().setContext(this).build()
        btnPickImage = findViewById<Button>(R.id.admin_notice_upload)
        btnPubNotice = findViewById<Button>(R.id.btn_publish_notice2)
        editNoticeDate = findViewById(R.id.select_date)
        edit_notice_title = findViewById(R.id.edit_notice_title)
        edit_notice_desc = findViewById(R.id.notice_descr)
        spinner_noticetype = findViewById(R.id.spinner_noticetype)
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_courselist = findViewById(R.id.spinner_courselist)
//        spinner_deptlist = findViewById(R.id.spinner_deptlist)
        spinner_facultystud = findViewById(R.id.spinner_facultystud)

        multiYearLayout.visibility = View.GONE
        MultiSpinnerYear.visibility = View.GONE


//        spinner_multiple.visibility = View.VISIBLE
//        selfMultipleYear.visibility = View.VISIBLE

        //val progressBar = findViewById<ProgressBar>(R.id.progressBar2)

//        MultiSpinnerYear.isColorSeparation=true
//        MultiSpinnerYear.setBackgroundColor(getResources().getColor(R.color.colorSpinnerOrangeDark))
        ////////////Start
//        var studYearArray = arrayOf("Select Year")
//        var StudYearAdap: ArrayAdapter<String> =
//            ArrayAdapter(
//                this,
//                R.layout.support_simple_spinner_dropdown_item,
//                studYearArray
//            )
//        spinner_multiple.adapter = StudYearAdap


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

                if (InternetConnection.checkConnection(this@ExamAdminNoticeBoard)) {
                    val dialog: android.app.AlertDialog =
                        SpotsDialog.Builder().setContext(this).build()
                    dialog.setMessage("Please Wait!!! \nwhile we are updating courses")
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
                                        this@ExamAdminNoticeBoard,
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
                                            this@ExamAdminNoticeBoard,
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
                            this@ExamAdminNoticeBoard,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@ExamAdminNoticeBoard,
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

        ////////////End
//        val yr_list = Arrays.asList(*resources.getStringArray(R.array.StudYear))
//        val listArray0 = java.util.ArrayList<KeyPairBoolData>()
//
//        var listSize = yr_list.size
//        val yearData = ArrayList<MultiSelectData>()
//        for (i in 0..listSize - 1) {
//            yearData.add(
//                MultiSelectData(yr_list[i], false)
//            )
//        }

//        val multiSelectAdapter = MultiSelectAdap_U(this, yearData)
//        spinner_multiple.adapter = multiSelectAdapter

        //Student Year Spinner


//        spinner_multiple.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@ExamAdminNoticeBoard,yr_list[p2] , Toast.LENGTH_LONG).show();
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        }


//        for (i in yr_list.indices) {
//            val h = KeyPairBoolData()
//            h.id = (i + 1).toLong()
//            h.name = yr_list.get(i)
//            h.isSelected = false
//            listArray0.add(h)
//        }
//        MultiSpinnerYear.setEmptyTitle("No Data Found")
//        MultiSpinnerYear.setSearchHint("Find Hint")
//        MultiSpinnerYear.setItems(listArray0, -1,object :SpinnerListener{
//
//            onItemsSelecte
//        }
//        )

//        MultiSpinnerYear.setItems(listArray0, -1,
//            SpinnerListener { items ->
//                for (i in items.indices) {
//                    if (items[i].isSelected) {
//                        Log.i(
//                            TAG,
//                            i.toString() + " : " + items[i].name + " : " + items[i].isSelected
//                        )
//                    }
//                }
//            })


        instituteName1.add("Select Institute")

        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        Institute_Name = mypref.getString("key_institute", null)



        UserRole = mypref.getString("key_userrole", null)!!
        UserID = mypref.getString("Stud_id_key", null)!!
        UserName = mypref.getString("key_drawer_title", null)!!
        UserMobileNo = mypref.getString("key_editmob", null)!!
        UserEmail = mypref.getString("key_email", null)!!
        UserDesig = mypref.getString("key_designation", null)!!

        mServices = Common.getAPI()

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        editNoticeDate.text = sdf.format(cal.time).toString()

        //Spinner_1
        var noticetypeAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, noticetype)
        spinner_noticetype.adapter = noticetypeAdap

        spinner_noticetype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedNoticeType = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        //Spinner_2
        var usertypeAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, facultystud)
        spinner_facultystud.adapter = usertypeAdap

        spinner_facultystud.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFacultyStud = p0!!.getItemAtPosition(p2) as String

                if (selectedFacultyStud == "All") {
                    student_flag = "T"
                    admin_flag = "T"
                    faculty_flag = "T"
                }
                if (selectedFacultyStud == "Student") {
                    student_flag = "T"
                    admin_flag = "T"
                    faculty_flag = "F"
                }
                if (selectedFacultyStud == "Faculty") {
                    student_flag = "F"
                    admin_flag = "T"
                    faculty_flag = "T"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var institueAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@ExamAdminNoticeBoard,
            R.layout.support_simple_spinner_dropdown_item, instituteName1
        )
        spinner_institue.adapter = institueAdap

        if (InternetConnection.checkConnection(this)) {

            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are updating Institute Info")
            dialog.setCancelable(false)
            dialog.show()
            try {

                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            dialog.dismiss()

                            Toast.makeText(this@ExamAdminNoticeBoard, t.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(
                                    this@ExamAdminNoticeBoard,
                                    result.Status,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                listsinstz = result.Data6!!.size
                                instituteName1.add("All Institute")
//                            instituteName1.add(Institute_Name!!)
                                for (i in 0..listsinstz - 1)
//                            {
//
//                                if (result.Data6!![i].Course_Institute.equals(
//                                        "JNMC",
//                                        ignoreCase = true
//                                    )
//                                )
                                {

                                    instituteName1.add(result.Data6!![i].Course_Institute)
                                }
//                            }
                            }
                            institueAdap.notifyDataSetChanged()
                        }

                    })



            } catch (ex: Exception) {
                institueAdap.notifyDataSetChanged()
                dialog.dismiss()
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else
        {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }



        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@ExamAdminNoticeBoard)) {
                    try {
                        selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                        courselist.clear()
                        mServices.GetInstituteData()
                            .enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@ExamAdminNoticeBoard,
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
                                            this@ExamAdminNoticeBoard,
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
                        courselist.add("All Courses")
                        var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                            this@ExamAdminNoticeBoard,
                            R.layout.support_simple_spinner_dropdown_item,
                            courselist
                        )
                        spinner_courselist.adapter = usercourselistadp
                    } catch (ex: Exception) {

                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@ExamAdminNoticeBoard,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@ExamAdminNoticeBoard,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@ExamAdminNoticeBoard)) {
//                    if (selectedInstituteName != "Select institute") {
                        val dialog: android.app.AlertDialog =
                            SpotsDialog.Builder().setContext(this@ExamAdminNoticeBoard).build()
                        dialog.setMessage("Please Wait!!! \nwhile we are updating courses")
                        dialog.setCancelable(false)
                        dialog.show()
                        try {


                            selectedcourselist = p0!!.getItemAtPosition(p2) as String
                            deptlist.clear()

                            mServices.GetInstituteData()
                                .enqueue(object : Callback<APIResponse> {
                                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                        dialog.dismiss()
                                        Toast.makeText(
                                            this@ExamAdminNoticeBoard,
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
                                                this@ExamAdminNoticeBoard,
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
                                            deptlist.add(0, "All Department")
                                            txt_Dept.text="All Department"

                                        }


                                    }
                                })

//                        var userdeptlistadp: ArrayAdapter<String> =
//                            ArrayAdapter<String>(
//                                this@ExamAdminNoticeBoard,
//                                R.layout.support_simple_spinner_dropdown_item, deptlist
//                            )
//                        spinner_deptlist.adapter = userdeptlistadp


                        } catch (ex: Exception) {
                            dialog.dismiss()
                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                this@ExamAdminNoticeBoard,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
//                } else {
//                    GenericUserFunction.showInternetNegativePopUp(
//                        this@ExamAdminNoticeBoard,
//                        getString(R.string.failureNoInternetErr)
//                    )
//                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

//        spinner_deptlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                if (InternetConnection.checkConnection(this@ExamAdminNoticeBoard)) {
//                    try {
//                        selecteddeptlist = p0!!.getItemAtPosition(p2) as String
//                        mServices.GetInstituteData()
//                            .enqueue(object : Callback<APIResponse> {
//                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                                    Toast.makeText(
//                                        this@ExamAdminNoticeBoard,
//                                        t.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                                override fun onResponse(
//                                    call: Call<APIResponse>,
//                                    response: Response<APIResponse>
//                                ) {
//                                    val result: APIResponse? = response.body()
//                                    if (result!!.Responsecode == 204) {
//                                        Toast.makeText(
//                                            this@ExamAdminNoticeBoard,
//                                            result.Status,
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    } else {
//                                        val listsinstz: Int = result.Data6!!.size
//                                        dept_id=""
//                                        for (i in 0..listsinstz - 1) {
//                                            if (result.Data6!![i].Course_Institute == selectedInstituteName) {
//                                                val listscoursez: Int =
//                                                    result.Data6!![i].Courses!!.size
//                                                for (j in 0..listscoursez - 1) {
//                                                    if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
//                                                        val listsdeptz: Int =
//                                                            result.Data6!![i].Courses!![j].Departments!!.size
//                                                        for (k in 0 until listsdeptz) {
//                                                            if (result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME .contains(selecteddeptlist) ){
//                                                                dept_id += result.Data6!![i].Courses!![j].Departments!![k].DEPT_ID
//                                                            }
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//
//                                    }
//                                }
//                            })
//                    } catch (ex: Exception) {
//
//                        ex.printStackTrace()
//                        GenericUserFunction.showApiError(
//                            this@ExamAdminNoticeBoard,
//                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//                        )
//                    }
//                } else {
//                    GenericUserFunction.showInternetNegativePopUp(
//                        this@ExamAdminNoticeBoard,
//                        getString(R.string.failureNoInternetErr)
//                    )
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }

        btnPickImage.setOnClickListener {
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
//                pickImage()
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                galleryIntent.type = "*/*"
                startActivityForResult(galleryIntent, 400)

            }
            btnpdf.setOnClickListener {
                //                REQUEST_CODE = 200
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

        btnPubNotice.setOnClickListener {
            sendNotice()
        }

    }

    fun noticedateclick(view: View) {
        println(view)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                editNoticeDate.text = """$dayOfMonth-${monthOfYear + 1}-$year"""
            },
            year,
            month,
            day
        )
        dpd.show()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun sendNotice() {
        if (editNoticeDate.text.toString().isEmpty()) {
            editNoticeDate.error = "Please select notice date"
            return
        } else {
            notice_date = editNoticeDate.text.toString()
        }

        if (edit_notice_title.text.toString().isEmpty()) {
            edit_notice_title.error = "Please input notice board title"
            GenericUserFunction.DisplayToast(this, "Please give Notice Title to proceed")
            return
        } else {
            edit_notice_title.error = null
            notice_title = edit_notice_title.text.toString()
        }
        if (edit_notice_desc.text.toString().isEmpty()) {
            edit_notice_desc.error = "Please input notice board description"
            GenericUserFunction.DisplayToast(this, "Please give Notice description to proceed")
            return
        } else {
            edit_notice_desc.error = null
            notice_desc = edit_notice_desc.text.toString()
        }
        if (selectedInstituteName=="Select Institute") {
            Toast.makeText(this, "Please select Institute to proceed", Toast.LENGTH_SHORT).show()
            GenericUserFunction.DisplayToast(this, "Please select Institute to proceed")
            return
        }
        if (txt_year.text.toString()=="Select Year") {
            txt_year.error = "Please select year"
            GenericUserFunction.DisplayToast(this, "Please select year to proceed")
            return
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
                return
            } else
                if (dept_id != null || dept_id != "") {
                    txt_Dept.error = null
                    dept_id = dept_id!!.removeRange(0, 1)

                } else {

                    txt_Dept.error = "Please Choose Departments to proceed"
                    GenericUserFunction.DisplayToast(
                        this, "Please Choose Departments to proceed"
                    )
                    return
                }

        if (selectedcourselist=="All Courses")
        {
            course_id="C000000"
        }
        if (UserID.isEmpty()) {
//            edit_notice_desc.error = "Please relogin again"
            GenericUserFunction.DisplayToast(
                this,
                "please re-login, Data need to be refreshed to proceed"
            )
            return
        } else {
            UserID = UserID
        }


        if (confirmStatus == "T" && type == "pdf" && mediaPath != null) {
            try {
//                uploadFile()
                uploadFileImg()
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }
        if (confirmStatus == "T" && type == "image" && mediaPath != null) {
            try {
                uploadFileImg()

            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }
        if (confirmStatus == "F") {
            var CustDialog = Dialog(this)
            CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
            var ivNegClose1: ImageView =
                CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
            var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
            var btnCustomDialogCancel: Button =
                CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
            var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView


            tvMsg.text = "Do you want to submit Notice without attachment?"
//    GenericPublicVariable.CustDialog.setCancelable(false)
            btnOk.setOnClickListener {
                CustDialog.dismiss()
                SubmitNoticeWithoutFile()

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

    private fun SubmitNoticeWithoutFile() {
        if (InternetConnection.checkConnection(this)) {
            try {
                //Dialog Start
                val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
                dialog.setMessage("Please Wait!!! \nwhile we are updating your Notice")
                dialog.setCancelable(false)
                dialog.show()
                //Dialog End
                filename = "-"
                try {
                    mServices.UploadNotice(
                        notice_date,
                        notice_title,
                        notice_desc,
                        selectedInstituteName,
                        selectedcourselist,
                        selecteddeptlist,
                        selectedNoticeType,
                        selectedFacultyStud,
                        confirmStatus,
                        UserRole,
                        UserID,
                        filename,
                        course_id,
                        "" + dept_id,
                        student_flag,
                        faculty_flag,
                        admin_flag,
                        txt_year.text.toString()
                    ).enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@ExamAdminNoticeBoard, t.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            dialog.dismiss()
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 200) {
                                GenericPublicVariable.CustDialog = Dialog(this@ExamAdminNoticeBoard)
                                GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                                var ivPosClose1: ImageView =
                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                tvMsg.text = "Notice Send Successfully"
                                GenericPublicVariable.CustDialog.setCancelable(false)
                                btnOk.setOnClickListener {
                                    GenericPublicVariable.CustDialog.dismiss()
                                    callSelf(this@ExamAdminNoticeBoard)

                                }
                                ivPosClose1.setOnClickListener {
                                    GenericPublicVariable.CustDialog.dismiss()
                                    callSelf(this@ExamAdminNoticeBoard)

                                }

                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                GenericPublicVariable.CustDialog.show()
                            } else {
                                GenericUserFunction.showApiError(
                                    this@ExamAdminNoticeBoard,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
//                            Toast.makeText(this@ExamAdminNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                .show()
                        }
                    })
                } catch (ex: Exception) {
                    dialog.dismiss()

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            dialogCommon!!.dismiss()
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {
//                rImage = data!!.getStringExtra("notice_image")
//                rTitle = data.getStringExtra("notice_content")
//                RESOU_FLAG = "T"
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(this@ExamAdminNoticeBoard, "Nothing Selected", Toast.LENGTH_SHORT)
//            }
//        }
//    }

    //new one
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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

//               uploadFile(selectedImage, "My Image")
            }
        } else if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
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
        } else if (requestCode == 300 && resultCode == Activity.RESULT_OK && null != data) {
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

        } else if (requestCode == 400 && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            var bitmaps: Bitmap =
                MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
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

    private fun uploadFile() {
        // Map is used to multipart the file using okhttp3.RequestBody
        val file = File(mediaPath)

        // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody)
        val filename = RequestBody.create(MediaType.parse("text/plain"), file.getName())

        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
            PhpApiInterface::class.java
        )
        var call3: Call<ServerResponse> =
            phpApiInterface.uploadFile(fileToUpload, filename)
        call3.enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                val serverResponse = response.body()
                if (serverResponse != null) {
                    if (serverResponse!!.success) {
//                        Toast.makeText(
//                            this@ExamAdminNoticeBoard,
//                            serverResponse.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
                        try {
                            mServices.UploadNotice(
                                notice_date,
                                notice_title,
                                notice_desc,
                                selectedInstituteName,
                                selectedcourselist,
                                selecteddeptlist,
                                selectedNoticeType,
                                selectedFacultyStud,
                                confirmStatus,
                                UserRole,
                                UserID,
                                serverResponse.message!!,
                                course_id,
                                dept_id,
                                student_flag,
                                faculty_flag,
                                admin_flag,
                                txt_year.text.toString()
                            ).enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    dialogCommon!!.dismiss()
                                    Toast.makeText(
                                        this@ExamAdminNoticeBoard,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<APIResponse>,
                                    response: Response<APIResponse>
                                ) {
                                    dialogCommon!!.dismiss()
                                    val result: APIResponse? = response.body()

                                    GenericUserFunction.showPositivePopUp(
                                        this@ExamAdminNoticeBoard,
                                        "Notice Send Successfully"
                                    )
                                }
                            })
                        } catch (ex: Exception) {
                            dialogCommon!!.dismiss()

                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                applicationContext,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            serverResponse!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    dialogCommon!!.dismiss()
//                    assert(serverResponse != null)
//                    Log.v("Response", serverResponse!!.toString())
                }

            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                dialogCommon!!.dismiss()
                Toast.makeText(this@ExamAdminNoticeBoard, t.message, Toast.LENGTH_LONG).show()
            }
        })
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

    fun pickImage() {
        REQUEST_CODE = 100
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        println("path >> " + i.data)

        startActivityForResult(i, 101)
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        var byte: ByteArrayOutputStream = ByteArrayOutputStream(100000)
//    ByteArrayOutputStream bytes = new ByteArrayOutputStream()
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//    return uri.parse(path);
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, byte)
        var path: String =
            MediaStore.Images.Media.insertImage(inContext!!.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    companion object {
        //val PDF_UPLOAD_HTTP_URL = "http://avbrh.gearhostpreview.com/pdfupload/upload.php"
        val PDF_UPLOAD_HTTP_URL = "http://dmimsdu.in/web/pdfupload/pdfnoticeupload.php"
    }

    private fun uploadFileImg() {
        if (InternetConnection.checkConnection(this)) {
            dialogCommon!!.setMessage("Please Wait!!! \nwhile we are updating your Exam Key")
            dialogCommon!!.setCancelable(false)
            dialogCommon!!.show()

//        PdfNameHolder = txt_fileStart.text.toString() + "_" + PdfNameEditText!!.text.toString().trim()
            // Map is used to multipart the file using okhttp3.RequestBody
            val file = File(mediaPath)

            var longString = file.name + "@cut" + UserRole.trim() //roleadmin
            // Parsing any Media type file
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", longString, requestBody)
            val filename = RequestBody.create(MediaType.parse("text/plain"), longString)
//        val std_Id = RequestBody.create(MediaType.parse("text/plain"), STUD_ID.trim())

            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            var call3: Call<ServerResponse> =
                phpApiInterface.noticeCommonUpload(fileToUpload, filename)
            call3.enqueue(object : Callback<ServerResponse> {
                override fun onResponse(
                    call: Call<ServerResponse>,
                    responsee: Response<ServerResponse>
                ) {
                    val serverResponse3 = responsee.body()
                    if (serverResponse3 != null) {
                        if (serverResponse3!!.success) {
                            var fileurl: String = serverResponse3.message.toString()
//                        var filename:String=serverResponse3.filename.toString()
                            try {
                                if (InternetConnection.checkConnection(this@ExamAdminNoticeBoard)) {


                                    mServices.UploadNotice(
                                        notice_date,
                                        notice_title,
                                        notice_desc,
                                        selectedInstituteName,
                                        selectedcourselist,
                                        selecteddeptlist,
                                        selectedNoticeType,
                                        selectedFacultyStud,
                                        confirmStatus,
                                        UserRole,
                                        UserID,
                                        fileurl,
                                        course_id,
                                        dept_id,
                                        student_flag,
                                        faculty_flag,
                                        admin_flag,
                                        txt_year.text.toString()
                                    ).enqueue(object : Callback<APIResponse> {
                                        override fun onFailure(
                                            call: Call<APIResponse>,
                                            t: Throwable
                                        ) {
                                            dialogCommon!!.dismiss()
                                            Toast.makeText(
                                                this@ExamAdminNoticeBoard,
                                                t.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onResponse(
                                            call: Call<APIResponse>,
                                            response: Response<APIResponse>
                                        ) {
                                            val result: APIResponse? = response.body()
                                            dialogCommon!!.dismiss()
//                                        Toast.makeText(this@GreivanceStudFile, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()Responsecode
                                            if (result!!.Responsecode == 200) {

                                                GenericPublicVariable.CustDialog = Dialog(this@ExamAdminNoticeBoard)
                                                GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                                                var ivPosClose1: ImageView =
                                                    GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                                                var btnOk: Button = GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                                                var tvMsg: TextView = GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                                                tvMsg.text = "Notice Send Successfully"
                                                GenericPublicVariable.CustDialog.setCancelable(false)
                                                btnOk.setOnClickListener {
                                                    GenericPublicVariable.CustDialog.dismiss()
                                                    callSelf(this@ExamAdminNoticeBoard)

                                                }
                                                ivPosClose1.setOnClickListener {
                                                    GenericPublicVariable.CustDialog.dismiss()
                                                    callSelf(this@ExamAdminNoticeBoard)

                                                }

                                                GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                                                GenericPublicVariable.CustDialog.show()
                                            } else {
                                                GenericUserFunction.showApiError(
                                                    this@ExamAdminNoticeBoard,
                                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                                )
                                            }
                                            mediaPath = null
                                            confirmStatus = "F"
                                        }
                                    })
                                } else {
                                    GenericUserFunction.showInternetNegativePopUp(
                                        this@ExamAdminNoticeBoard,
                                        getString(R.string.failureNoInternetErr)
                                    )
                                }
                            } catch (ex: java.lang.Exception) {
                                dialogCommon!!.dismiss()
                                GenericUserFunction.showApiError(
                                    this@ExamAdminNoticeBoard,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )
                            }
                        } else {
                            dialogCommon!!.dismiss()
                            GenericUserFunction.showApiError(
                                this@ExamAdminNoticeBoard,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    } else {
                        dialogCommon!!.dismiss()
                        assert(serverResponse3 != null)
//                    Log.v("Response", serverResponse3!!.toString())
                    }

                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    dialogCommon!!.dismiss()
                    GenericUserFunction.showApiError(
                        this@ExamAdminNoticeBoard,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            })
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
    }
    fun callSelf (ctx:Context){
        val intent = Intent(ctx, ctx::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }
}


