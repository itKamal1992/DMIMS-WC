package com.dmims.dmims.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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
    var from_date: TextView? = null
    var to_date: TextView? = null

    lateinit var from_notice_date:String

    var from_date_conv: String? = null
    var to_date_conv: String? = null

    var from_date_d: String? = null
    var to_date_d: String? = null


    private lateinit var btn_ScheduleFeedback: Button
    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_deptlist: Spinner
    private lateinit var spinner_courselist: Spinner
    private lateinit var spinner_FeedbackType: Spinner
    private lateinit var spinner_YearDept: Spinner
    var listsinstz: Int = 0
    var instituteName1 = ArrayList<String>()
    var feedBackType = ArrayList<String>()
    private lateinit var selectedInstituteName: String
    private lateinit var selectedFeedbackName: String
    private lateinit var selecteddeptlist: String
    private lateinit var selecteddeptYear: String
    private  var selectedcourselist: String?=null
    var deptlist = ArrayList<String>()
    var course_id: String = "All"
    var dept_id: String = "All"
    var courselist = ArrayList<String>()

    var select_date: TextView? = null
    private lateinit var date_sel: String
    private lateinit var btn_submit: Button
    private var progressbarlogin4: ProgressBar? = null
    private var feedbackTypeList: ArrayList<FeedBackMasterDataRef>? = null
    var cal = Calendar.getInstance()



    private lateinit var mServices: IMyAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_feedback_schdule)
        select_date = findViewById<TextView>(R.id.select_date)
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_deptlist = findViewById(R.id.spinner_deptlist)
        spinner_courselist = findViewById(R.id.spinner_courselist)
        spinner_FeedbackType = findViewById(R.id.spinner_FeedbackType)
        spinner_YearDept=findViewById(R.id.spinner_Yeardeptlist)
        btn_ScheduleFeedback = findViewById<Button>(R.id.btn_ScheduleFeedback)

        btn_Scheduled.setOnClickListener {
            val intent = Intent(this@RegistrarFeedbackSchdule, ScheduledFeedbackEI::class.java)

            startActivity(intent)

        }
        mServices = Common.getAPI()



        instituteName1.add("Select institute")
        instituteName1.add("All")
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
        to_date_conv= sdf4.format(cal.time).toString()

        val myFormat5 = "dd-MM-yyyy" // mention the format you need
        val sdf5= SimpleDateFormat(myFormat5, Locale.US)
        from_date_d = sdf5.format(cal.time).toString()

        val myFormat6= "dd-MM-yyyy" // mention the format you need
        val sdf6= SimpleDateFormat(myFormat6, Locale.US)
        to_date_d = sdf6.format(cal.time).toString()


        var userfeedBackTypeadp: ArrayAdapter<String> = ArrayAdapter<String>(
            this@RegistrarFeedbackSchdule,
            R.layout.support_simple_spinner_dropdown_item,
            feedBackType
        )

        spinner_FeedbackType.adapter = userfeedBackTypeadp
        if (InternetConnection.checkConnection(this)) {
        try {
            mServices.GetInstituteData()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            Toast.makeText(this@RegistrarFeedbackSchdule, result.Status, Toast.LENGTH_SHORT).show()
                        } else {
                            listsinstz = result.Data6!!.size
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
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )}
        }
        else {
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
        try{
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)

            var call: Call<FeedBackMaster> = phpApiInterface.feedback_master()
            call.enqueue(object : Callback<FeedBackMaster> {
                override fun onResponse(call: Call<FeedBackMaster>, response: Response<FeedBackMaster>) {
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
                        }
                    }
                }

                override fun onFailure(call: Call<FeedBackMaster>, t: Throwable) {
                    Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }catch (ex: Exception) {
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }
    else
    {
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr))
    }
        spinner_institue.adapter = institueAdap
        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@RegistrarFeedbackSchdule)) {
                try{
                    selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                    courselist.clear()
                    mServices.GetInstituteData()
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 204) {
                                    Toast.makeText(this@RegistrarFeedbackSchdule, result.Status, Toast.LENGTH_SHORT).show()
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
                        this@RegistrarFeedbackSchdule,
                        R.layout.support_simple_spinner_dropdown_item,
                        courselist
                    )
                    spinner_courselist.adapter = usercourselistadp
                }catch (ex: Exception) {
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@RegistrarFeedbackSchdule,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }
            else {
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
                try{
                    selectedcourselist = p0!!.getItemAtPosition(p2) as String
                    deptlist.clear()
                    mServices.GetInstituteData()
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 204) {
                                    Toast.makeText(this@RegistrarFeedbackSchdule, result.Status, Toast.LENGTH_SHORT).show()
                                } else {
                                    val listsinstz: Int = result.Data6!!.size
                                    for (i in 0..listsinstz - 1) {
                                        if (result.Data6!![i].Course_Institute == selectedInstituteName) {
                                            val listscoursez: Int = result.Data6!![i].Courses!!.size
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
                    deptlist.add("All")
                    var userdeptlistadp: ArrayAdapter<String> =
                        ArrayAdapter<String>(
                            this@RegistrarFeedbackSchdule,
                            R.layout.support_simple_spinner_dropdown_item, deptlist
                        )
                    spinner_deptlist.adapter = userdeptlistadp
                }catch (ex: Exception) {
                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@RegistrarFeedbackSchdule,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }
            else {
                GenericUserFunction.showInternetNegativePopUp(
                    this@RegistrarFeedbackSchdule,
                    getString(R.string.failureNoInternetErr)
                )
            }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spinner_deptlist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (InternetConnection.checkConnection(this@RegistrarFeedbackSchdule)) {
                    try {
                        selecteddeptlist = p0!!.getItemAtPosition(p2) as String
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

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@RegistrarFeedbackSchdule,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                }
                else {
                    GenericUserFunction.showInternetNegativePopUp(
                        this@RegistrarFeedbackSchdule,
                        getString(R.string.failureNoInternetErr)
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_YearDept.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                selecteddeptYear=spinner_YearDept.selectedItem.toString()

                println("yearSelected  "+selecteddeptYear)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        btn_ScheduleFeedback.setOnClickListener {
            if (selectedFeedbackName.equals("Select Feedback Type")) {
                showToast("Please select type of Feedback")
                return@setOnClickListener
            }
            if (select_date!!.text.isEmpty()) {
                to_date!!.error = "Please select feedback schedule date"
                return@setOnClickListener
            }

            if (to_date!!.text.isEmpty()) {
                to_date!!.error = "Please select to date"
                return@setOnClickListener
            }

            if (from_date!!.text.isEmpty()) {
                to_date!!.error = "Please select from date"
                return@setOnClickListener
            }

            if (selectedInstituteName.equals("Select institute")) {
                showToast("Please select Institute")
                return@setOnClickListener
            }

            if (spinner_YearDept.selectedItem.toString().equals("Select Year")) {
                showToast("Please select Year")
                return@setOnClickListener
            }
            if (InternetConnection.checkConnection(this)) {
            try{
                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                var call: Call<FeedBackInsert> = phpApiInterface.InsertFeedBackScheduler(selectedInstituteName,selectedcourselist!!,selecteddeptlist,selectedFeedbackName,select_date!!.text.toString(),from_date_conv.toString(),to_date_conv.toString(),selecteddeptYear)
                call.enqueue(object : Callback<FeedBackInsert> {
                    override fun onFailure(call: Call<FeedBackInsert>, t: Throwable) {
                        Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT).show()
                    }
                    override fun onResponse(call: Call<FeedBackInsert>, response: Response<FeedBackInsert>) {
                        val result: FeedBackInsert? = response.body()
                        GenericUserFunction.showPositivePopUp(this@RegistrarFeedbackSchdule,result!!.Response)


                    }

                })
            }catch (ex: Exception) {
                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
            sendNotice()
            }
            else
            {
                GenericUserFunction.showInternetNegativePopUp(
                    this,
                    getString(R.string.failureNoInternetErr))
            }
        }

        spinner_FeedbackType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long)
            {
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
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                select_date!!.text = sdf.format(date).toString()
                //Toast.makeText(this, """$dayOfMonth-${monthOfYear + 1}-$year""", Toast.LENGTH_LONG).show()
            }, year, month, day
        )
        dpd.show()
    }
    fun clickToDataPicker(view: View)
    {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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


            }, year, month, day
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
            R.style.AppTheme4, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
                from_date_conv = sdf.format(date3).toString()



                val myFormat5 = "dd-MM-yyyy" // mention the format you need
                val sdf5= SimpleDateFormat(myFormat5, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date5 = cal.time
                sdf5.format(date5)
                from_date_d = sdf5.format(date5).toString()




            }, year, month, day
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

        try {
            mServices.UploadNotice(
                from_date_d.toString(),
                "Feedback Scheduled",
                feedbackSche1 + from_date_d + " to " + to_date_d,
                selectedInstituteName,
                "All",
                "All",
                "Administrative",
                "Student",
                "F",
                "EXAMINCHARGE",
                "61",
                "-",
                "All",
                "All",
                "T",
                "F",
                "T"
            ).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@RegistrarFeedbackSchdule, t.message, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    // val result: APIResponse? = response.body()

                    Toast.makeText(
                        this@RegistrarFeedbackSchdule,
                        "Notice Send Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                applicationContext,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }
        else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }

    }

}
