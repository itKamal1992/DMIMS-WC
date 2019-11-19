package com.dmims.dmims.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.showToast
import com.dmims.dmims.R
import kotlinx.android.synthetic.main.activity_feedback_optiontype.*
class FeedbackOptionType : AppCompatActivity() {
    private lateinit var spinner_FeedbackType: Spinner
    private lateinit var selected_Af_SS_Q1_Answer: String
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_optiontype)


        spinner_FeedbackType=findViewById<Spinner>(R.id.spinner_FeedbackType)
        spinner_FeedbackType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected_Af_SS_Q1_Answer = p0!!.getItemAtPosition(p2) as String


            }
            override fun onNothingSelected(p0: AdapterView<*>?)
            {
            }
        }

        btn_feedbackNext_submit.setOnClickListener {


            if(selected_Af_SS_Q1_Answer.equals("Formative Examination",ignoreCase = true))
            {
                val intentStudentSurvey = Intent(this@FeedbackOptionType, FormativeFeedbackActivity::class.java)
                startActivity(intentStudentSurvey)
                finish()
            }
            else
                if(selected_Af_SS_Q1_Answer.equals("Summative Examination",ignoreCase = true))
                {
                    val intentStudentSurvey = Intent(this@FeedbackOptionType, Student_Feedback_SummativeExam::class.java)
                    startActivity(intentStudentSurvey)
                    finish()
                }
                else
                    if(selected_Af_SS_Q1_Answer.equals("Select Your Choice",ignoreCase = true))
                    {
                        showToast("Please select available feedback name to proceed further.")
                    }
        }

    }
}
