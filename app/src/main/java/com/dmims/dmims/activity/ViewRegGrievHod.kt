package com.dmims.dmims.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.adapter.GrievanceAdapterHodShow
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.StudentGrievanceGet
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import kotlinx.android.synthetic.main.view_griev_hod.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ViewRegGrievHod : AppCompatActivity() {
    var from_date_sel: String = "-"
    var to_date_sel: String = "-"
    var to_date: TextView? = null
    var from_date: TextView? = null
    var search_id: Button? = null
    var current_date: String = "-"
    var progressBar: ProgressBar? = null
    var k: Int = 0
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var instname: String? = null
    var COURSE_ID: String? = null
    var users = ArrayList<StudentGrievanceGet>()

    lateinit var recyclerView: RecyclerView

    var student_id_key:String = ""
    var roll:String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_griev_hod)

        to_date = findViewById(R.id.select_to_date)
        from_date = findViewById(R.id.select_from_date)
        search_id = findViewById<Button>(R.id.search_id)
        recyclerView = findViewById<RecyclerView>(R.id.rv_schedledfeedback)
        progressBar = findViewById<ProgressBar>(R.id.progressBar7)
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        to_date!!.text = sdf.format(cal.time).toString()
        from_date!!.text = sdf.format(cal.time).toString()
        to_date_sel = sdf.format(cal.time)
        current_date = sdf.format(cal.time)
        from_date_sel = sdf.format(cal.time)

        search_id!!.setOnClickListener {

            println("to date" + to_date + " from date " + from_date)

            GetDateGrievance()
        }

        btn_currentattend.setOnClickListener {

            val intent=Intent(this@ViewRegGrievHod,CurrentGrievHodPrincConv::class.java)
            startActivity(intent)

        }


        mServices = Common.getAPI()

        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
         student_id_key = mypref.getString("Stud_id_key", null)
         roll=mypref.getString("key_userrole", null)



        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBar!!.visibility = View.VISIBLE
        try {
            mServices.GetStudGrievanceSubmited()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        progressBar!!.visibility = View.GONE
                        GenericUserFunction.showNegativePopUp(this@ViewRegGrievHod,t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    )
                    {
                        progressBar!!.visibility = View.GONE
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Status == "ok") {
                            var listSize = result.Data17!!.size
                            val users = ArrayList<StudentGrievanceGet>()
                            println("result 4>>> " + users)

                            for (i in 0..listSize - 1) {
                                if(roll.equals("GREVIANCE_CELL")){

                                        if (result.Data17!![i].G_ATTACHMENT == "T") {
//                                        k = R.drawable.ic_notice_yes
//                                    result.Data17!![i].ID

                                            if(result.Data17!![i].ATTACHMENT_URL.contains(".jpg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".jpeg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".png",ignoreCase = true))
                                            {
                                                k = R.drawable.ic_jpg
                                            }else
                                                if(result.Data17!![i].ATTACHMENT_URL.contains(".pdf",ignoreCase = true)) {
                                                    k = R.drawable.icon_pdf
                                                }
                                        }
                                        else
                                        {
                                            k = R.drawable.ic_anotice_no
                                        }


                                        users.add(
                                            StudentGrievanceGet(
                                                result.Data17!![i].G_ID,
                                                result.Data17!![i].STUD_ID,
                                                result.Data17!![i].course_id,
                                                result.Data17!![i].roll_no,
                                                result.Data17!![i].Grev_name,
                                                result.Data17!![i].Inst_Name,
                                                result.Data17!![i].Comp_To,
                                                result.Data17!![i].Grev_Filename,
                                                result.Data17!![i].DEPARTMENT,
                                                result.Data17!![i].ATTACHMENT_URL,
                                                result.Data17!![i].G_SUBJECT,
                                                result.Data17!![i].G_CATEGORY,
                                                result.Data17!![i].G_AGAINST,
                                                result.Data17!![i].G_DISCRIPTION,
                                                result.Data17!![i].G_DATE,
                                                result.Data17!![i].G_ATTACHMENT,
                                                result.Data17!![i].G_STATUS,
                                                result.Data17!![i].ASSING_TO_ID,
                                                result.Data17!![i].REMINDER,
                                                result.Data17!![i].G_COMMENT,
                                                result.Data17!![i].TO_DATE,
                                                result.Data17!![i].FROM_DATE,
                                                k,
                                                false

                                            )
                                        )



                                        if (users.isEmpty()) {
                                            GenericUserFunction.showOopsError(
                                                this@ViewRegGrievHod,
                                                "No Grievance found for the current request"
                                            )
                                        } else {

                                            val adapter =
                                                GrievanceAdapterHodShow(users, this@ViewRegGrievHod)
                                            recyclerView.adapter = adapter
                                        }

                                }
                                else
                                if ((result.Data17!![i].ASSING_TO_ID.equals(student_id_key))&&(roll.contains(result.Data17!![i].Comp_To))) {
                                    if (result.Data17!![i].G_ATTACHMENT == "T") {
//                                        k = R.drawable.ic_notice_yes
//                                        result.Data17!![i].ID

                                        if(result.Data17!![i].ATTACHMENT_URL.contains(".jpg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".jpeg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".png",ignoreCase = true))
                                        {
                                            k = R.drawable.ic_jpg
                                        }else
                                            if(result.Data17!![i].ATTACHMENT_URL.contains(".pdf",ignoreCase = true)) {
                                                k = R.drawable.icon_pdf
                                            }
                                    }
                                    else
                                    {
                                        k = R.drawable.ic_anotice_no
                                    }


                                    users.add(
                                        StudentGrievanceGet(
                                            result.Data17!![i].G_ID,
                                            result.Data17!![i].STUD_ID,
                                            result.Data17!![i].course_id,
                                            result.Data17!![i].roll_no,
                                            result.Data17!![i].Grev_name,
                                            result.Data17!![i].Inst_Name,
                                            result.Data17!![i].Comp_To,
                                            result.Data17!![i].Grev_Filename,
                                            result.Data17!![i].DEPARTMENT,
                                            result.Data17!![i].ATTACHMENT_URL,
                                            result.Data17!![i].G_SUBJECT,
                                            result.Data17!![i].G_CATEGORY,
                                            result.Data17!![i].G_AGAINST,
                                            result.Data17!![i].G_DISCRIPTION,
                                            result.Data17!![i].G_DATE,
                                            result.Data17!![i].G_ATTACHMENT,
                                            result.Data17!![i].G_STATUS,
                                            result.Data17!![i].ASSING_TO_ID,
                                            result.Data17!![i].REMINDER,
                                            result.Data17!![i].G_COMMENT,
                                            result.Data17!![i].TO_DATE,
                                            result.Data17!![i].FROM_DATE,
                                            k,
                                            false

                                        )
                                    )



                                    if ((users.isEmpty())||(users.size==0)) {
                                        GenericUserFunction.showOopsError(
                                            this@ViewRegGrievHod,
                                            "No Grievance found for the current request"
                                        )
                                    } else {

                                        val adapter =
                                            GrievanceAdapterHodShow(users, this@ViewRegGrievHod)
                                        recyclerView.adapter = adapter
                                    }
                                }

                            }
                            if (users.isEmpty()) {
                                GenericUserFunction.showOopsError(
                                    this@ViewRegGrievHod,
                                    "No Grievance found for the current request"
                                )
                            }
                            }

                    }
                })

        } catch (ex: Exception) {
            progressBar!!.visibility = View.GONE
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }


    private fun GetDateGrievance() {

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        progressBar!!.visibility = View.VISIBLE
        try {
            mServices.GetDatewiseOnlineGrievanceReport(select_to_date.text.toString(), select_from_date.text.toString())
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        progressBar!!.visibility = View.GONE
                        GenericUserFunction.showNegativePopUp(this@ViewRegGrievHod,t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        progressBar!!.visibility = View.GONE
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Status == "ok") {
                            var listSize = result.Data17!!.size
                            users = ArrayList<StudentGrievanceGet>()

                            println("result 4>>> " + users)

                            for (i in 0..listSize - 1) {
                                if (result.Data17!![i].ASSING_TO_ID.equals(student_id_key)) {

                                    if (result.Data17!![i].G_ATTACHMENT == "T") {
//                                        k = R.drawable.ic_notice_yes
//                                    result.Data17!![i].ID

                                        if(result.Data17!![i].ATTACHMENT_URL.contains(".jpg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".jpeg",ignoreCase = true)||result.Data17!![i].ATTACHMENT_URL.contains(".png",ignoreCase = true))
                                        {
                                            k = R.drawable.ic_jpg
                                        }else
                                            if(result.Data17!![i].ATTACHMENT_URL.contains(".pdf",ignoreCase = true)) {
                                                k = R.drawable.icon_pdf
                                            }
                                    }
                                    else
                                    {
                                        k = R.drawable.ic_anotice_no
                                    }



                                    users.add(
                                        StudentGrievanceGet(
                                            result.Data17!![i].G_ID,
                                            result.Data17!![i].STUD_ID,
                                            result.Data17!![i].course_id,
                                            result.Data17!![i].roll_no,
                                            result.Data17!![i].Grev_name,
                                            result.Data17!![i].Inst_Name,
                                            result.Data17!![i].Comp_To,
                                            result.Data17!![i].Grev_Filename,
                                            result.Data17!![i].DEPARTMENT,
                                            result.Data17!![i].ATTACHMENT_URL,
                                            result.Data17!![i].G_SUBJECT,
                                            result.Data17!![i].G_CATEGORY,
                                            result.Data17!![i].G_AGAINST,
                                            result.Data17!![i].G_DISCRIPTION,
                                            result.Data17!![i].G_DATE,
                                            result.Data17!![i].G_ATTACHMENT,
                                            result.Data17!![i].G_STATUS,
                                            result.Data17!![i].ASSING_TO_ID,
                                            result.Data17!![i].REMINDER,
                                            result.Data17!![i].G_COMMENT,
                                            result.Data17!![i].TO_DATE,
                                            result.Data17!![i].FROM_DATE,
                                            k,
                                            false

                                        )
                                    )
                                }

                            }
                            if (users.isEmpty()) {
                                GenericUserFunction.showOopsError(
                                    this@ViewRegGrievHod,
                                    "No Grievance found for the current request"
                                )
                            } else {

//                                val adapter =
//                                    GrievanceAdapterStudShow(users, this@ViewRegGrievHod)
//                                recyclerView.adapter = adapter

                                val adapter =
                                    GrievanceAdapterHodShow                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        (users, this@ViewRegGrievHod)
                                recyclerView.adapter = adapter
                            }

                        }
                    }
                })

        } catch (ex: Exception) {
            progressBar!!.visibility = View.GONE
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }

    }


    fun clickFromDataPicker(view: View) {
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
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                from_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun clickToDataPicker(view: View) {
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
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                cal.set(year, monthOfYear, dayOfMonth)
                val date = cal.time
                sdf.format(date)
                to_date!!.text = sdf.format(date).toString()
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}
