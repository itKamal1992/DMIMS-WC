package com.dmims.dmims.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.model.FeedBackSchedule
import com.dmims.dmims.model.FeedBackScheduleField
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_exam_admin_notice_board.*
import kotlinx.android.synthetic.main.activity_registrar_feedback_schdule.*
import kotlinx.android.synthetic.main.activity_registrar_feedback_schdule.linear_year
import kotlinx.android.synthetic.main.activity_registrar_feedback_schdule.txt_year
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FeedbackOptionActivity : AppCompatActivity() {
    private lateinit var btn_survey_submit: Button
    private lateinit var txt_Note: TextView
    private lateinit var tv_endDate: TextView
    private lateinit var tv_startDate: TextView

    private lateinit var spinner_FeedbackType: Spinner
    private lateinit var spinner_FeedbackTypeYear: Spinner
    private lateinit var selected_Af_SS_Q1_Answer: String
    var feedbacknames = ArrayList<String>()
    var feedbacdsates = ArrayList<String>()
    var feedbacedates = ArrayList<String>()
    var yeararr = ArrayList<String>()
    var institutearr = ArrayList<String>()
    var coursearr = ArrayList<String>()



    var cal = Calendar.getInstance()
    var current_date: String = "-"
    var YearSelected: String = ""
    var year:String=""
    var nameOfInstitute:String=""

    var Feedback:String=""
    var Feedbackget:String=""


    lateinit var Course_ID: String
    lateinit var Stud_ID: String
    lateinit var Stud_Name: String
    lateinit var Stud_Roll_No: String
    lateinit var Stud_Institute: String
    lateinit var Course: String
    lateinit var Courseget: String
    lateinit var CurrentDate: String

    var studYearArray = arrayOf("1st, 2nd","Final MBBS Part 1","Final MBBS Part 2","All ( First to Final Year )")
    internal var mUserItems = java.util.ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_option)




        feedbacknames.add("Select Your Choice")
        feedbacdsates.add("Select Start Date")
        feedbacedates.add("Select End date")
        yeararr.add("year")
        coursearr.add("course")

        institutearr.add("institute")
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        current_date = sdf.format(cal.time)

        GetCurrentDate(current_date)

        txt_Note=findViewById(R.id.txt_Note)
        spinner_FeedbackType=findViewById<Spinner>(R.id.spinner_FeedbackType)
        spinner_FeedbackTypeYear=findViewById<Spinner>(R.id.spinner_FeedbackTypeYear)

        btn_survey_submit=findViewById<Button>(R.id.btn_survey_submit)
        tv_startDate=findViewById<TextView>(R.id.tv_startDate)
        tv_endDate=findViewById<TextView>(R.id.tv_endDate)



        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        Course_ID = mypref.getString("course_id", null)!!
        Stud_ID = mypref.getString("Stud_id_key", null)!!
        Stud_Name = mypref.getString("key_drawer_title", null)!!
        Stud_Roll_No = mypref.getString("roll_no", null)!!
        Stud_Institute = mypref.getString("key_institute_stud", null)!!
        Course = mypref.getString("key_course", null)!!





        if (Stud_Institute=="JNMC"){
            studYearArray = arrayOf("1st", "2nd", "Final MBBS Part 1","Final MBBS Part 2", "All ( First to Final Year )")
        }else{
            studYearArray = arrayOf("1st", "2nd", "3rd", "Final Year","All ( First to Final Year )")
        }
        var studentyearada: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, studYearArray)

        spinner_FeedbackTypeYear.adapter=studentyearada
       spinner_FeedbackTypeYear.selectedItem.toString()


        var Af_SS_Q1_AnswerAdap: ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, feedbacknames)
        spinner_FeedbackType.adapter = Af_SS_Q1_AnswerAdap
        spinner_FeedbackType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected_Af_SS_Q1_Answer = p0!!.getItemAtPosition(p2) as String
                if(p2 != 0) {
                    tv_startDate.text = feedbacdsates[p2]
                    tv_endDate.text = feedbacedates[p2]
                    year=yeararr[p2]
                    nameOfInstitute=institutearr[p2]
                    Courseget=coursearr[p2]
                   Feedbackget= feedbacknames[p2]



                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?)
            {
            }
        }






        /*   var checkedItems = BooleanArray(studYearArray.size)
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
   */













        btn_survey_submit.setOnClickListener {

            /*  val intent=Intent(this@FeedbackOptionActivity,FeedbackOptionType::class.java)
              startActivity(intent)
  */

        Feedback=spinner_FeedbackType.selectedItem.toString()
     YearSelected=spinner_FeedbackTypeYear.selectedItem.toString()
            println("Year i get"+year +"selected "+YearSelected)

            if ((year.contains(YearSelected) || year=="All ( First to Final Year )") && (nameOfInstitute==Stud_Institute || nameOfInstitute=="All Institute" )&& (Courseget==Course|| Courseget=="All Courses" && Feedback.equals(Feedbackget)))
            {

                var feedbackSelected= spinner_FeedbackType.selectedItem.toString()
                if (feedbackSelected.equals("Select Your Choice"))
                {
                    Toast.makeText(this,feedbackSelected,Toast.LENGTH_LONG).show()

                }
                else if(selected_Af_SS_Q1_Answer.equals("EXAM FEEDBACK FORMATIVE",ignoreCase = true))
                {
                    val intentStudentSurvey = Intent(this@FeedbackOptionActivity, FormativeFeedbackActivity::class.java)
                    startActivity(intentStudentSurvey)
                    finish()
                }
                else if(selected_Af_SS_Q1_Answer.equals("EXAM FEEDBACK SUMMATIVE",ignoreCase = true))
                {
                    val intentStudentSurvey = Intent(this@FeedbackOptionActivity, Student_Feedback_SummativeExam::class.java)
                    startActivity(intentStudentSurvey)
                    finish()
                }
                else  if(selected_Af_SS_Q1_Answer.equals("Select Your Choice",ignoreCase = true))
                {
                    GenericUserFunction.DisplayToast(this@FeedbackOptionActivity,"Please select available feedback name to proceed further.")
                }
                println("feedbackSelected  "+feedbackSelected)

            }else
            {
                Toast.makeText(applicationContext,"Your feedback not scheduled yet",Toast.LENGTH_LONG).show()
            }








        }

    }
    fun GetCurrentDate(date:String) {
        if (InternetConnection.checkConnection(this)) {
        val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
        try {

            dialog.setMessage("Please Wait!!! \nwhile we are getting available feedback")
            dialog.setCancelable(false)
            dialog.show()


        println("c" + current_date)

        var phpApiInterface: PhpApiInterface =
            ApiClientPhp.getClient().create(PhpApiInterface::class.java)
        var calldate: Call<FeedBackSchedule> = phpApiInterface.CurrentDateSubmit(date)
        calldate.enqueue(object : Callback<FeedBackSchedule> {
            override fun onFailure(call: Call<FeedBackSchedule>, t: Throwable) {
                dialog.dismiss()
                GenericUserFunction.showApiError(this@FeedbackOptionActivity,t.message.toString())

            }

            override fun onResponse(
                call: Call<FeedBackSchedule>,
                response: Response<FeedBackSchedule>
            ) {
                dialog.dismiss()
                val result: List<FeedBackScheduleField>? = response.body()!!.Data
                println("Response1 >> " + result!![0].id)
                if (result!![0].id == "error") {


                    GenericUserFunction.showApiError(this@FeedbackOptionActivity,"Feedback not Scheduled")

                } else {

                    var listSize = result!!.size
                    var check:Int=0

                    for (i in 0 .. listSize - 1) {

                        if(result!![i].INSTITUTE_NAME.equals(Stud_Institute) || result!![i].INSTITUTE_NAME=="All Institute")
                        {

                            feedbacknames.add(result!![i].FEEDBACK_NAME)
                            feedbacdsates.add(result!![i].START_DATE)
                            feedbacedates.add(result!![i].END_DATE)
                            yeararr.add(result!![i].YEAR)
                            institutearr.add(result!![i].INSTITUTE_NAME)
                            coursearr.add(result!![i].COURSE_NAME)

                            var datayear=  result!![i].YEAR

                            println("data year here "+datayear)

                            println("true part")
                            check=1
                        }




                    }
                    if (check==0){
                        GenericUserFunction.showApiError(this@FeedbackOptionActivity,"Feedback not Scheduled")
                    }
                }
            }
        })
    }
        catch (ex : Exception)
        {
            dialog.dismiss()
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time.")
        }
    }
    else
    {
        GenericUserFunction.showInternetNegativePopUp(
            this,
            getString(R.string.failureNoInternetErr))
    }
    }
}
