package com.dmims.dmims.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mServices
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.Generic.showToast
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.*
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_registrar_feedback_schdule.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistrarFeedbackSchdule : AppCompatActivity() {
    var dialogCommon: android.app.AlertDialog? = null
    var from_date: TextView? = null
    var to_date: TextView? = null

    lateinit var from_notice_date: String

    var from_date_conv: String? = null
    var to_date_conv: String? = null

    var from_date_d: String? = null
    var to_date_d: String? = null


    private lateinit var btn_ScheduleFeedback: Button
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner
    private lateinit var spinner_FeedbackType: Spinner

    var listsinstz: Int = 0
    var instituteName1 = ArrayList<String>()
    var feedBackType = ArrayList<String>()
    private lateinit var selectedInstituteName: String
    private lateinit var selectedFeedbackName: String
    private lateinit var selecteddeptlist: String
    private lateinit var selecteddeptYear: String
    private var selectedcourselist: String? = null
    var deptlist = ArrayList<String>()
    var course_id: String = "All"
    var dept_id: String = ""
    var courselist = ArrayList<String>()
    var studYearArray = arrayOf("1st", "2nd", "3rd", "4th", "5th", "All ( First to Final Year )")
    internal var mUserItems = java.util.ArrayList<Int>()
    internal var mUserDeptItems = java.util.ArrayList<Int>()

    var select_date: TextView? = null
    private lateinit var date_sel: String
    private lateinit var btn_submit: Button
    private var progressbarlogin4: ProgressBar? = null
    private var feedbackTypeList: ArrayList<FeedBackMasterDataRef>? = null
    var cal = Calendar.getInstance()
    private lateinit var id_admin: String
    private lateinit var roleadmin: String


    private lateinit var mServices: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_feedback_schdule)
        select_date = findViewById<TextView>(R.id.select_date)
        spinner_institue = findViewById(R.id.spinner_institue)

        spinner_courselist = findViewById(R.id.spinner_courselist)
        spinner_FeedbackType = findViewById(R.id.spinner_FeedbackType)

        btn_ScheduleFeedback = findViewById<Button>(R.id.btn_ScheduleFeedback)

        dialogCommon = SpotsDialog.Builder().setContext(this).build()

        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        var drawer_titler = mypref.getString("key_drawer_title", null)
        id_admin = mypref.getString("Stud_id_key", null)!!
        roleadmin = mypref.getString("key_userrole", null)!!


        btn_Scheduled.setOnClickListener {
            val intent = Intent(this@RegistrarFeedbackSchdule, ScheduledFeedbackEI::class.java)

            startActivity(intent)

        }
        mServices = Common.getAPI()




        instituteName1.add("Select Institute")

        feedBackType.add("Select Feedback Type")

        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        select_date!!.text = sdf.format(cal.time).toString()
        date_sel = sdf.format(cal.time).toString()

        from_date = findViewById<TextView>(R.id.select_from_date)
        to_date = findViewById<TextView>(R.id.select_to_date)

        from_date!!.text = sdf.format(cal.time).toString()
        to_date!!.text = sdf.format(cal.time).toString()

        val myFormat4 = "yyyy-MM-dd" // mention the format you need
        val sdf4 = SimpleDateFormat(myFormat4, Locale.US)
        from_date_conv = sdf4.format(cal.time).toString()
        to_date_conv = sdf4.format(cal.time).toString()

        val myFormat5 = "dd-MM-yyyy" // mention the format you need
        val sdf5 = SimpleDateFormat(myFormat5, Locale.US)
        from_date_d = sdf5.format(cal.time).toString()

        val myFormat6 = "dd-MM-yyyy" // mention the format you need
        val sdf6 = SimpleDateFormat(myFormat6, Locale.US)
        to_date_d = sdf6.format(cal.time).toString()


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

                if (txt_year.text.toString() == "") {
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
            if (!txt_Dept.text.toString().equals("Select Department")) {
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

                    if (txt_Dept.text.toString() == "") {
                        txt_Dept.text = "Select Department"
                    }
                    mUserDeptItems.removeAll(mUserDeptItems)

                    if (InternetConnection.checkConnection(this)) {
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
                                            this@RegistrarFeedbackSchdule,
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
                                                this@RegistrarFeedbackSchdule,
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
                                this@RegistrarFeedbackSchdule,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    } else {
                        GenericUserFunction.showInternetNegativePopUp(
                            this@RegistrarFeedbackSchdule,
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


            } else {
                GenericUserFunction.DisplayToast(
                    this, "Please Choose Course then Departments to proceed"
                )
            }

        }


        var userfeedBackTypeadp: ArrayAdapter<String> = ArrayAdapter<String>(
            this@RegistrarFeedbackSchdule,
            R.layout.support_simple_spinner_dropdown_item,
            feedBackType
        )

        spinner_FeedbackType.adapter = userfeedBackTypeadp
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog =
                SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are updating courses")
            dialog.setCancelable(false)
            dialog.show()
            try {
                mServices.GetInstituteData()
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            dialog.dismiss()
                            Toast.makeText(
                                this@RegistrarFeedbackSchdule,
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

                                GenericUserFunction.showApiError(
                                    this@RegistrarFeedbackSchdule,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )
                            } else {
                                listsinstz = result.Data6!!.size
                                instituteName1.add("All Institute")
//                        instituteName1.remove("All")
                                for (i in 0..listsinstz - 1) {

//                            if (result.Data6!![i].Course_Institute.equals("JNMC",ignoreCase = true))
//                            {

                                    instituteName1.add(result.Data6!![i].Course_Institute)
//                            }
                                }
                            }
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
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

        var institueAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@RegistrarFeedbackSchdule,
            R.layout.support_simple_spinner_dropdown_item, instituteName1
        )

        if (InternetConnection.checkConnection(this)) {
            try {
                var phpApiInterface: PhpApiInterface =
                    ApiClientPhp.getClient().create(PhpApiInterface::class.java)

                var call: Call<FeedBackMaster> = phpApiInterface.feedback_master()
                call.enqueue(object : Callback<FeedBackMaster> {
                    override fun onResponse(
                        call: Call<FeedBackMaster>,
                        response: Response<FeedBackMaster>
                    ) {
                        var users = ArrayList<FeedBackMaster>()
                        if (response.isSuccessful) {
                            users.clear()
                            feedbackTypeList = response.body()!!.Datav
                            if (feedbackTypeList!![0].ID == "error") {
                                Toast.makeText(
                                    this@RegistrarFeedbackSchdule,
                                    "No Data in Time Table Master.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                users.clear()
                                var listSize = feedbackTypeList!!.size
                                for (i in 0 until listSize) {
                                    if (feedBackType.contains(feedbackTypeList!![i].FEEDBACK_NAME)) {
                                    } else {
                                        feedBackType.add(feedbackTypeList!![i].FEEDBACK_NAME)
                                    }
                                }
                                userfeedBackTypeadp.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun onFailure(call: Call<FeedBackMaster>, t: Throwable) {
                        GenericUserFunction.showApiError(
                            this@RegistrarFeedbackSchdule,
                            t.message.toString()
                        )
                    }

                })
            } catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

        spinner_institue.adapter = institueAdap
        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@RegistrarFeedbackSchdule)) {
                    try {
                        selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                        courselist.clear()
                        mServices.GetInstituteData()
                            .enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@RegistrarFeedbackSchdule,
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
                                            this@RegistrarFeedbackSchdule,
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
                            this@RegistrarFeedbackSchdule,
                            R.layout.support_simple_spinner_dropdown_item,
                            courselist
                        )
                        spinner_courselist.adapter = usercourselistadp
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@RegistrarFeedbackSchdule,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@RegistrarFeedbackSchdule,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@RegistrarFeedbackSchdule)) {
                    try {
                        selectedcourselist = p0!!.getItemAtPosition(p2) as String
                        deptlist.clear()
                        mServices.GetInstituteData()
                            .enqueue(object : Callback<APIResponse> {
                                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@RegistrarFeedbackSchdule,
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
                                            this@RegistrarFeedbackSchdule,
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
                        txt_Dept.text = "All Department"
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@RegistrarFeedbackSchdule,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                } else {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@RegistrarFeedbackSchdule,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        btn_ScheduleFeedback.setOnClickListener {
            if (selectedFeedbackName.equals("Select Feedback Type")) {
                showToast("Please select type of Feedback")
                return@setOnClickListener
            }
            if (select_date!!.text.isEmpty()) {
                select_date!!.error = "Please select feedback schedule date"
                return@setOnClickListener
            }

            if (to_date!!.text.isEmpty()) {
                to_date!!.error = "Please select to date"
                return@setOnClickListener
            }

            if (from_date!!.text.isEmpty()) {
                from_date!!.error = "Please select from date"
                return@setOnClickListener
            }

            if (selectedInstituteName == "Select Institute") {
                Toast.makeText(this, "Please select Institute to proceed", Toast.LENGTH_SHORT)
                    .show()
                GenericUserFunction.DisplayToast(this, "Please select Institute to proceed")
                return@setOnClickListener
            }
            if (txt_year.text.toString() == "Select Year") {
                txt_year.error = "Please select year"
                GenericUserFunction.DisplayToast(this, "Please select year to proceed")
                return@setOnClickListener
            } else {
                txt_year.error = null
                if (txt_year.text.contains("All ( First to Final Year )")) {
                    txt_year.text = "All ( First to Final Year )"
                }
            }

            if (txt_Dept.text.toString() == "All Department") {
                selecteddeptlist = "All Department"
                dept_id = "D000000"
            } else
                if (txt_Dept.text.toString() == "Select Department") {
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

            if (selectedcourselist == "All Courses") {
                course_id = "C000000"
            }


            if (InternetConnection.checkConnection(this)) {
                dialogCommon!!.setMessage("Please Wait!!! \nwhile we are Scheduling Feedback")
                dialogCommon!!.setCancelable(false)
                dialogCommon!!.show()
                try {
                    var phpApiInterface: PhpApiInterface =
                        ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                    var call: Call<FeedBackInsert> = phpApiInterface.InsertFeedBackScheduler(
                        selectedInstituteName,
                        selectedcourselist!!,
                        selecteddeptlist,
                        selectedFeedbackName,
                        select_date!!.text.toString(),
                        from_date_conv.toString(),
                        to_date_conv.toString(),
                        txt_year.text.toString()
                    )
                    call.enqueue(object : Callback<FeedBackInsert> {
                        override fun onFailure(call: Call<FeedBackInsert>, t: Throwable) {
                            dialogCommon!!.dismiss()
                            GenericUserFunction.showApiError(
                                this@RegistrarFeedbackSchdule,
                                t.message.toString()
                            )
                        }

                        override fun onResponse(
                            call: Call<FeedBackInsert>,
                            response: Response<FeedBackInsert>
                        ) {
                            val result: FeedBackInsert? = response.body()
                            if (result!!.Response == "Feedback Schedule Successfully") {

                                sendNotice()
                            }


                        }

                    })
                } catch (ex: Exception) {
                    dialogCommon!!.dismiss()
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }

            } else {
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr)
                )
            }
        }

        spinner_FeedbackType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedFeedbackName = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }


    fun clickToDataPicker1(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                select_date!!.text = sdf.format(date).toString()
                //Toast.makeText(this, """$dayOfMonth-${monthOfYear + 1}-$year""", Toast.LENGTH_LONG).show()
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun clickToDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                to_date!!.text = sdf.format(date).toString()

                val myFormat2 = "yyyy-MM-dd" // mention the format you need
                val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date2 = cal.time
                sdf2.format(date2)
                to_date_conv = sdf2.format(date2).toString()


                val myFormat6 = "dd-MM-yyyy" // mention the format you need
                val sdf6 = SimpleDateFormat(myFormat6, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date6 = cal.time
                sdf6.format(date6)
                to_date_d = sdf6.format(date6).toString()


            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun clickFromDataPicker(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                from_date!!.text = sdf.format(date).toString()

                //   from_notice_date= from_date!!.text as String

                val myFormat3 = "yyyy-MM-dd" // mention the format you need
                val sdf3 = SimpleDateFormat(myFormat3, Locale.US)

                cal.set(year, monthOfYear, dayOfMonth)
                val date3 = cal.time
                sdf3.format(date3)
                from_date_conv = sdf3.format(date3).toString()



                val myFormat5 = "dd-MM-yyyy" // mention the format you need
                val sdf5 = SimpleDateFormat(myFormat5, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date5 = cal.time
                sdf5.format(date5)
                from_date_d = sdf5.format(date5).toString()


            },
            year,
            month,
            day
        )
        dpd.show()
    }
//    private fun validateDateData() {
//
//
//    }

    private fun sendNotice() {
        if (InternetConnection.checkConnection(this)) {
            var feedbackSche1 = " Dear Student,Exam Feedback Scheduled From "
            var feedbackSche2 = "Please Submit Your feedback before deadline"
            println("from_date ====" + from_date_d)

            id_admin
            roleadmin


            try {
                mServices.UploadNotice(
                    from_date_d.toString(),
                    "Feedback Scheduled",
                    feedbackSche1 + from_date_d + " to " + to_date_d,
                    selectedInstituteName,
                    "" + selectedcourselist,
                    "" + selecteddeptlist,
                    "Administrative",
                    "Student",
                    "F",
                    roleadmin,
                    id_admin,
                    "-",
                    course_id,
                    "All",
                    "T",
                    "F",
                    "T",
                    txt_year.text.toString()
                ).enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dialogCommon!!.dismiss()
                        GenericUserFunction.showApiError(
                            this@RegistrarFeedbackSchdule,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        dialogCommon!!.dismiss()
//
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 200) {
                            GenericPublicVariable.CustDialog = Dialog(this@RegistrarFeedbackSchdule)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                            var ivPosClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                            var btnOk: Button =
                                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                            var tvMsg: TextView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                            tvMsg.text = "Feedback Scheduled Successfully"
                            GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@RegistrarFeedbackSchdule)

                            }
                            ivPosClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@RegistrarFeedbackSchdule)

                            }

                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                ColorDrawable(Color.TRANSPARENT)
                            )
                            GenericPublicVariable.CustDialog.show()
                        } else {
                            GenericUserFunction.showApiError(
                                applicationContext,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }

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
            dialogCommon!!.dismiss()
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

    }

    fun callSelf(ctx: Context) {
        val intent = Intent(ctx, ctx::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }

}
