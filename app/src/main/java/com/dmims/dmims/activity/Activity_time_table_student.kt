package com.dmims.dmims.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.TimeTableDataC
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.TimeTableData
import com.dmims.dmims.model.TimeTableDataRef
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import com.github.barteksc.pdfviewer.PDFView
import kotlinx.android.synthetic.main.activity_time_table_institute.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Activity_time_table_student : AppCompatActivity() {
    var pdf: PDFView? = null
    var stud_k: Int = 0
    var yearlist = ArrayList<String>()
    var semlist = ArrayList<String>()
    var typeoftimetbl = ArrayList<String>()

    private lateinit var selectedsemtype: String
    private lateinit var selectedyear: String
    private lateinit var selectedtypeoftimetbl: String
    var semtype = arrayOf("Select Semester")
    var yeartype = arrayOf("Select Year")
    private lateinit var spinner_semestertype: Spinner
    private lateinit var spinner_timetabletype: Spinner
    private lateinit var spinner_timetableyear: Spinner
    private lateinit var btn_download: Button
    private lateinit var progressBar1: ProgressBar
    private lateinit var progressBar2: ProgressBar
    private var trsnsdlist: ArrayList<TimeTableDataRef>? = null
    private lateinit var institute_name: String

    private var time_table_name: String = "-"
    private var time_table_url: String = "-"
    private lateinit var mServices: IMyAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_student)
        setSupportActionBar(toolbar)
        pdf = findViewById<PDFView>(R.id.pdfid)
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        try {
            stud_k = Integer.parseInt(mypref.getString("Stud_id_key", null))
            institute_name = mypref.getString("key_institute", null)!!
        }
        catch (ex:java.lang.Exception)
        {
           Toast.makeText(this,ex.localizedMessage,Toast.LENGTH_LONG).show()
        }

        spinner_semestertype = findViewById(R.id.spinner_semestertype)
        spinner_timetabletype = findViewById(R.id.spinner_timetabletype)
        spinner_timetableyear = findViewById(R.id.spinner_timetableyear)
        btn_download = findViewById(R.id.btn_download)
        progressBar1 = findViewById(R.id.progressBar1)
        progressBar2 = findViewById(R.id.progressBar2)
        progressBar1.visibility = View.INVISIBLE //visible
        progressBar2.visibility = View.INVISIBLE //visible

        typeoftimetbl.add("Select Type")

        if (institute_name == "JNMC")
        {
            typeoftimetbl.add("Clinical")
            typeoftimetbl.add("Theory")
        }else
        if (institute_name == "RNPC" || institute_name == "SPDC")
        {
            typeoftimetbl.add("TH_CL")
        }else
        {

        }
