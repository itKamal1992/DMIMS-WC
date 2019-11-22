package com.dmims.dmims.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.ImageClass
import com.dmims.dmims.ImageUpload
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FacultyNoticeBoard : AppCompatActivity() {
    private var READ_REQUEST_CODE = 300
    private var SERVER_PATH = "http://103.68.25.26/dmims/UploadImage/"
    private var uri: Uri? = null
    private lateinit var mServices: IMyAPI
    private var TAG = FacultyNoticeBoard::class.java.simpleName
    var noticetype = arrayOf("Administrative", "General")
    var facultystud = arrayOf("All", "Faculty", "Student")
    private lateinit var btnPickImage: Button
    private lateinit var btnPubNotice: Button
    private lateinit var spinner_noticetype: Spinner
    private lateinit var spinner_facultystud: Spinner
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner
    private lateinit var spinner_deptlist: Spinner
    private lateinit var rImage: String
    private lateinit var rTitle: String
    var instituteName1 = ArrayList<String>()//Creating an empty arraylist
    var courselist = ArrayList<String>()
    var deptlist = ArrayList<String>()
    private lateinit var sel_notice_type: String
    var filename: String = "-"
    var course_id: String = "All"
    var dept_id: String = "All"
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
    var cal = Calendar.getInstance()
    var listsinstz: Int = 0
    private lateinit var id_admin: String
    private lateinit var roleadmin: String
    private val REQUEST_GALLERY_CODE = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_notice_board)

        btnPickImage = findViewById<Button>(R.id.admin_notice_upload)
        btnPubNotice = findViewById<Button>(R.id.btn_publish_notice2)
        editNoticeDate = findViewById(R.id.select_date)
        edit_notice_title = findViewById(R.id.edit_notice_title)
        edit_notice_desc = findViewById(R.id.notice_descr)
        spinner_noticetype = findViewById(R.id.spinner_noticetype)
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_courselist = findViewById(R.id.spinner_courselist)
        spinner_deptlist = findViewById(R.id.spinner_deptlist)
        spinner_facultystud = findViewById(R.id.spinner_facultystud)
        roleadmin = "roleadmin"
        id_admin = "id_admin"
        instituteName1.add("All")
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
        if (InternetConnection.checkConnection(this)) {
            try {
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@FacultyNoticeBoard, t.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onResponse(
                            call: Call<APIResponse>,
                            response: Response<APIResponse>
                        ) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(
                                    this@FacultyNoticeBoard,
                                    result.Status,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                listsinstz = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    instituteName1.add(result.Data6!![i].Course_Institute)
                                }
                            }
                        }
                    })


                var institueAdap: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this@FacultyNoticeBoard,
                        R.layout.support_simple_spinner_dropdown_item, instituteName1
                    )
                spinner_institue.adapter = institueAdap
            } catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }else
        {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@FacultyNoticeBoard)) {
                try{
                selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                courselist.clear()
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@FacultyNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(this@FacultyNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                            } else {
                                val listsinstz: Int = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                        val listscoursez: Int = result.Data6!![i].Courses!!.size
                                        for (j in 0..listscoursez - 1) {
                                            courselist.add(result.Data6!![i].Courses!![j].COURSE_NAME)
                                        }
                                    }
                                }
                            }
                        }
                    })
                courselist.add("All")
                var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@FacultyNoticeBoard,
                    R.layout.support_simple_spinner_dropdown_item,
                    courselist
                )
                spinner_courselist.adapter = usercourselistadp
            }catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this@FacultyNoticeBoard,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
            }}
                else
                {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@FacultyNoticeBoard,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@FacultyNoticeBoard)) {
                try{
                selectedcourselist = p0!!.getItemAtPosition(p2) as String
                deptlist.clear()
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@FacultyNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(this@FacultyNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                            } else {
                                val listsinstz: Int = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                        val listscoursez: Int = result.Data6!![i].Courses!!.size
                                        for (j in 0..listscoursez - 1) {
                                            if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                course_id = result.Data6!![i].Courses!![j].COURSE_ID
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
                deptlist.add("All")
                var userdeptlistadp: ArrayAdapter<String> =
                    ArrayAdapter<String>(
                        this@FacultyNoticeBoard,
                        R.layout.support_simple_spinner_dropdown_item, deptlist
                    )
                spinner_deptlist.adapter = userdeptlistadp
            }catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this@FacultyNoticeBoard,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
            }
            }
            else
            {
                GenericUserFunction.showInternetNegativePopUp(
                    this@FacultyNoticeBoard,
                    getString(R.string.failureNoInternetErr)
                )
            }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_deptlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@FacultyNoticeBoard)) {
                try{
                selecteddeptlist = p0!!.getItemAtPosition(p2) as String
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            Toast.makeText(this@FacultyNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                            val result: APIResponse? = response.body()
                            if (result!!.Responsecode == 204) {
                                Toast.makeText(this@FacultyNoticeBoard, result.Status, Toast.LENGTH_SHORT).show()
                            } else {
                                val listsinstz: Int = result.Data6!!.size
                                for (i in 0..listsinstz - 1) {
                                    if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                        val listscoursez: Int = result.Data6!![i].Courses!!.size
                                        for (j in 0..listscoursez - 1) {
                                            if (result.Data6!![i].Courses!![j].COURSE_NAME == selectedcourselist) {
                                                val listsdeptz: Int =
                                                    result.Data6!![i].Courses!![j].Departments!!.size
                                                for (k in 0 until listsdeptz) {
                                                    if (result.Data6!![i].Courses!![j].Departments!![k].DEPT_NAME == selecteddeptlist) {
                                                        dept_id =
                                                            result.Data6!![i].Courses!![j].Departments!![k].DEPT_ID
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    })
            }catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this@FacultyNoticeBoard,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
            }
            }
            else
            {
                GenericUserFunction.showInternetNegativePopUp(
                    this@FacultyNoticeBoard,
                    getString(R.string.failureNoInternetErr)
                )
            }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnPickImage.setOnClickListener {
            val intent = Intent(applicationContext, ImageUpload::class.java)
            startActivityForResult(intent, 1)
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
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                println(view)
                println(year)
                editNoticeDate.text = """$dayOfMonth-${monthOfYear + 1}-$year"""

            }, year, month, day
        )
        dpd.show()
    }


    private fun sendNotice() {
        if (editNoticeDate.text.toString().isEmpty()) {
            editNoticeDate.error = "Please select notice date"
            return
        } else {
            notice_date = editNoticeDate.text.toString()
        }
        if (edit_notice_title.text.toString().isEmpty()) {
            edit_notice_title.error = "Please input notice board title"
            return
        } else {
            notice_title = edit_notice_title.text.toString()
        }
        if (edit_notice_desc.text.toString().isEmpty()) {
            edit_notice_desc.error = "Please input notice board description"
            return
        } else {
            notice_desc = edit_notice_desc.text.toString()
        }
        if (id_admin.isEmpty()) {
            edit_notice_desc.error = "Please relogin again"
            return
        } else {
            id_admin = id_admin
        }
        if (RESOU_FLAG == "T") {
            if (InternetConnection.checkConnection(this)) {
            try{
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
            var call: Call<ImageClass> = phpApiInterface.uploadImage(rTitle, rImage)
            call.enqueue(object : Callback<ImageClass> {
                override fun onFailure(call: Call<ImageClass>, t: Throwable) {
                    Toast.makeText(this@FacultyNoticeBoard, "Server Response" + t.message, Toast.LENGTH_SHORT)
                }

                override fun onResponse(call: Call<ImageClass>, response: Response<ImageClass>) {
                    var imageClass: ImageClass? = response.body()
                    Toast.makeText(this@FacultyNoticeBoard, imageClass!!.getResponse(), Toast.LENGTH_SHORT)
                    //filename = "http://avbrh.gearhostpreview.com/imageupload/" + imageClass.getuploadPath()
                    filename = "http://dmimsdu.in/web/imageupload/" + imageClass.getuploadPath()
                }

            })
            }catch (ex: Exception) {

                ex.printStackTrace()
                GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
            }
            }
            else
            {
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr))
            }


        }

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
                RESOU_FLAG,
                roleadmin,
                id_admin,
                filename,
                course_id,
                dept_id,
                student_flag,
                faculty_flag,
                admin_flag
            ).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@FacultyNoticeBoard, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    val result: APIResponse? = response.body()
                    Toast.makeText(this@FacultyNoticeBoard, result!!.Status, Toast.LENGTH_SHORT).show()
                }
            })
        }catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(this,"Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                rImage = data!!.getStringExtra("notice_image")
                rTitle = data.getStringExtra("notice_content")
                RESOU_FLAG = "T"
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this@FacultyNoticeBoard, "Nothing Selected", Toast.LENGTH_SHORT)
            }
        }
    }
}



