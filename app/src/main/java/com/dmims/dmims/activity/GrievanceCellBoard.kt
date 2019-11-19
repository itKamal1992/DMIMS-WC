package com.dmims.dmims.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GrievanceCellBoard : AppCompatActivity() {
    private lateinit var mServices: IMyAPI
    private var TAG = GrievanceCellBoard::class.java.simpleName

    var greviancetype = arrayOf("Administrative", "Academic", "Other")
    var studcat = arrayOf("UG", "PG", "PhD")
    var grevianceinfo = arrayOf("Issues about subjective grades.","Issues about course content, teaching methodology, etc.","Issues about faculty performance or faculty behavior.","Issues about parking, hostel, etc.","Issues about scholarships.","Issues about sexual harassment.","Issues about transcripts.","Issues about class availability, times, etc.","Issues about academic advising.","Other")
    private lateinit var btnPubGrevi: Button
    private var stud_k: String = "-"
    private lateinit var spinner_greviancetype: Spinner
    private lateinit var spinner_grevianceinfo: Spinner
    private lateinit var spinner_studcat: Spinner
    private lateinit var sel_information: String
    private lateinit var sel_name: String
    private lateinit var sel_mobileno: String
    private lateinit var sel_emailid: String
    private lateinit var sel_address: String
    private lateinit var sel_workplace: String
    private lateinit var sel_datetime: String
    private lateinit var sel_studcat: String
    private lateinit var sel_nameofindividual: String
    private lateinit var sel_detaildesc: String
    private lateinit var selectedgreviancetype: String
    private lateinit var selectedgrevianceinfo: String
    private lateinit var sel_proposedsol: String
    private lateinit var sel_providername: String
    private lateinit var notice_date: String
    private lateinit var notice_title: String
    private lateinit var notice_desc: String
    //private lateinit var editViewtxtInfo: EditText
    private lateinit var edit_viewtxt_name: EditText
    private lateinit var edit_viewtxt_mobileno: EditText
    private lateinit var edit_viewtxt_emailid: EditText
    private lateinit var edit_viewtxt_address: EditText
    private lateinit var edit_viewtxt_workplace: EditText
    private lateinit var edit_viewtxt_datetime: EditText
    private lateinit var edit_viewtxt_nameofindi: EditText
    private lateinit var edit_viewtxt_proposedsol: EditText
    private lateinit var edit_viwtext_detaildescr: EditText
    private lateinit var edit_viewtxt_providename: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grievance_cell)
        stud_k = intent.getStringExtra("stud_k")
        btnPubGrevi = findViewById(R.id.btn_publish_grievance)
        spinner_grevianceinfo = findViewById(R.id.spinner_grevianceinfo)
        edit_viewtxt_name = findViewById(R.id.viewtxt_name)
        edit_viewtxt_mobileno = findViewById(R.id.viewtxt_mobileno)
        edit_viewtxt_emailid = findViewById(R.id.viewtxt_emailid)
        edit_viewtxt_address = findViewById(R.id.viewtxt_address)
        edit_viewtxt_workplace = findViewById(R.id.viewtxt_workplace)
        spinner_studcat = findViewById(R.id.spinner_studcat)
        edit_viewtxt_datetime = findViewById(R.id.viewtxt_datetime)
        edit_viwtext_detaildescr = findViewById(R.id.viwtext_detaildescr)
        edit_viewtxt_nameofindi = findViewById(R.id.viewtxt_nameofindi)
        edit_viewtxt_proposedsol = findViewById(R.id.viewtxt_proposedsol)
        edit_viewtxt_providename = findViewById(R.id.viewtxt_providename)

        spinner_greviancetype = findViewById(R.id.spinner_typeofgreviance)
        mServices = Common.getAPI()

        var greviancetypeAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, greviancetype)
        spinner_greviancetype.adapter = greviancetypeAdap
        spinner_greviancetype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedgreviancetype = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var grevianceinfoAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, grevianceinfo)
        spinner_grevianceinfo.adapter = grevianceinfoAdap
        spinner_grevianceinfo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedgrevianceinfo = p0!!.getItemAtPosition(p2) as String
                sel_information = selectedgrevianceinfo
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var studcatAdap: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, studcat)
        spinner_studcat.adapter = studcatAdap
        spinner_studcat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sel_studcat = p0!!.getItemAtPosition(p2) as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnPubGrevi.setOnClickListener {
            sendGreviance()
        }

    }

    private fun sendGreviance() {

        if (edit_viewtxt_name.text.toString().isEmpty()) {
            edit_viewtxt_name.error = "Please provide name of Grievant"
            return
        } else {
            sel_name = edit_viewtxt_name.text.toString()
        }
        if (edit_viewtxt_mobileno.text.toString().isEmpty() || edit_viewtxt_mobileno.text.toString().length != 10) {
            edit_viewtxt_mobileno.error = "Please input mobile no."
            return
        } else {
            sel_mobileno = edit_viewtxt_mobileno.text.toString()
        }
        if (edit_viewtxt_emailid.text.toString().isEmpty()) {
            edit_viewtxt_emailid.error = "Please input valid email id."
            return
        } else {
            sel_emailid = edit_viewtxt_emailid.text.toString()
        }
        if (edit_viewtxt_address.text.toString().isEmpty()) {
            edit_viewtxt_address.error = "Please input valid address."
            return
        } else {
            sel_address = edit_viewtxt_address.text.toString()
        }
        if (edit_viewtxt_workplace.text.toString().isEmpty()) {
            edit_viewtxt_workplace.error = "Please input valid workplace."
            return
        } else {
            sel_workplace = edit_viewtxt_workplace.text.toString()
        }
        if (edit_viewtxt_datetime.text.toString().isEmpty()) {
            edit_viewtxt_datetime.error = "Please input valid date place time of event."
            return
        } else {
            sel_datetime = edit_viewtxt_datetime.text.toString()
        }

        if (edit_viwtext_detaildescr.text.toString().isEmpty()) {
            edit_viwtext_detaildescr.error = "Please input detail description."
            return
        } else {
            sel_detaildesc = edit_viwtext_detaildescr.text.toString()
        }

        if (edit_viewtxt_nameofindi.text.toString().isEmpty()) {
            edit_viewtxt_nameofindi.error = "Please input valid name of individuals."
            return
        } else {
            sel_nameofindividual = edit_viewtxt_nameofindi.text.toString()
        }
        if (edit_viewtxt_proposedsol.text.toString().isEmpty()) {
            sel_proposedsol = "-"
        } else {
            sel_proposedsol = edit_viewtxt_proposedsol.text.toString()
        }
        if (edit_viewtxt_providename.text.toString().isEmpty()) {
            sel_proposedsol = "-"
        } else {
            sel_providername = edit_viewtxt_providename.text.toString()
        }

        try {
            mServices.InsertGrievanceReport(
                sel_information,
                sel_name,
                sel_mobileno,
                sel_emailid,
                sel_address,
                sel_workplace,
                sel_studcat,
                selectedgreviancetype,
                sel_datetime,
                sel_detaildesc,
                sel_nameofindividual,
                sel_proposedsol,
                sel_providername,
                stud_k
            ).enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    Toast.makeText(this@GrievanceCellBoard, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                    val result: APIResponse? = response.body()
                    if (result!!.Responsecode == 200) {                                                                                                                                          4
                        Toast.makeText(this@GrievanceCellBoard, result.Status, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@GrievanceCellBoard, result.Status, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}