//        try
//        {
//            mServices = Common.getAPI()
//            mServices.StudentSearch(stud_k)
//                .enqueue(object : Callback<APIResponse>
//                {
//                    override fun onFailure(call: Call<APIResponse>, t: Throwable)
//                    {
//                        Toast.makeText(this@Activity_time_table_student, t.message, Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>)
//                    {
//                        val result: APIResponse? = response.body()
//                        if (result!!.Status == "ok") {
//                            institute_name = result.Data7!!.course_institute
//
//                            //  typeoftimetbl.clear()
//                            if (institute_name == "JNMC")
//                            {
//                                typeoftimetbl.add("Clinical")
//                                typeoftimetbl.add("Theory")
//                            }
//                            if (institute_name == "RNPC" || institute_name == "SPDC")
//                            {
//                                typeoftimetbl.add("TH_CL")
//                            }
//                        }
//                    }
//                })
//        } catch (ex: Exception)
//        {
//
//            ex.printStackTrace()
//            GenericUserFunction.showApiError(
//                this,
//                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//            )
//        }


        var typeoftimetblAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeoftimetbl)

        spinner_timetabletype.adapter = typeoftimetblAdap


        spinner_timetabletype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {
                    selectedtypeoftimetbl = p0!!.getItemAtPosition(p2) as String
                    if (selectedtypeoftimetbl == "Theory") {
                        selectedtypeoftimetbl = "TH"
                    }
                    if (selectedtypeoftimetbl == "Clinical") {
                        selectedtypeoftimetbl = "CL"
                    }

//                    if (selectedtypeoftimetbl == "Select Type") {
//                        Toast.makeText(
//                            this@Activity_time_table_student,
//                            "Please select table type",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                        return
//                    }
                    progressBar1.visibility = View.VISIBLE //visible
                    progressBar2.visibility = View.INVISIBLE //visible
                    yearlist.clear()

                    var phpApiInterface: PhpApiInterface =
                        ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                    var call: Call<TimeTableData> =
                        phpApiInterface.timetable_details(institute_name, selectedtypeoftimetbl)
                    call.enqueue(object : Callback<TimeTableData> {
                        override fun onFailure(call: Call<TimeTableData>, t: Throwable) {
                            Toast.makeText(
                                this@Activity_time_table_student,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<TimeTableData>,
                            response: Response<TimeTableData>
                        ) {
                            progressBar1.visibility = View.INVISIBLE //visible
                            progressBar2.visibility = View.INVISIBLE //visible
                            var users = ArrayList<TimeTableDataC>()

                            if (response.isSuccessful) {
                                users.clear()
                                trsnsdlist = response.body()!!.Data
                                if (trsnsdlist!![0].ID == "error") {
                                    Toast.makeText(
                                        this@Activity_time_table_student,
                                        "No Data in Time Table Master.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    users.clear()
                                    var listSize = trsnsdlist!!.size
                                    for (i in 0..listSize - 1) {
                                        if (trsnsdlist!![i].INSTITUTE == institute_name) {
                                            if (yearlist.contains(trsnsdlist!![i].YEAR)) {
                                            } else {
                                                yearlist.add(trsnsdlist!![i].YEAR)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    yearlist.add("Select Year")
                    var usercourselistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@Activity_time_table_student,
                        R.layout.support_simple_spinner_dropdown_item,
                        yearlist
                    )
                    spinner_timetableyear.adapter = usercourselistadp

                } catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@Activity_time_table_student,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }

            }


            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        spinner_timetableyear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                try {
                    selectedyear = p0!!.getItemAtPosition(p2) as String
                    if (selectedtypeoftimetbl == "Theory") {
                        selectedtypeoftimetbl = "TH"
                    }
                    if (selectedtypeoftimetbl == "Clinical") {
                        selectedtypeoftimetbl = "CL"
                    }
//                    if (selectedtypeoftimetbl == "Select Type") {
//                        Toast.makeText(
//                            this@Activity_time_table_student,
//                            "Please select table type",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                        return
//                    }
                    progressBar2.visibility = View.VISIBLE //visible
                    progressBar1.visibility = View.INVISIBLE //visible
                    semlist.clear()
                    var phpApiInterface: PhpApiInterface =
                        ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                    var call: Call<TimeTableData> =
                        phpApiInterface.timetable_details(institute_name, selectedtypeoftimetbl)


                    call.enqueue(object : Callback<TimeTableData> {

                        override fun onFailure(call: Call<TimeTableData>, t: Throwable) {
                            Toast.makeText(
                                this@Activity_time_table_student,
                                t.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<TimeTableData>,
                            response: Response<TimeTableData>
                        ) {
                            progressBar2.visibility = View.INVISIBLE //visible
                            progressBar1.visibility = View.INVISIBLE //visible
                            var users = ArrayList<TimeTableDataC>()
                            if (response.isSuccessful) {
                                users.clear()
                                trsnsdlist = response.body()!!.Data
                                if (trsnsdlist!![0].ID == "error") {
                                    Toast.makeText(
                                        this@Activity_time_table_student,
                                        "No Data in Time Table Master.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    users.clear()
                                    var listSize = trsnsdlist!!.size
                                    for (i in 0..listSize - 1) {
                                        if (trsnsdlist!![i].INSTITUTE == institute_name) {
                                            if (trsnsdlist!![i].TYPE == selectedtypeoftimetbl) {
                                                if (trsnsdlist!![i].YEAR == selectedyear) {
                                                    if (semlist.contains(trsnsdlist!![i].SEMESTER)) {
                                                    } else {
                                                        semlist.add(trsnsdlist!![i].SEMESTER)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    semlist.add("Select Semester")
                    var usersemlistadp: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@Activity_time_table_student,
                        R.layout.support_simple_spinner_dropdown_item,
                        semlist
                    )
                    spinner_semestertype.adapter = usersemlistadp
                } catch (ex: Exception) {

                    ex.printStackTrace()
                    GenericUserFunction.showApiError(
                        this@Activity_time_table_student,
                        "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                    )
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        spinner_semestertype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedsemtype = p0!!.getItemAtPosition(p2) as String
            }
        }

        btn_download.setOnClickListener {
            if(selectedtypeoftimetbl==null || selectedtypeoftimetbl=="Select Type"){
                GenericUserFunction.DisplayToast(this,"Please select Time Table Type")
                return@setOnClickListener
            }
            if (selectedyear==null || selectedyear=="Select Year"){
                GenericUserFunction.DisplayToast(this,"Please select Time Table Year")
                return@setOnClickListener
            }
            if (selectedsemtype==null || selectedsemtype=="Select Semester")
            {
                GenericUserFunction.DisplayToast(this,"Please select Semester Type")
                return@setOnClickListener
            }
            try {
                var phpApiInterface: PhpApiInterface =
                    ApiClientPhp.getClient().create(PhpApiInterface::class.java)
                var call1: Call<TimeTableData> =
                    phpApiInterface.timetable_details_url(
                        institute_name,
                        selectedtypeoftimetbl,
                        selectedyear,
                        selectedsemtype
                    )
                call1.enqueue(object : Callback<TimeTableData> {
                    override fun onFailure(call1: Call<TimeTableData>, t: Throwable) {
                        Toast.makeText(
                            this@Activity_time_table_student,
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call1: Call<TimeTableData>,
                        response: Response<TimeTableData>
                    ) {
                        var users = ArrayList<TimeTableDataC>()
                        if (response.isSuccessful) {
                            users.clear()
                            trsnsdlist = response.body()!!.Data
                            if (trsnsdlist!![0].ID == "error") {
                                Toast.makeText(
                                    this@Activity_time_table_student,
                                    "No Data in Time Table Master.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                users.clear()
                                var listSize = trsnsdlist!!.size
                                for (i in 0..listSize - 1) {
                                    if (trsnsdlist!![i].INSTITUTE == institute_name) {
                                        if (trsnsdlist!![i].TYPE == selectedtypeoftimetbl) {
                                            if (trsnsdlist!![i].YEAR == selectedyear) {
                                                if (trsnsdlist!![i].SEMESTER == selectedsemtype) {
                                                    time_table_name =
                                                        trsnsdlist!![i].TIME_TABLE_NAME
                                                    time_table_url = trsnsdlist!![i].URL

                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        downloadpdf(time_table_url)
                    }
                })
            } 
            catch (ex: Exception)
            {

                ex.printStackTrace()
                GenericUserFunction.showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        }

    }

    fun downloadpdf(time_table_url1: String) {
//        intent = Intent(this, StudentPdfViewr::class.java)
//        intent.putExtra("pdfUrl", time_table_url1)
//        startActivity(intent)

        var intent: Intent = Intent(this@Activity_time_table_student, Common_PDF_Viewer::class.java)
        intent.putExtra("url", time_table_url1)
        intent.putExtra("actionTitle", "Time Table")
        startActivity(intent)
    }
}


