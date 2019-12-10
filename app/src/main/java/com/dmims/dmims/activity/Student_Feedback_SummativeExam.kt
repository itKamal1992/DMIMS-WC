package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.dmims.dmims.ExamFeedBack.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.dataclass.FeedBackDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.DeptListStudData
import com.dmims.dmims.model.DeptListStudDataRef
import com.dmims.dmims.model.FeedBackInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.student__feedback__summative_exam.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Student_Feedback_SummativeExam : AppCompatActivity() {
    lateinit var spinner_SummaExamination: Spinner
    lateinit var spinner_SummaYear: Spinner
    lateinit var Et_summdesc: EditText

    lateinit var rg_SummAQ1: RadioGroup
    lateinit var rg_SummAQ2: RadioGroup
    lateinit var rg_SummAQ3: RadioGroup
    lateinit var rg_SummAQ4: RadioGroup

    lateinit var rg_SummBQ1: RadioGroup
    lateinit var rg_SummBQ2: RadioGroup
    lateinit var rg_SummBQ3: RadioGroup
    lateinit var rg_SummBQ4: RadioGroup
    lateinit var rg_SummBQ5: RadioGroup

    lateinit var rg_SummCQ1: RadioGroup
    lateinit var rg_SummCQ2: RadioGroup
    lateinit var rg_SummCQ3: RadioGroup
    lateinit var rg_SummCQ4: RadioGroup

    lateinit var rg_SummDQ1: RadioGroup
    lateinit var rg_SummDQ2: RadioGroup
    lateinit var rg_SummDQ3: RadioGroup
    lateinit var rg_SummDQ4: RadioGroup
    lateinit var rg_SummDQ5: RadioGroup
    lateinit var rg_SummDQ6: RadioGroup

    lateinit var str_summNameFaculty: String
    lateinit var str_summExamination: String
    lateinit var str_summYear: String

    lateinit var str_SummAQ1: String
    lateinit var str_SummAQ2: String
    lateinit var str_SummAQ3: String
    lateinit var str_SummAQ4: String

    lateinit var str_SummBQ1: String
    lateinit var str_SummBQ2: String
    lateinit var str_SummBQ3: String
    lateinit var str_SummBQ4: String
    lateinit var str_SummBQ5: String

    lateinit var str_SummCQ1: String
    lateinit var str_SummCQ2: String
    lateinit var str_SummCQ3: String
    lateinit var str_SummCQ4: String

    lateinit var str_SummDQ1: String
    lateinit var str_SummDQ2: String
    lateinit var str_SummDQ3: String
    lateinit var str_SummDQ4: String
    lateinit var str_SummDQ5: String
    lateinit var str_SummDQ6: String

    lateinit var str_etAQ1: String
    lateinit var str_etAQ2: String
    lateinit var str_etAQ3: String
    lateinit var str_etAQ4: String

    lateinit var str_etBQ1: String
    lateinit var str_etBQ2: String
    lateinit var str_etBQ3: String
    lateinit var str_etBQ4: String
    lateinit var str_etBQ5: String

    lateinit var str_etCQ1: String
    lateinit var str_etCQ2: String
    lateinit var str_etCQ3: String
    lateinit var str_etCQ4: String

    lateinit var str_etDQ1: String
    lateinit var str_etDQ2: String
    lateinit var str_etDQ3: String
    lateinit var str_etDQ4: String
    lateinit var str_etDQ5: String
    lateinit var str_etDQ6: String

    private lateinit var institute_name: String

    lateinit var btnSubmit: Button

    var COURSE_ID: String = "-"
//    var stud_kstr: String = "-"
//    var stud_namestr: String = "-"
//    var stud_k: Int = 0

    var array = intArrayOf(1, 99, 10000, 84849, 111, 212, 314, 21, 442, 455, 244, 554, 22, 22, 211)
    lateinit var a: Array<String>
    lateinit var c: Array<String>


    lateinit var Course_ID: String
    lateinit var Stud_ID: String
    lateinit var Stud_Name: String
    lateinit var Stud_Roll_No: String
    lateinit var Stud_Institute: String
    lateinit var CurrentDate: String

    lateinit var Course: String
    lateinit var Institute: String
    lateinit var Et_Str_summdesc: String

    private var Deptlist: ArrayList<DeptListStudDataRef>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student__feedback__summative_exam)


        var cal = Calendar.getInstance()
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        CurrentDate = sdf.format(cal.time).toString()

        //  println("subSumm  "+" "+array.size+"  a "+a[1])
        et_Summative_PreexamQ1.visibility = View.GONE
        et_Summative_PreexamQ2.visibility = View.GONE
        et_Summative_PreexamQ3.visibility = View.GONE
        et_Summative_PreexamQ4.visibility = View.GONE

        et_Summative_structuringpaperQ1.visibility = View.GONE
        et_Summative_structuringpaperQ2.visibility = View.GONE
        et_Summative_structuringpaperQ3.visibility = View.GONE
        et_Summative_structuringpaperQ4.visibility = View.GONE
        et_Summative_structuringpaperQ5.visibility = View.GONE

        et_Summative_customizedasQ1.visibility = View.GONE
        et_Summative_customizedasQ2.visibility = View.GONE
        et_Summative_customizedasQ3.visibility = View.GONE
        et_Summative_customizedasQ4.visibility = View.GONE

        et_Summative_conductPractiexamQ1.visibility = View.GONE
        et_Summative_conductPractiexamQ2.visibility = View.GONE
        et_Summative_conductPractiexamQ3.visibility = View.GONE
        et_Summative_conductPractiexamQ4.visibility = View.GONE
        et_Summative_conductPractiexamQ5.visibility = View.GONE
        et_Summative_conductPractiexamQ6.visibility = View.GONE



        Et_summdesc=findViewById(R.id.et_sumdescdetails)
        Et_Str_summdesc=Et_summdesc.text.toString()

        rg_SummAQ1 = findViewById(R.id.rg_Summative_PreexamQ1)
        rg_SummAQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)



            str_SummAQ1 = radio.text.toString()



            println("Text 1 " + str_SummAQ1)
            if (radio.text.equals("No")) {
                et_Summative_PreexamQ1.visibility = View.VISIBLE
            } else {
                et_Summative_PreexamQ1.visibility = View.GONE
            }


        }




        rg_SummAQ2 = findViewById(R.id.rg_Summative_PreexamQ2)
        rg_SummAQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No")) {
                et_Summative_PreexamQ2.visibility = View.VISIBLE
            } else {
                et_Summative_PreexamQ2.visibility = View.GONE
            }

        }

        rg_SummAQ3 = findViewById(R.id.rg_Summative_PreexamQ3)
        rg_SummAQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No")) {
                et_Summative_PreexamQ3.visibility = View.VISIBLE
            } else {
                et_Summative_PreexamQ3.visibility = View.GONE
            }

        }

        rg_SummAQ4 = findViewById(R.id.rg_Summative_PreexamQ4)
        rg_SummAQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)


            if (radio.text.equals("No")) {
                et_Summative_PreexamQ4.visibility = View.VISIBLE
            } else {
                et_Summative_PreexamQ4.visibility = View.GONE
            }

        }

        rg_SummBQ1 = findViewById(R.id.rg_Summative_structuringpaperQ1)
        rg_SummBQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_structuringpaperQ1.visibility = View.VISIBLE
            } else {
                et_Summative_structuringpaperQ1.visibility = View.GONE
            }

        }


        rg_SummBQ2 = findViewById(R.id.rg_Summative_structuringpaperQ2)
        rg_SummBQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_structuringpaperQ2.visibility = View.VISIBLE
            } else {
                et_Summative_structuringpaperQ2.visibility = View.GONE
            }

        }

        rg_SummBQ3 = findViewById(R.id.rg_Summative_structuringpaperQ3)
        rg_SummBQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_structuringpaperQ3.visibility = View.VISIBLE
            } else {
                et_Summative_structuringpaperQ3.visibility = View.GONE
            }

        }

        rg_SummBQ4 = findViewById(R.id.rg_Summative_structuringpaperQ4)
        rg_SummBQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_structuringpaperQ4.visibility = View.VISIBLE
            } else {
                et_Summative_structuringpaperQ4.visibility = View.GONE
            }

        }


        rg_SummBQ5 = findViewById(R.id.rg_Summative_structuringpaperQ5)
        rg_SummBQ5.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_structuringpaperQ5.visibility = View.VISIBLE
            } else {
                et_Summative_structuringpaperQ5.visibility = View.GONE
            }

        }

        rg_SummCQ1 = findViewById(R.id.rg_Summative_customizedasQ1)
        rg_SummCQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)

            if (radio.text.equals("No")) {
                et_Summative_customizedasQ1.visibility = View.VISIBLE
            } else {
                et_Summative_customizedasQ1.visibility = View.GONE
            }

        }

        rg_SummCQ2 = findViewById(R.id.rg_Summative_customizedasQ2)
        rg_SummCQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_customizedasQ2.visibility = View.VISIBLE
            } else {
                et_Summative_customizedasQ2.visibility = View.GONE
            }

        }

        rg_SummCQ3 = findViewById(R.id.rg_Summative_customizedasQ3)
        rg_SummCQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_customizedasQ3.visibility = View.VISIBLE
            } else {
                et_Summative_customizedasQ3.visibility = View.GONE
            }

        }

        rg_SummCQ4 = findViewById(R.id.rg_Summative_customizedasQ4)
        rg_SummCQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_customizedasQ4.visibility = View.VISIBLE
            } else {
                et_Summative_customizedasQ4.visibility = View.GONE
            }

        }


        rg_SummDQ1 = findViewById(R.id.rg_Summative_conductPractiexamQ1)
        rg_SummDQ1.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ1.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ1.visibility = View.GONE
            }

        }


        rg_SummDQ2 = findViewById(R.id.rg_Summative_conductPractiexamQ2)
        rg_SummDQ2.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ2.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ2.visibility = View.GONE
            }

        }

        rg_SummDQ3 = findViewById(R.id.rg_Summative_conductPractiexamQ3)
        rg_SummDQ3.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ3.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ3.visibility = View.GONE
            }

        }

        rg_SummDQ4 = findViewById(R.id.rg_Summative_conductPractiexamQ4)
        rg_SummDQ4.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ4.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ4.visibility = View.GONE
            }

        }


        rg_SummDQ5 = findViewById(R.id.rg_Summative_conductPractiexamQ5)
        rg_SummDQ5.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ5.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ5.visibility = View.GONE
            }

        }



        rg_SummDQ6 = findViewById(R.id.rg_Summative_conductPractiexamQ6)
        rg_SummDQ6.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals("No")) {
                et_Summative_conductPractiexamQ6.visibility = View.VISIBLE
            } else {
                et_Summative_conductPractiexamQ6.visibility = View.GONE
            }

        }


        spinner_SummaExamination = findViewById(R.id.spinner_examsummativeexam)
        spinner_SummaYear = findViewById(R.id.spinner_yearsummativeexam)


        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        Course_ID = mypref.getString("course_id", null)!!
        Stud_ID = mypref.getString("Stud_id_key", null)!!
        Stud_Name = mypref.getString("key_drawer_title", null)!!
        Stud_Roll_No = mypref.getString("roll_no", null)!!
        Stud_Institute = mypref.getString("key_institute_stud", null)!!

        when (Stud_Institute) {
            "JNMC" -> str_summNameFaculty = "Medicine"
            "SPDC" -> str_summNameFaculty = "Dental"
            "MGAC" -> str_summNameFaculty = "Ayurveda"
            "SRMMCON" -> str_summNameFaculty = "Nursing"
            "RNPC" -> str_summNameFaculty = "Physiotherapy"
            "Inter Disciplinary" -> str_summNameFaculty = "Inter Disciplinary"

        }
        txt_faculty.text = str_summNameFaculty

        if (InternetConnection.checkConnection(this)) {
            var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                PhpApiInterface::class.java
            )
            var call3: Call<DeptListStudData> = phpApiInterface.InstDetailsStudYear(Course_ID)
            call3.enqueue(object : Callback<DeptListStudData> {
                override fun onFailure(call: Call<DeptListStudData>, t: Throwable) {
                    Toast.makeText(
                        this@Student_Feedback_SummativeExam,
                        t.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onResponse(
                    call: Call<DeptListStudData>,
                    response: Response<DeptListStudData>
                ) {
                    var users = ArrayList<FeedBackDataC>()

                    if (response.isSuccessful) {
                        users.clear()
                        Deptlist = response.body()!!.Data
                        if (Deptlist!!.size > 0)
                            Institute = Deptlist!![0].COURSE_INSTITUTE
                        Course = Deptlist!![0].COURSE_NAME

                    }

                }


            })

        } else {
            GenericUserFunction.showInternetNegativePopUp(
                this,
                getString(R.string.failureNoInternetErr)
            )
        }
        btnSubmit = findViewById(R.id.btn_summFeedSubmit)
        btnSubmit.setOnClickListener {

            str_summExamination = spinner_SummaExamination.selectedItem.toString()
            str_summYear = spinner_SummaYear.selectedItem.toString()



            println("str_summNameSAculty " + str_summNameFaculty + " " + str_summExamination + " " + str_summYear)


            //    GenericPublicVariable.CustDialog.setCancelable(false)


            if (str_summNameFaculty.equals("Select Faculty")) {
                Toast.makeText(this, "Select Faculty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (str_summExamination.equals("Select Examination")) {
                Toast.makeText(this, "Select exam", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (str_summYear.equals("Select Year")) {
                Toast.makeText(this, "Select year", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
//                COURSE_ID = mypref.getString("course_id", null)
//                stud_kstr = mypref.getString("Stud_id_key", null)
//                stud_namestr = mypref.getString("key_drawer_title", null)
//                stud_k = Integer.parseInt(stud_kstr)

                str_summExamination = spinner_SummaExamination.selectedItem.toString()
                str_summYear = spinner_SummaYear.selectedItem.toString()

                var selectedAQ1 = rg_SummAQ1.checkedRadioButtonId
                str_SummAQ1 = findViewById<RadioButton>(selectedAQ1).text.toString()
                str_etAQ1 = findViewById<EditText>(R.id.et_Summative_PreexamQ1).text.toString()
                if (str_etAQ1.equals("")) {
                    str_etAQ1 = "-"
                }
                var selectedAQ2 = rg_SummAQ2.checkedRadioButtonId
                str_SummAQ2 = findViewById<RadioButton>(selectedAQ2).text.toString()
                str_etAQ2 = findViewById<EditText>(R.id.et_Summative_PreexamQ2).text.toString()
                if (str_etAQ2.equals("")) {
                    str_etAQ2 = "-"
                }
                var selectedAQ3 = rg_SummAQ3.checkedRadioButtonId
                str_SummAQ3 = findViewById<RadioButton>(selectedAQ3).text.toString()
                str_etAQ3 = findViewById<EditText>(R.id.et_Summative_PreexamQ3).text.toString()
                if (str_etAQ3.equals("")) {
                    str_etAQ3 = "-"
                }
                var selectedAQ4 = rg_SummAQ4.checkedRadioButtonId
                str_SummAQ4 = findViewById<RadioButton>(selectedAQ4).text.toString()
                str_etAQ4 = findViewById<EditText>(R.id.et_Summative_PreexamQ4).text.toString()
                if (str_etAQ4.equals("")) {
                    str_etAQ4 = "-"
                }


                var selectedBQ1 = rg_SummBQ1.checkedRadioButtonId
                str_SummBQ1 = findViewById<RadioButton>(selectedBQ1).text.toString()
                str_etBQ1 =
                    findViewById<EditText>(R.id.et_Summative_structuringpaperQ1).text.toString()
                if (str_etBQ1.equals("")) {
                    str_etBQ1 = "-"
                }
                var selectedBQ2 = rg_SummBQ2.checkedRadioButtonId
                str_SummBQ2 = findViewById<RadioButton>(selectedBQ2).text.toString()
                str_etBQ2 =
                    findViewById<EditText>(R.id.et_Summative_structuringpaperQ2).text.toString()
                if (str_etBQ2.equals("")) {
                    str_etBQ2 = "-"
                }
                var selectedBQ3 = rg_SummBQ3.checkedRadioButtonId
                str_SummBQ3 = findViewById<RadioButton>(selectedBQ3).text.toString()
                str_etBQ3 =
                    findViewById<EditText>(R.id.et_Summative_structuringpaperQ3).text.toString()
                if (str_etBQ3.equals("")) {
                    str_etBQ3 = "-"
                }
                var selectedBQ4 = rg_SummBQ4.checkedRadioButtonId
                str_SummBQ4 = findViewById<RadioButton>(selectedBQ4).text.toString()
                str_etBQ4 =
                    findViewById<EditText>(R.id.et_Summative_structuringpaperQ4).text.toString()
                if (str_etBQ4.equals("")) {
                    str_etBQ4 = "-"
                }
                var selectedBQ5 = rg_SummBQ5.checkedRadioButtonId
                str_SummBQ5 = findViewById<RadioButton>(selectedBQ5).text.toString()
                str_etBQ5 =
                    findViewById<EditText>(R.id.et_Summative_structuringpaperQ5).text.toString()
                if (str_etBQ5.equals("")) {
                    str_etBQ5 = "-"
                }


                var selectedCQ1 = rg_SummCQ1.checkedRadioButtonId
                str_SummCQ1 = findViewById<RadioButton>(selectedCQ1).text.toString()
                str_etCQ1 = findViewById<EditText>(R.id.et_Summative_customizedasQ1).text.toString()
                if (str_etCQ1.equals("")) {
                    str_etCQ1 = "-"
                }
                var selectedCQ2 = rg_SummCQ2.checkedRadioButtonId
                str_SummCQ2 = findViewById<RadioButton>(selectedCQ2).text.toString()
                str_etCQ2 = findViewById<EditText>(R.id.et_Summative_customizedasQ2).text.toString()
                if (str_etCQ2.equals("")) {
                    str_etCQ2 = "-"
                }
                var selectedCQ3 = rg_SummCQ3.checkedRadioButtonId
                str_SummCQ3 = findViewById<RadioButton>(selectedCQ3).text.toString()
                str_etCQ3 = findViewById<EditText>(R.id.et_Summative_customizedasQ3).text.toString()
                if (str_etCQ3.equals("")) {
                    str_etCQ3 = "-"
                }
                var selectedCQ4 = rg_SummCQ4.checkedRadioButtonId
                str_SummCQ4 = findViewById<RadioButton>(selectedCQ4).text.toString()
                str_etCQ4 = findViewById<EditText>(R.id.et_Summative_customizedasQ4).text.toString()
                if (str_etCQ4.equals("")) {
                    str_etCQ4 = "-"
                }

                var selectedDQ1 = rg_SummDQ1.checkedRadioButtonId
                str_SummDQ1 = findViewById<RadioButton>(selectedDQ1).text.toString()
                str_etDQ1 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ1).text.toString()
                if (str_etDQ1.equals("")) {
                    str_etDQ1 = "-"
                }
                var selectedDQ2 = rg_SummDQ2.checkedRadioButtonId
                str_SummDQ2 = findViewById<RadioButton>(selectedDQ2).text.toString()
                str_etDQ2 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ2).text.toString()
                if (str_etDQ2.equals("")) {
                    str_etDQ2 = "-"
                }
                var selectedDQ3 = rg_SummDQ3.checkedRadioButtonId
                str_SummDQ3 = findViewById<RadioButton>(selectedDQ3).text.toString()
                str_etDQ3 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ3).text.toString()
                if (str_etDQ3.equals("")) {
                    str_etDQ3 = "-"
                }
                var selectedDQ4 = rg_SummDQ4.checkedRadioButtonId
                str_SummDQ4 = findViewById<RadioButton>(selectedDQ4).text.toString()
                str_etDQ4 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ4).text.toString()
                if (str_etDQ4.equals("")) {
                    str_etDQ4 = "-"
                }
                var selectedDQ5 = rg_SummDQ5.checkedRadioButtonId
                str_SummDQ5 = findViewById<RadioButton>(selectedDQ5).text.toString()
                str_etDQ5 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ5).text.toString()
                if (str_etDQ5.equals("")) {
                    str_etDQ5 = "-"
                }
                var selectedDQ6 = rg_SummDQ6.checkedRadioButtonId
                str_SummDQ6 = findViewById<RadioButton>(selectedDQ6).text.toString()
                str_etDQ6 =
                    findViewById<EditText>(R.id.et_Summative_conductPractiexamQ6).text.toString()
                if (str_etDQ6.equals("")) {
                    str_etDQ6 = "-"
                }


               if (Et_summdesc.text.toString().equals("")) {
                    Et_Str_summdesc=""
                }else
               {
                   Et_Str_summdesc=Et_summdesc.text.toString()
               }


//
//                println(
//                    "here text q1 " + str_summNameFaculty + " n " + str_summExamination + " n " + str_summYear + " n " + str_etAQ1 + "/" + str_etAQ2 + "/" + str_etAQ3 + "/" + str_etAQ4 + "/" + str_etBQ1 + "/" + str_etBQ2 + "/" + str_etBQ3 + "/" + str_etBQ4 + "/" + str_etBQ5 + "/"
//                            + str_etCQ1 + "/" + str_etCQ2 + "/" + str_etCQ3 + "/" + str_etCQ4 + "/" + str_etDQ1 + "/" + str_etDQ2 + "/" + str_etDQ3 + "/" + str_etDQ4 + "/" + str_etDQ5 + "/" + str_etDQ6
//                )
//                println(
//                    "here q1" + str_SummAQ1 + "-" + str_SummAQ2 + "-" + str_SummAQ3 + "-" + str_SummAQ4 + "-" + str_SummBQ1 + "-" + str_SummBQ2 + "-" + str_SummBQ3 + "-" + str_SummBQ4 + "-" + str_SummBQ5 + "-"
//                            + str_SummCQ1 + "-" + str_SummCQ2 + "-" + str_SummCQ3 + "-" + str_SummCQ4 + "-" + str_SummDQ1 + "-" + str_SummDQ2 + "-" + str_SummDQ3 + "-" + str_SummDQ4 + "-" + str_SummDQ5 + "-" + str_SummDQ6 + "-"
//                )

                var CustDialog = Dialog(this)
                CustDialog.setContentView(R.layout.dialog_question_yes_no_custom_popup)
                var ivNegClose1: ImageView =
                    CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
                var btnOk: Button = CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                var btnCustomDialogCancel: Button =
                    CustDialog.findViewById(R.id.btnCustomDialogCancel) as Button
                var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                tvMsg.text = "Do you want to Submit your Feedback"
//    GenericPublicVariable.CustDialog.setCancelable(false)
                btnOk.setOnClickListener {
                    if (InternetConnection.checkConnection(this)) {
                        var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                            PhpApiInterface::class.java
                        )
                        var call3: Call<DeptListStudData> =
                            phpApiInterface.InstDetailsStudYear(Course_ID)
                        call3.enqueue(object : Callback<DeptListStudData> {
                            override fun onFailure(call: Call<DeptListStudData>, t: Throwable) {
                                Toast.makeText(
                                    this@Student_Feedback_SummativeExam,
                                    t.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<DeptListStudData>,
                                response: Response<DeptListStudData>
                            ) {
                                CustDialog.dismiss()

                                var users = ArrayList<DeptListStudDataRef>()
                                if (response.isSuccessful) {
                                    users.clear()
                                    users = response.body()!!.Data!!
                                    if (users!!.size > 0) {
                                        Institute = users!![0].COURSE_INSTITUTE
                                        Course = users!![0].COURSE_NAME
                                        Update()
                                    }

                                }


                            }

                        })
                    } else {
                        CustDialog.dismiss()
                        GenericUserFunction.showInternetNegativePopUp(
                            this,
                            getString(R.string.failureNoInternetErr)
                        )
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

            }


        }


    }

    fun Update() {

//        Toast.makeText(this, "Update called ", Toast.LENGTH_SHORT).show()


        println(
            "FeedbackType FormativeFeedback" +
                    "\n SAculty " + str_summNameFaculty +
                    "\n Course_ID " + Course_ID +
                    "\n Stud_ID " + Stud_ID +
                    "\n Stud_Name " + Stud_Name +
                    "\n Stud_Stud_Roll_No " + Stud_Roll_No +
                    "\n Course " + Course +
                    "\n Institute " + Institute +
                    "\n str_summExamination " + str_summExamination +
                    "\n str_summYear " + str_summYear +
                    "\n str_etAQ1 " + str_etAQ1 +
                    "\n str_etAQ2 " + str_etAQ2 +
                    "\n str_etAQ3 " + str_etAQ3 +
                    "\n str_etAQ4 " + str_etAQ4 +

                    "\n str_etBQ1 " + str_etBQ1 +
                    "\n str_etBQ2 " + str_etBQ2 +
                    "\n str_etBQ3 " + str_etBQ3 +
                    "\n str_etBQ4 " + str_etBQ4 +
                    "\n str_etBQ5 " + str_etBQ5 +

                    "\n str_etCQ1 " + str_etCQ1 +
                    "\n str_etCQ2 " + str_etCQ2 +
                    "\n str_etCQ3 " + str_etCQ3 +
                    "\n str_etCQ4 " + str_etCQ4 +

                    "\n str_SummDQ1 " + str_SummDQ1 +
                    "\n str_SummDQ2 " + str_SummDQ2 +
                    "\n str_SummDQ2 " + str_SummDQ2 +
                    "\n str_SummDQ2 " + str_SummDQ2 +
                    "\n str_SummDQ2 " + str_SummDQ2 +
                    "\n str_SummDQ " + str_SummDQ2
        )


        var commonFeedBack = CommonFeedBack(
            "Summative",
            Course_ID, Stud_ID, Stud_Name, Stud_Roll_No, Course, Stud_Institute,
            CurrentDate.substring(6),

            Summative(
                str_summNameFaculty,
                str_summExamination,
                str_summYear,
                CurrentDate,
                Et_Str_summdesc,
                Feed_Sum_SectA(
                    str_SummAQ1,
                    str_etAQ1,
                    str_SummAQ2,
                    str_etAQ2,
                    str_SummAQ3,
                    str_etAQ3,
                    str_SummAQ4,
                    str_etAQ4
                ),
                Feed_Sum_SectB(
                    str_SummBQ1,
                    str_etBQ1,
                    str_SummBQ2,
                    str_etBQ2,
                    str_SummBQ3,
                    str_etBQ3,
                    str_SummBQ4,
                    str_etBQ4,
                    str_SummBQ5,
                    str_etBQ5

                ),
                Feed_Sum_SectC(
                    str_SummCQ1,
                    str_etCQ1,
                    str_SummCQ2,
                    str_etCQ2,
                    str_SummCQ3,
                    str_etCQ3,
                    str_SummCQ4,
                    str_etCQ4
                ),
                Feed_Sum_SectD(
                    str_SummDQ1,
                    str_etDQ1,
                    str_SummDQ2,
                    str_etDQ2,
                    str_SummDQ3,
                    str_etDQ3,
                    str_SummDQ4,
                    str_etDQ4,
                    str_SummDQ5,
                    str_etDQ5,
                    str_SummDQ6,
                    str_etDQ6
                )


            )
        )
        if (InternetConnection.checkConnection(this)) {
            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
            try {

                dialog.setMessage("Please Wait!!! \nFeedback getting stored")
                dialog.setCancelable(false)
                dialog.show()
//            Dialog End
                GenericPublicVariable.mServices.SubmitExamFeedback(commonFeedBack)
                    .enqueue(object : Callback<APIResponse> {
                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                            dialog.dismiss()
                            Toast.makeText(
                                this@Student_Feedback_SummativeExam,
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
                            println("Result >>> " + result)

//                                        Toast.makeText(this@InstituteNoticeBoard, result!!.Status, Toast.LENGTH_SHORT)
//                                            .show()
                            if (result!!.Responsecode == 200) {
                                GenericUserFunction.showPositivePopUp(
                                    this@Student_Feedback_SummativeExam,
                                    result!!.Status
                                )
                            } else {
                                GenericUserFunction.showApiError(
                                    this@Student_Feedback_SummativeExam,
                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                                )

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
    }


}
