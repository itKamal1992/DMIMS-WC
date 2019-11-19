package com.dmims.dmims.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mServices
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.showToast
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.DeptListStudDataRef
import com.dmims.dmims.model.NewUserInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewRegistration : AppCompatActivity() {
    private lateinit var edit_Name: EditText
    private lateinit var edit_Email: EditText
    private lateinit var edit_Mob: EditText
    private lateinit var edit_RollNo: EditText
    private lateinit var edit_EnrolNo: EditText
    private lateinit var edit_Pass: EditText

    private lateinit var spinner_institue: Spinner
    private lateinit var spinner_courselist: Spinner
    private lateinit var spinner_Sem: Spinner
    private lateinit var btn_Update: Button


    private lateinit var selectedInstituteName: String
    private lateinit var selectedcourselist: String
    private lateinit var selectedsem: String
    private lateinit var CourseID: String


//    selectedInstituteName selectedcourselist selectedsem

    var listsinstz: Int = 0
    var instituteName1 = ArrayList<String>()
    var courselist = ArrayList<String>()
    var semlist = ArrayList<String>()
    var FEEDBACK_DATE: String = "-"
    private var InstituteList: java.util.ArrayList<DeptListStudDataRef>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_registration)
//        edit_Name edit_Email edit_Mob edit_RollNo  edit_EnrolNo
//        spinner_institue spinner_courselist spinner_Sem

        val myFormat = "dd-MM-yyyy" // mention the format you need
        var cal = Calendar.getInstance()
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        FEEDBACK_DATE = sdf.format(cal.time)

        edit_Name = findViewById(R.id.edit_Name)
        edit_Email = findViewById(R.id.edit_Email)
        edit_Mob = findViewById(R.id.edit_Mob)
        edit_RollNo = findViewById(R.id.edit_RollNo)
        edit_EnrolNo = findViewById(R.id.edit_EnrolNo)
        edit_Pass = findViewById(R.id.edit_Pass)
        spinner_institue = findViewById(R.id.spinner_institue)
        spinner_courselist = findViewById(R.id.spinner_courselist)
        spinner_Sem = findViewById(R.id.spinner_Sem)
        btn_Update = findViewById(R.id.btn_Update)
        try {
            instituteName1.add("Select Institute")
            semlist.add("Select Semester")
            semlist.add("01")
            semlist.add("02")
            semlist.add("03")
            semlist.add("04")
            semlist.add("05")
            semlist.add("06")
            semlist.add("07")
            semlist.add("08")
            semlist.add("09")
            semlist.add("No Semester Pattern")

            mServices.GetInstituteData()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        showToast(t.message!!)
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            showToast(result.Status)
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
                    this@NewRegistration,
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
        spinner_institue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {
                    selectedInstituteName = p0!!.getItemAtPosition(p2) as String
                    courselist.clear()
                    mServices.GetInstituteData()
                        .enqueue(object : Callback<APIResponse> {
                            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                                showToast(t.message!!)
                            }

                            override fun onResponse(
                                call: Call<APIResponse>,
                                response: Response<APIResponse>
                            ) {
                                val result: APIResponse? = response.body()
                                if (result!!.Responsecode == 204) {
                                    showToast(result.Status)
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
                    courselist.add("Select Course")
                    var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@NewRegistration,
                        R.layout.support_simple_spinner_dropdown_item,
                        courselist
                    )
                    spinner_courselist.adapter = usercourselistadp
                } catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@NewRegistration,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_courselist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedcourselist = p0!!.getItemAtPosition(p2) as String

                if (selectedcourselist.equals("Select Course")) {

                } else {
                    try {

                        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                            PhpApiInterface::class.java
                        )
                        var call3: Call<DeptListStudData> =
                            phpApiInterface.IGetData(selectedInstituteName, selectedcourselist)
                        call3.enqueue(object : Callback<DeptListStudData> {
                            override fun onFailure(call: Call<DeptListStudData>, t: Throwable) {
                                Toast.makeText(this@NewRegistration, t.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<DeptListStudData>,
                                response: Response<DeptListStudData>
                            ) {
                                var users = java.util.ArrayList<FeedBackDataC>()

                                if (response.isSuccessful) {
                                    users.clear()
                                    InstituteList = response.body()!!.Data
                                    if (InstituteList!![0].ID == "error") {
                                        Toast.makeText(
                                            this@NewRegistration,
                                            "No Data in Department Master.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        users.clear()
                                        var listSize = InstituteList!!.size
                                        for (i in 0..listSize - 1) {
                                            CourseID = InstituteList!![i].COURSE_ID

                                        }


                                    }
                                }
                            }
                        })


                        var userdeptlistadp: ArrayAdapter<String> =
                            ArrayAdapter<String>(
                                this@NewRegistration,
                                R.layout.support_simple_spinner_dropdown_item, semlist
                            )
                        spinner_Sem.adapter = userdeptlistadp
                    } catch (ex: Exception) {

                        ex.printStackTrace()
                        GenericUserFunction.showApiError(
                            this@NewRegistration,
                            "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        spinner_Sem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedsem = p0!!.getItemAtPosition(p2) as String

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btn_Update.setOnClickListener {

            if (edit_Name.text.trim().isEmpty()) {
              showToast("Please give your Name")
                return@setOnClickListener
            } else
                if (edit_Email.text.trim().isEmpty()) {
                    showToast("Please give your E-mail")
                    return@setOnClickListener
                } else

                    if (edit_Mob.text.trim().isEmpty()) {
                       showToast("Please give your Mobile No")
                        return@setOnClickListener
                    } else if (edit_Pass.text.trim().isEmpty()) {
                        showToast("Please give your your Password")
                        return@setOnClickListener
                    } else
                        if (edit_RollNo.text.trim().isEmpty()) {
                            showToast( "Please give your Roll No")
                            return@setOnClickListener
                        } else
                            if (selectedInstituteName.equals("Select Institute")) {
                                showToast(
                                    "Please select your Institute "
                                )
                                return@setOnClickListener
                            } else
                                if (selectedcourselist.equals("Select Course")) {
                                    showToast("Please select your Course")
                                    return@setOnClickListener
                                } else
                                    if (selectedsem.equals("Select Semester")) {
                                        showToast("Please select your Semester")
                                        return@setOnClickListener
                                    } else {
                                        try {
                                            mServices.StudentSearchByRollNo(
                                                edit_RollNo.text.toString().trim(),
                                                CourseID
                                            )
                                                .enqueue(object : Callback<APIResponse> {
                                                    override fun onFailure(
                                                        call: Call<APIResponse>,
                                                        t: Throwable
                                                    ) {
                                                        Toast.makeText(
                                                            this@NewRegistration,
                                                            t.message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                    }

                                                    override fun onResponse(
                                                        call: Call<APIResponse>,
                                                        response: Response<APIResponse>
                                                    ) {
                                                        val result: APIResponse? = response.body()
                                                        if (result!!.Status == "ok") {
                                                            result.Data16!!.course_id
                                                            ////Add this field to api call edit_Pass

                                                            var phpApiInterface: PhpApiInterface =
                                                                ApiClientPhp.getClient()
                                                                    .create(PhpApiInterface::class.java)
                                                            var call3: Call<NewUserInsert> =
                                                                phpApiInterface.NewRegisteration(
                                                                    result.Data16!!.student_name,
                                                                    edit_Email.text.toString().trim(),
                                                                    edit_Mob.text.toString().trim(),
                                                                    edit_RollNo.text.toString().trim(),
                                                                    result.Data16!!.EnrolNo,
                                                                    result.Data16!!.course_institute,
                                                                    selectedcourselist,
                                                                    edit_Pass.text.toString().trim(),
                                                                    result.Data16!!.student_id,
                                                                    result.Data16!!.course_id,
                                                                    result.Data16!!.sem_id,
                                                                    FEEDBACK_DATE
                                                                )




                                                            call3.enqueue(object :
                                                                Callback<NewUserInsert> {
                                                                override fun onFailure(
                                                                    call: Call<NewUserInsert>,
                                                                    t: Throwable
                                                                ) {
                                                                    Toast.makeText(
                                                                        this@NewRegistration,
                                                                        t.message,
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }

                                                                override fun onResponse(
                                                                    call: Call<NewUserInsert>,
                                                                    response: Response<NewUserInsert>
                                                                ) {
                                                                    val result3: NewUserInsert? =
                                                                        response.body()
                                                                    Toast.makeText(
                                                                        this@NewRegistration,
                                                                        result3!!.response,
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                    GenericUserFunction.showPositivePopUp(
                                                                        this@NewRegistration,
                                                                        "Congratulation you have registered successfully. Please login with mobile no."
                                                                    )
                                                                }

                                                            })


                                                        }
                                                    }
                                                }
                                                )
                                        } catch (ex: Exception) {

                                            ex.printStackTrace()
                                            GenericUserFunction.showApiError(
                                                this@NewRegistration,
                                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                            )
                                        }


                                    }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
