package com.dmims.dmims.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.dataclass.StudentGrievanceGet
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.remote.IMyAPI
import kotlinx.android.synthetic.main.activity_grievance_full.*
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class GrievanceFull : AppCompatActivity() {

    var current_date: String = "-"
    var progressBar: ProgressBar? = null
    var k: Int = 0
    private lateinit var mServices: IMyAPI
    var cal = Calendar.getInstance()
    var instname: String? = null
    var COURSE_ID: String? = null
    lateinit var btn_Submit: Button
    var ASSING_TO_ID: String = ""
    var str_Attachment: String = ""
    var selected_Department: String = ""
    var selected_user_choice: String = ""
    lateinit var spinnerForwarded: Spinner
    lateinit var ll_forwarded: LinearLayout
    lateinit var ll_commentOwn: LinearLayout
    lateinit var ll_viewAttchment: LinearLayout
    lateinit var tv_Date: TextView
    lateinit var Griev_Id: TextView
    lateinit var Griev_Name: TextView
    lateinit var Griev_Subject: TextView
    lateinit var Griev_Category: TextView
    lateinit var Griev_CompAgainst: TextView
    lateinit var Griev_Description: TextView
    lateinit var spinnerForwardeTo: Spinner
    lateinit var spinnerForwardeName: Spinner
    lateinit var et_comment: EditText
    var a: String = ""
    var listsinstz: Int = 0
    var hodName = ArrayList<String>()
    var deptName = ArrayList<String>()
    var DeanPrinciName = ArrayList<String>()
    var PrincipalDeanClg = ArrayList<String>()
    var ASSING_ID = ArrayList<String>()
    var instituteName1 = ArrayList<String>()
    var array_forwardedTo = ArrayList<String>()
    var instName = ArrayList<String>()
    var statusAction = ArrayList<String>()
    var Griev_IdStr: String = ""
    var roll: String = ""
    var strForwardedViaStatus: String = ""
    var strForwardeTo: String = ""
    var G_ID: String = ""
    var Comp_To: String = ""
    var Comp_To_first: String = ""
    var DEPARTMENT: String = ""
    var G_COMMENT: String = ""
    var G_STATUS: String = ""
    var G_DEPARTMENT: String = ""
    var REMINDER: String = ""
    var G_Attachment: String = ""
    var user_department: String = ""
    var user_id: Int = 0
    var G_Instname: String = ""
    var user_institute: String = ""
    var form_et_comment: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grievance_full)

        //Shared pref
        var mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        user_id = mypref.getString("Stud_id_key", null).toInt()
        user_department = mypref.getString("key_Department", null)
        roll = mypref.getString("key_userrole", null)
        user_institute = mypref.getString("key_institute", null)

        et_comment = findViewById(R.id.et_comment)
        btn_Submit = findViewById(R.id.btn_SubmitComment)
        spinnerForwarded = findViewById(R.id.spinner_forwardedVia)
        spinnerForwardeTo = findViewById(R.id.spinner_ForwaredTo)
        spinnerForwardeName = findViewById(R.id.spinner_ForwaredeName)
        ll_forwarded = findViewById(R.id.ll_forwarded)
        ll_commentOwn = findViewById(R.id.ll_commentOwn)
        ll_forwarded.visibility = View.GONE

        //Insti spinner disable
