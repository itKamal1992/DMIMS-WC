package com.dmims.dmims.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.R
import com.dmims.dmims.common.Common
import com.dmims.dmims.dashboard.*
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.NewUserInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var edit_mob: EditText
    private lateinit var edit_password: EditText
    private lateinit var mServices: IMyAPI
    private lateinit var btn_login: Button
    private var set_status_frm: String = ""
    private var hod_name: String = ""
    var listsuser: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //reteriveData()
        setContentView(R.layout.activity_main)

        mServices = Common.getAPI()
        val mob = intent.getStringExtra("edit_mobotp")
        val otp_gen_user = intent.getStringExtra("otp_gen_user")

//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
//        val mob = intent.getStringExtra("d")
        //  txt_register = findViewById<TextView>(R.id.txt_register)
        edit_mob = findViewById(R.id.edit_mob)
        edit_password = findViewById(R.id.edit_password)
        btn_login = findViewById(R.id.btn_login)
        edit_mob.setText(mob)

        if (otp_gen_user.equals("-")) {
            set_status_frm = "myDb"
        } else {
            set_status_frm = "Orcl"
            edit_password.setText(otp_gen_user)
        }

        btn_login.setOnClickListener {
            if (set_status_frm.equals("Orcl")) {
                authenticateUser(edit_mob.text.toString(), edit_password.text.toString())
            }
            if (set_status_frm.equals("myDb")) {
                authenticateUsermyDb(edit_mob.text.toString(), edit_password.text.toString())
            }

        }
    }


    private fun authenticateUser(mobile: String, password: String) {
        try {
            val dialog: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
            dialog.setMessage("Please Wait!!! \nwhile we are authenticating your details")
            dialog.setCancelable(false)
            dialog.show()
            mServices.VerifyOtp(mobile, password)
                .enqueue(object : Callback<APIResponse> {
                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        dialog.dismiss()
                        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<APIResponse>,
                        response: Response<APIResponse>
                    ) {
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {
                            dialog.dismiss()
                            Toast.makeText(this@MainActivity, result.Status, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            if (result.Data1?.USER_ROLE        != null && result.Data1?.USER_ROLE.equals(
                                    "Student",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, StudentDashboard::class.java)
                                println(" course >>> " + result.Data1!!.COURSE_ID!!.get(0))
                                saveData(
                                    result.Data1!!.USER_ROLE,
                                    result.Data1!!.NAME,
                                    result.Data1!!.STUDENTID + " / " + result.Data1!!.PUNCH_ID,
                                    result.Data1!!.STUDENTID,
                                    result.Data1!!.DOA,
                                    result.Data1!!.COURSE_ID!!.get(0),
                                    result.Data1!!.ROLL_NO

                                )
                                intent.putExtra("NAME", result.Data1!!.NAME)
                                intent.putExtra(
                                    "STUD_INFO",
                                    result.Data1!!.STUDENTID + " / " + result.Data1!!.PUNCH_ID
                                )
                                intent.putExtra("STUD_ID_KEY", result.Data1!!.STUDENTID)
                                startActivity(intent)
                            }
                            if (result.Data2?.USER_ROLE != null && result.Data2?.USER_ROLE.equals("Parent", ignoreCase = true))
                            {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, StudentDashboard::class.java)

                                saveData(
                                    result.Data2!!.USER_ROLE,
                                    result.Data2!!.GARDIAN_NAME,
                                    result.Data2!!.NAME,
                                    result.Data2!!.STUDENTID,
                                    "-",
                                    result.Data1!!.COURSE_ID!!.get(0),
                                    "-"
                                )
                                intent.putExtra("NAME", result.Data2!!.GARDIAN_NAME)
                                intent.putExtra("STUD_INFO", result.Data2!!.NAME)
                                intent.putExtra("STUD_ID_KEY", result.Data2!!.STUDENTID)
                                startActivity(intent)
                            }
                            if (result.Data3?.USER_ROLE != null && result.Data3?.USER_ROLE.equals(
                                    "Faculty",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, FacultyDashboard::class.java)
                                saveData(
                                    result.Data3!!.USER_ROLE,
                                    result.Data3!!.NAME,
                                    result.Data3!!.FACULTY_ID,
                                    "-",
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "ADMIN",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    "",
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-",
                                    result.Data4!!.DESIGNATION,
                                    result.Data4!!.EMAIL
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.ROLL != null && result.Data4?.ROLL!!.contains("HOD",ignoreCase = true)) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, HodDashboard::class.java)

                                if (result.Data4!!.MNAME == null && result.Data4!!.LNAME== null) {
                                    hod_name = result.Data4!!.FNAME
                                } else {

                                    hod_name = result.Data4!!.FNAME + result.Data4!!.MNAME + result.Data4!!.LNAME
                                    }
                                saveData(
                                    result.Data4!!.ROLL,
                                    hod_name,
                                    result.Data4!!.DEPNAM01,
                                    result.Data4!!.UFMID,
                                    "-",
                                    "-",
                                    result.Data4!!.INST_NAME,
                                    result.Data4!!.DESIG,
                                    result.Data4!!.EMAIL
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "INSTITUTE",
                                    ignoreCase = true
                                )
                            )
                            {
                                dialog.dismiss()
                                println(" result >>> " + result.Data4)

                                mServices.CheckPrincipal(mobile)
                                    .enqueue(object : Callback<APIResponse> {
                                        override fun onFailure(
                                            call: Call<APIResponse>,
                                            t: Throwable
                                        ) {
                                            GenericUserFunction.DisplayToast(this@MainActivity,t.message!!)

                                        }

                                        override fun onResponse(
                                            call: Call<APIResponse>,
                                            response: Response<APIResponse>
                                        ) {
                                            val result2: APIResponse? = response.body()
                                            if (result2!!.Responsecode == 200) {


                                                if((result.Data4!!.USER_ROLE.equals(result2.Data19!!.ROLL))||(result2.Data19!!.ROLL==null)){
                                                    // Call normal flow
                                                    saveData(
                                                        result.Data4!!.USER_ROLE,//
                                                        result.Data4!!.ADMIN_NAME,
                                                        "",
                                                        result.Data4!!.ID,
                                                        "-",
                                                        result.Data4!!.COURSE_ID,
                                                        result.Data4!!.INSTITUTE_NAME,
                                                        result.Data4!!.DESIGNATION,
                                                        result.Data4!!.EMAIL

                                                    )

                                                    val intent =Intent(applicationContext, InstituteDashboard::class.java)
                                                    startActivity(intent)
                                                }
                                                else
                                                {
                                                    CheckUser(result.Data4!!.USER_ROLE,result2.Data19!!.ROLL,result,result2)

                                                }

                                            }
                                            else {
                                                Toast.makeText(this@MainActivity, result.Status, Toast.LENGTH_SHORT)
                                                    .show()

                                            }
                                        }

                                    }
                                    )


//                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "REGISTRAR",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, RegisterarCellDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "GREVIANCE_CELL",
                                    ignoreCase = true
                                )
                            )
                            {
                                dialog.dismiss()
                                println(" result >>> " + result.Data4)

                                mServices.CheckPrincipal(mobile)
                                    .enqueue(object : Callback<APIResponse> {
                                        override fun onFailure(
                                            call: Call<APIResponse>,
                                            t: Throwable
                                        ) {
                                            GenericUserFunction.DisplayToast(this@MainActivity,t.message!!)

                                        }

                                        override fun onResponse(
                                            call: Call<APIResponse>,
                                            response: Response<APIResponse>
                                        ) {
                                            val result2: APIResponse? = response.body()
                                            if (result2!!.Responsecode == 200) {


                                                if((result.Data4!!.USER_ROLE.equals(result2.Data19!!.ROLL))||(result2.Data19!!.ROLL==null)){
                                                    // Call normal flow
                                                    saveData(
                                                        result.Data4!!.USER_ROLE,//
                                                        result.Data4!!.ADMIN_NAME,
                                                        "",
                                                        result.Data4!!.ID,
                                                        "-",
                                                        result.Data4!!.COURSE_ID,
                                                        result.Data4!!.INSTITUTE_NAME,
                                                        result.Data4!!.DESIGNATION,
                                                        result.Data4!!.EMAIL

                                                    )

                                                    val intent =Intent(applicationContext, HodDashboard::class.java)
                                                    startActivity(intent)
                                                }
                                                else
                                                {
                                                    CheckUser(result.Data4!!.USER_ROLE,result2.Data19!!.ROLL,result,result2)

                                                }

                                            }
                                            else {
                                                Toast.makeText(this@MainActivity, result.Status, Toast.LENGTH_SHORT)
                                                    .show()

                                            }
                                        }

                                    }
                                    )


//                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "CONVENER",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "COCONVENER",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent = Intent(applicationContext, AdminDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    result.Data4!!.ID,
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-"
                                )
                                startActivity(intent)
                            }
                            if (result.Data4?.USER_ROLE != null && result.Data4?.USER_ROLE.equals(
                                    "EXAMINCHARGE",
                                    ignoreCase = true
                                )
                            ) {
                                dialog.dismiss()
                                val intent =
                                    Intent(applicationContext, ExamCellDashboard::class.java)
                                saveData(
                                    result.Data4!!.USER_ROLE,
                                    result.Data4!!.ADMIN_NAME,
                                    "",
                                    result.Data4!!.ID,
                                    "-",
                                    "-",
                                    "-",
                                    result.Data4!!.DESIGNATION,
                                    result.Data4!!.EMAIL
                                )
                                startActivity(intent)
                            }


                        }
                    }
                })
        } catch (ex: Exception)
        {

            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
    }


    private fun authenticateUsermyDb(mobile: String, password: String) {
        try {
            val dialog2: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()
            dialog2.setMessage("Please Wait!!! \nwhile we are authenticating your details")
            dialog2.setCancelable(false)
            dialog2.show()

            var phpApiInterface: PhpApiInterface =
                ApiClientPhp.getClient()
                    .create(PhpApiInterface::class.java)
            var call3: Call<NewUserInsert> =
                phpApiInterface.VerifyOtpMob(
                    mobile, password
                )

            call3.enqueue(object :
                Callback<NewUserInsert> {
                override fun onFailure(
                    call: Call<NewUserInsert>,
                    t: Throwable
                ) {
                    dialog2.dismiss()
                    Toast.makeText(
                        this@MainActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<NewUserInsert>,
                    response: Response<NewUserInsert>
                ) {
                    dialog2.dismiss()
                    val result3: NewUserInsert? =
                        response.body()
                    result3!!.response
                    var arrSplit: List<String> = result3!!.response.split("SEPARATOR")
                    if (arrSplit[0].equals("1")) {
                        dialog2.dismiss()
//NAME,STUDENT_ID,COURSE_ID,ROLLNO
                        val intent = Intent(applicationContext, StudentDashboard::class.java)
                        saveData(
                            "Student",
                            arrSplit[1],
                            arrSplit[2],
                            arrSplit[2],
                            "-",
                            arrSplit[3],
                            arrSplit[4]

                        )
                        intent.putExtra("NAME", arrSplit[1])
                        intent.putExtra(
                            arrSplit[2],
                            arrSplit[2]
                        )
                        intent.putExtra("STUD_ID_KEY", arrSplit[2])
                        startActivity(intent)


                    }
                    if (arrSplit[0].equals("2")) {
                        //Toast.makeText(this@RegActivity, result.Status, Toast.LENGTH_SHORT).show()
                        GenericUserFunction.showNegativePopUp(
                            this@MainActivity,
                            "Please Input Correct Mobile number and Password"
                        )
                        dialog2.dismiss()
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

    private fun saveData(
        USER_ROLE: String,
        NAME: String,
        STUD_INFO: String,
        STUD_ID: String,
        DOA: String,
        courseid_1: String,
        roll_no: String
    ) {
        if (edit_mob.text.isEmpty()) {
            edit_mob.error = "Please enter Mobile Number"
            return
        }
        if (edit_password.text.isEmpty()) {
            edit_password.error = "Please enter password"
            return
        }
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
        editor.putString("key_editmob", edit_mob.text.toString().trim())
        editor.putString("key_password", edit_password.text.toString().trim())
        editor.putString("key_drawer_title", NAME)
        editor.putString("key_enroll_no", STUD_INFO)
        editor.putString("Stud_id_key", STUD_ID)
        editor.putString("key_userrole", USER_ROLE)
        editor.putString("key_doa", DOA)
        editor.putString("course_id", courseid_1)
        editor.putString("roll_no", roll_no)
        editor.apply()
    }

    private fun saveData(
        USER_ROLE: String,
        NAME: String,
        Department: String,
        STUD_ID: String,
        ExtraField_2: String,
        courseid_1: String,
        Institute: String,
        Designation: String,
        Email: String
    ) {
        if (edit_mob.text.isEmpty()) {
            edit_mob.error = "Please enter Mobile Number"
            return
        }
        if (edit_password.text.isEmpty()) {
            edit_password.error = "Please enter password"
            return
        }
        val mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        val editor = mypref.edit()
        editor.putString("Stud_id_key", STUD_ID)
        editor.putString("key_drawer_title", NAME)
        editor.putString("key_editmob", edit_mob.text.toString().trim())
        editor.putString("key_userrole", USER_ROLE)
        editor.putString("key_institute", Institute)
        editor.putString("key_email", Email)
        editor.putString("key_designation", Designation)
        editor.putString("course_id", courseid_1)
        editor.putString("key_password", edit_password.text.toString().trim())

        editor.putString("key_Department", Department)
        editor.putString("key_extraField_2", ExtraField_2)

        editor.apply()
    }

    fun CheckUser(userone:String,usertwo:String,result1: APIResponse,result2: APIResponse){
    var CustDialog = Dialog(this)
    CustDialog.setContentView(R.layout.dialog_check_user)
    var ivNegClose1: ImageView = CustDialog.findViewById(R.id.ivCustomDialogNegClose) as ImageView
    var btnRoleOne: Button = CustDialog.findViewById(R.id.btnRoleOne) as Button
    var btnRoleTwo: Button = CustDialog.findViewById(R.id.btnRoleTwo) as Button
    var tvMsg: TextView = CustDialog.findViewById(R.id.tvMsgCustomDialog) as TextView

        btnRoleOne.text="Login as $userone"
        btnRoleTwo.text="Login as $usertwo"


    tvMsg.text = "Select User Type"
//    GenericPublicVariable.CustDialog.setCancelable(false)
    btnRoleOne.setOnClickListener {
        CustDialog.dismiss()
        Ins_LogInAsUserOne(result1)

    }
    btnRoleTwo.setOnClickListener {
        CustDialog.dismiss()
        Ins_LogInAsUserTwo(result2)
    }
    ivNegClose1.setOnClickListener {
        CustDialog.dismiss()

    }
    CustDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    CustDialog.show()

    }

    private fun Ins_LogInAsUserOne(result1: APIResponse) {


        if(result1.Data4!!.USER_ROLE.equals("GREVIANCE_CELL"))
        {
            saveData(
            result1.Data4!!.USER_ROLE,//
            result1.Data4!!.ADMIN_NAME,
            "",
            result1.Data4!!.UFMID,
            "-",
            result1.Data4!!.COURSE_ID,
            result1.Data4!!.INSTITUTE_NAME,
            result1.Data4!!.DESIGNATION,
            result1.Data4!!.EMAIL

        )
            val intent = Intent(applicationContext, HodDashboard::class.java)
            startActivity(intent)
        }
        else if(result1.Data4!!.USER_ROLE.equals("INSTITUTE",ignoreCase = true)) {

            saveData(
                result1.Data4!!.USER_ROLE,//
                result1.Data4!!.ADMIN_NAME,
                "",
                result1.Data4!!.ID,
                "-",
                result1.Data4!!.COURSE_ID,
                result1.Data4!!.INSTITUTE_NAME,
                result1.Data4!!.DESIGNATION,
                result1.Data4!!.EMAIL

            )

            val intent = Intent(applicationContext, InstituteDashboard::class.java)
            startActivity(intent)
        }
    }

    fun Ins_LogInAsUserTwo(result2:APIResponse){
        if (result2.Data19!!.MNAME == null && result2.Data19!!.LNAME== null) {
            hod_name = result2.Data19!!.FNAME
        } else {

            hod_name = result2.Data19!!.FNAME + result2.Data19!!.MNAME + result2.Data19!!.LNAME
        }
var str_ComplaintToGriev:String=""
        if (result2.Data19!!.INST_NAME == "SRMMCON" || result2.Data19!!.INST_NAME == "RNPC") {
            str_ComplaintToGriev = "Principal"

        } else {
            str_ComplaintToGriev = "Dean"
        }


        if((str_ComplaintToGriev.contains("Principal"))||(str_ComplaintToGriev.contains("Dean"))
            ||(result2.Data19!!.ROLL.contains("HOD",ignoreCase = true))) {
            saveData(
                str_ComplaintToGriev,
                hod_name,
                result2.Data19!!.DEPNAM01,
                result2.Data19!!.UFMID,
                "-",
                "-",
                result2.Data19!!.INST_NAME,
                result2.Data19!!.DESIG,
                result2.Data19!!.EMAIL
            )
            val intent = Intent(applicationContext, HodDashboard::class.java)
            startActivity(intent)
        }
    }
}