//        if (roll.equals("GREVIANCE_CELL")) {
//            txt_insTag.visibility = View.VISIBLE
//            layout_spin_Inst.visibility = View.VISIBLE
////            instName.add("Select Institute")
////            instName.add("JNMC")
////            instName.add("RNPC")
////            instName.add("SRMMCON")
////            instName.add("MGAC")
//            var InstAdap: ArrayAdapter<String> = ArrayAdapter<String>(
//                this@GrievanceFull,
//                R.layout.support_simple_spinner_dropdown_item, instName
//            )
//            spinner_selectIns!!.adapter = InstAdap
//        } else {
//            txt_insTag.visibility = View.GONE
//            layout_spin_Inst.visibility = View.GONE
//        }
        statusAction.add("Select Action")
        var statusActionAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GrievanceFull,
            R.layout.support_simple_spinner_dropdown_item, statusAction
        )
        spinnerForwarded!!.adapter = statusActionAdap

        spinnerForwarded.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                strForwardedViaStatus = spinnerForwarded.selectedItem.toString()
                if (strForwardedViaStatus.equals("Forward")) {
                    ll_forwarded.visibility = View.VISIBLE

                } else {
                    ll_forwarded.visibility = View.GONE
                }
            }
        }
        //Institute selected Spinner - Convener - Start
//        spinner_selectIns.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                user_institute = spinner_selectIns.selectedItem.toString()
//                if (user_institute == "Select Institute") {
//                    Toast.makeText(
//                        applicationContext,
//                        "please select Institute ",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else if (strForwardeTo == "Select") {
//                    Toast.makeText(
//                        applicationContext,
//                        "please select Forward To ",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else if (!strForwardeTo.equals("Convener", ignoreCase = true)) {
//                    GetDepartmentRole(strForwardeTo)
//                }
//            }
//        }
        //END - Selected Institute Action


        array_forwardedTo.add("Select Forwarded To")
        var ForwardedAdap: ArrayAdapter<String> = ArrayAdapter<String>(
            this@GrievanceFull,
            R.layout.support_simple_spinner_dropdown_item, array_forwardedTo
        )
        spinnerForwardeTo!!.adapter = ForwardedAdap
        spinnerForwardeTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                strForwardeTo = spinnerForwardeTo.selectedItem.toString()

                instituteName1.clear()
                hodName.clear()
                deptName.clear()
                if (strForwardeTo.equals("Select Forwarded To")) {
                    instituteName1.add("Select User")
                    var DepartmentAdap: ArrayAdapter<String> = ArrayAdapter<String>(
                        this@GrievanceFull,
                        R.layout.support_simple_spinner_dropdown_item, instituteName1
                    )
                    spinnerForwardeName!!.adapter = DepartmentAdap
                } else {
                    if (strForwardeTo.equals("Convener", ignoreCase = true)) {
                        instituteName1.add("Select User")
                        instituteName1.add("Grievance Cell Admin")
                        var DepartmentAdap: ArrayAdapter<String> = ArrayAdapter<String>(
                            this@GrievanceFull,
                            R.layout.support_simple_spinner_dropdown_item, instituteName1
                        )
                        spinnerForwardeName!!.adapter = DepartmentAdap
                        ASSING_TO_ID = "0"
                        selected_Department = "Grievance Cell Admin"
                    } else if (user_institute == "Select Institute") {
                        Toast.makeText(
                            applicationContext,
                            "please select Institute ",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        GetDepartmentRole(strForwardeTo)
                    }
                }
            }
        }
        tv_Date = findViewById(R.id.tv_date)
        Griev_Id = findViewById(R.id.tv_GrievanceId)
        Griev_Name = findViewById(R.id.tv_NameStud)
        Griev_Subject = findViewById(R.id.tv_subject)
        Griev_Category = findViewById(R.id.tv_category)
        Griev_CompAgainst = findViewById(R.id.tv_complaintAgainst)
        Griev_Description = findViewById(R.id.tv_description)
        val myFormat = "dd-MM-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        G_ID = intent.getStringExtra("id")
        mServices = Common.getAPI()
        try {
            mServices.GetStudGrievanceSubmited()
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<APIResponse>, response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Status == "ok") {
                            var listSize = result.Data17!!.size
                            val users = ArrayList<StudentGrievanceGet>()
                            println("result 4>>> " + users)
                            for (i in 0..listSize - 1) {
                                if (result.Data17!![i].G_ID.equals(G_ID)) {
                                    tv_Date.text = result.Data17!![i].G_DATE
                                    Griev_Id.text = result.Data17!![i].G_ID
                                    Griev_Name.text = result.Data17!![i].Grev_name
                                    Griev_Subject.text = result.Data17!![i].G_SUBJECT
                                    Griev_Category.text = result.Data17!![i].G_CATEGORY
                                    Griev_CompAgainst.text = result.Data17!![i].G_AGAINST
                                    Griev_Description.text = result.Data17!![i].G_DISCRIPTION
                                    str_Attachment = result.Data17!![i].ATTACHMENT_URL
                                    G_Attachment = result.Data17!![i].G_ATTACHMENT
                                    G_Instname = result.Data17!![i].Inst_Name
                                    Comp_To_first = result.Data17!![i].Comp_To    //Original
                                    form_et_comment = result.Data17!![i].G_COMMENT  //Original
                                    REMINDER = result.Data17!![i].REMINDER    //Original
                                    G_STATUS = result.Data17!![i].G_STATUS
                                    G_DEPARTMENT = result.Data17!![i].DEPARTMENT    //Original
                                    tv_GrievanceComment.text = form_et_comment
                                    tv_complaintTo.text = Comp_To_first
                                    tv_departmentTo.text = G_DEPARTMENT

                                    println("Griev_IdStr " + Griev_IdStr)

                                    val url = result.Data17!![i].ATTACHMENT_URL

                                }

                            }
                            statusAction.clear()
                            if (G_DEPARTMENT == "Grievance Cell Admin" && roll == "GREVIANCE_CELL") {
                                statusAction.add("Select Action")
                                statusAction.add("Attend")
                                statusAction.add("Forward")
                            } else if (roll != "GREVIANCE_CELL") {
                                statusAction.add("Select Action")
                                statusAction.add("Attend")
                                statusAction.add("Forward")
                            } else {
                                statusAction.add("Select Action")
                                statusAction.add("Forward")

                            }


                            ////////////////start
                            if (roll.equals("Principal", ignoreCase = true) || roll.equals(
                                    "Dean",
                                    ignoreCase = true
                                )
                            ) {
                                array_forwardedTo.add("HOD")
                                array_forwardedTo.add("Convener")
                            } else if (roll.contains("HOD", ignoreCase = true)) {

                                if (G_Instname == "SRMMCON" || G_Instname == "RNPC") {
                                    array_forwardedTo.add("Principal")

                                } else {
                                    array_forwardedTo.add("Dean")
                                }


                                array_forwardedTo.add("HOD")
                                array_forwardedTo.add("Convener")
                            } else if (roll.equals("GREVIANCE_CELL", ignoreCase = true)) {
                                if (G_Instname == "SRMMCON" || G_Instname == "RNPC") {
                                    array_forwardedTo.add("Principal")

                                } else {
                                    array_forwardedTo.add("Dean")
                                }
                                array_forwardedTo.add("HOD")
                                if (!Comp_To_first.equals("GREVIANCE_CELL")) {
                                    array_forwardedTo.add("Convener")
                                }
                            }
                            ///////////////end
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }




        btn_Submit.setOnClickListener {

            if (strForwardedViaStatus.equals("Select Action")) {
                GenericUserFunction.DisplayToast(this@GrievanceFull, "Select Grievance status")
                return@setOnClickListener
            }

            if (selected_user_choice.equals("Select User")) {
                GenericUserFunction.DisplayToast(this@GrievanceFull, "Please Select User")
                return@setOnClickListener
            }

            if (strForwardeTo.equals("Select Forwarded To")) {
                GenericUserFunction.DisplayToast(this@GrievanceFull, "Please Select Forwarded To")
                return@setOnClickListener
            }

//            if (roll.equals("GREVIANCE_CELL")) {
//
//
//            }
//
            if (et_comment.text.toString().equals("")) {
                et_comment.error = "Please enter your response"
                GenericUserFunction.DisplayToast(this@GrievanceFull, "Please enter your response")
                return@setOnClickListener
            }
            if ((strForwardedViaStatus.equals("Attend")) && (et_comment.text.toString().isNotEmpty())) {

                if (REMINDER.equals("-") && form_et_comment.equals("-")) {
                    form_et_comment =
                        "Seq. No. 2" + "\n" + "Status" + ": " + strForwardedViaStatus + "\n" + "User Account" + ": " + roll + "\n" + "Remark" + ": " + "Self $roll" + "\n" + "Comments" + ": " + et_comment.text.toString() + "\n\n" + "Seq. No. 1" + "\n" + "Status" + ": " + G_STATUS + "\n" + "User Account" + ": " + Comp_To_first + "\n" + "Remark" + ": " + REMINDER + "\n" + "Comment" + ": " + form_et_comment + "\n\n"
                } else {
                    var count: Int = StringUtils.countMatches(form_et_comment, "\n\n")
                    var seq_no = count + 1
                    form_et_comment =
                        "Seq. No. $seq_no" + "\n" + "Status" + ": " + strForwardedViaStatus + "\n" + "User Account" + ": " + roll + "\n" + "Remark" + ": " + "Self $roll" + "\n" + "Comments" + ": " + et_comment.text.toString() + "\n\n" + form_et_comment

                }

                callForAttend(
                    G_ID,
                    roll,
                    user_department,
                    form_et_comment,
                    strForwardedViaStatus,
                    user_id,
                    "Self $roll"
                )

            }
            if ((strForwardedViaStatus.equals("Forward")) && (et_comment.text.toString().isNotEmpty())) {
                if (strForwardeTo.equals("Convener", ignoreCase = true)) {
                    strForwardeTo = "GREVIANCE_CELL"
                }

                if (REMINDER.equals("-") && form_et_comment.equals("-")) {
                    form_et_comment =
                        "Seq. No. 2" + "\n" + "Status" + ": " + strForwardedViaStatus + "\n" + "User Account" + ": " + roll + "\n" + "Remark" + ": " + "$roll to $strForwardeTo" + "\n" + "Comments" + ": " + et_comment.text.toString() + "\n\n" + "Seq. No. 1" + "\n" + "Status" + ": " + G_STATUS + "\n" + "User Account" + ": " + Comp_To_first + "\n" + "Remark" + ": " + REMINDER + "\n" + "Comment" + ": " + form_et_comment + "\n\n"
                } else {
                    var count: Int = StringUtils.countMatches(form_et_comment, "\n\n")
                    var seq_no = count + 1
                    form_et_comment =
                        "Seq. No. $seq_no" + "\n" + "Status" + ": " + strForwardedViaStatus + "\n" + "User Account" + ": " + roll + "\n" + "Remark" + ": " + "$roll to $strForwardeTo" + "\n" + "Comments" + ": " + et_comment.text.toString() + "\n\n" + form_et_comment
                }
                callForForward(
                    G_ID,
                    strForwardeTo,
                    selected_Department,
                    form_et_comment,
                    strForwardedViaStatus,
                    ASSING_TO_ID.toInt(),
                    "$roll to $strForwardeTo"
                )

            }

        }



        ll_viewAttchment = findViewById(R.id.view_attachment)
        ll_viewAttchment.setOnClickListener {
            println("clicked llattach")

            if (G_Attachment.equals("F")) {
                GenericUserFunction.DisplayToast(applicationContext, "No attachment available")
            } else if ((str_Attachment.contains(
                    ".jpg",
                    ignoreCase = true
                )) || (str_Attachment.contains(".jpeg", ignoreCase = true))
                || (str_Attachment.contains(".png", ignoreCase = true))
            ) {

                val intent = Intent(this@GrievanceFull, Common_Image_Viewer::class.java)
                intent.putExtra("url", str_Attachment)
                intent.putExtra("actionTitle", "Image Viewer")
                startActivity(intent)
            } else //Code for PDF
                if (str_Attachment.contains(".pdf", ignoreCase = true)) {
                    val intent = Intent(this@GrievanceFull, Common_PDF_Viewer::class.java)
                    intent.putExtra("url", str_Attachment)
                    intent.putExtra("actionTitle", "PDF Viewer")
                    startActivity(intent)
                }


        }

    }

    private fun callForForward(
        G_ID: String,
        strForwardeTo: String,
        selectedDepartment: String,
        comment: String,
        strForwardedViaStatus: String,
        assignToID: Int,
        remainder: String
    ) {
        try {

            mServices.GreivanceUpdate(
                G_ID,
                strForwardeTo,
                selectedDepartment,
                comment,
                strForwardedViaStatus,
                assignToID,
                remainder
            )
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        GenericUserFunction.showNegativePopUp(
                            this@GrievanceFull,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>, response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Responsecode == 200) {
                            GenericPublicVariable.CustDialog = Dialog(this@GrievanceFull)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                            var ivPosClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                            var btnOk: Button =
                                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                            var tvMsg: TextView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                            tvMsg.text = "Grievance Updated Successfully"
                            GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GrievanceFull)

                            }
                            ivPosClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GrievanceFull)

                            }

                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                ColorDrawable(Color.TRANSPARENT)
                            )
                            GenericPublicVariable.CustDialog.show()
                        } else {
                            GenericUserFunction.showNegativePopUp(this@GrievanceFull, result.Status)
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }


    private fun callForAttend(
        G_ID: String,
        strForwardeTo: String,
        selectedDepartment: String,
        comment: String,
        strForwardedViaStatus: String,
        assignToID: Int,
        remainder: String
    ) {
        try {

            mServices.GreivanceUpdate(
                G_ID.toString(),
                strForwardeTo.toString(),
                selectedDepartment.toString(),
                comment.toString(),
                strForwardedViaStatus.toString(),
                assignToID,
                remainder.toString()
            )
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        GenericUserFunction.showNegativePopUp(
                            this@GrievanceFull,
                            t.message.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<APIResponse>, response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        println("result 1 >>> " + result.toString())
                        if (result!!.Responsecode == 200) {
//                            GenericUserFunction.showPositivePopUp(
//                                this@GrievanceFull,
//                                "Grievance Updated Successfully"
//                            )
                            GenericPublicVariable.CustDialog = Dialog(this@GrievanceFull)
                            GenericPublicVariable.CustDialog.setContentView(R.layout.positive_custom_popup)
                            var ivPosClose1: ImageView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.ivCustomDialogPosClose) as ImageView
                            var btnOk: Button =
                                GenericPublicVariable.CustDialog.findViewById(R.id.btnCustomDialogAccept) as Button
                            var tvMsg: TextView =
                                GenericPublicVariable.CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView
                            tvMsg.text = "Grievance Updated Successfully"
                            GenericPublicVariable.CustDialog.setCancelable(false)
                            btnOk.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GrievanceFull)

                            }
                            ivPosClose1.setOnClickListener {
                                GenericPublicVariable.CustDialog.dismiss()
                                callSelf(this@GrievanceFull)

                            }

                            GenericPublicVariable.CustDialog.window!!.setBackgroundDrawable(
                                ColorDrawable(Color.TRANSPARENT)
                            )
                            GenericPublicVariable.CustDialog.show()

                        } else {
                            GenericUserFunction.showNegativePopUp(this@GrievanceFull, result.Status)
                        }
                    }
                })

        } catch (ex: Exception) {

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }

    private fun GetDepartmentRole(string: String) {
        var stringData = ""
//        if (string.equals("Principal")) {
//            stringData = "Dean"
//        } else {
        stringData = string
//        }
//        instituteName1.clear()
//        println("str_ToData ")
//        instituteName = "JNMC"

        mServices.GetGreivanceData(G_Instname, string)
            .enqueue(object : Callback<APIResponse> {
                override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                    GenericUserFunction.showNegativePopUp(
                        this@GrievanceFull,
                        "Please wait sever is busy"
                    )
                }


                override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                    instituteName1.add("Select User")
                    val result: APIResponse? = response.body()
                    if (result!!.Responsecode == 200) {

                        listsinstz = result?.Data18!!.size

                        ASSING_ID.add("0")
                        if (stringData.equals("HOD")) {
                            for (i in 0..listsinstz - 1) {

                                if (result.Data18!![i].MNAME.isEmpty() && result.Data18!![i].LNAME.isEmpty() && result.Data18!![i].UFMID != user_id.toString()) {
                                    hodName!!.add(result.Data18!![i].FNAME)
                                    deptName!!.add(result.Data18!![i].DEPNAM01)
                                    instituteName1.add(result.Data18!![i].FNAME + "(" + result.Data18!![i].DEPNAM01 + ")")

                                } else {
                                    if (result.Data18!![i].UFMID != user_id.toString()) {
                                        hodName!!.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME)
                                        deptName!!.add(result.Data18!![i].DEPNAM01)
                                        instituteName1.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME + "(" + result.Data18!![i].DEPNAM01 + ")")
                                    }
                                }

                                ASSING_ID.add(result.Data18!![i].UFMID)
                            }

                        } else if (stringData.equals("Principal") || stringData.equals(
                                "Dean"
                            )
                        ) {
                            for (i in 0..listsinstz - 1) {
                                if (result.Data18!![i].MNAME.isEmpty() && result.Data18!![i].LNAME.isEmpty()) {
                                    DeanPrinciName!!.add(result.Data18!![i].FNAME)
                                    PrincipalDeanClg!!.add(result.Data18!![i].DEPNAM01)
                                    deptName!!.add(result.Data18!![i].DEPNAM01)
                                    instituteName1.add(result.Data18!![i].FNAME + "(" + result.Data18!![i].INST_NAME + ")")
                                } else {
                                    DeanPrinciName!!.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME)
                                    PrincipalDeanClg!!.add(result.Data18!![i].DEPNAM01)
                                    deptName!!.add(result.Data18!![i].DEPNAM01)
                                    instituteName1.add(result.Data18!![i].FNAME + result.Data18!![i].MNAME + result.Data18!![i].LNAME + "(" + result.Data18!![i].INST_NAME + ")")
                                }
                                ASSING_ID.add(result.Data18!![i].UFMID)
                            }
                        }
                        println("result  " + result?.Data18!![0].FNAME + result?.Data18!![0].MNAME + result?.Data18!![0].LNAME)
                        var DepartmentAdap: ArrayAdapter<String> = ArrayAdapter<String>(
                            this@GrievanceFull,
                            R.layout.support_simple_spinner_dropdown_item, instituteName1
                        )
                        spinnerForwardeName!!.adapter = DepartmentAdap
                        spinnerForwardeName.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selected_user_choice = spinnerForwardeName.selectedItem.toString()
                                    var position1 = spinnerForwardeName.selectedItemPosition
                                    println("positipon 1 " + position1)
                                    if (position1 == 0) {

                                    } else {
                                        println("position code " + ASSING_ID[position])
                                        ASSING_TO_ID = ASSING_ID[position]
                                        selected_Department = deptName[position - 1]
                                        println("selected_Department code " + selected_Department)
                                    }
                                }
                            }
                        println(" Data found")

                    } else {
                        GenericUserFunction.showNegativePopUp(
                            this@GrievanceFull,
                            "No data found against your choice please select proper choices"
                        )
                    }
                }

            })

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

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun callSelf(ctx: Context) {
        val intent = Intent(ctx, ViewRegGrievHod::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }

}
