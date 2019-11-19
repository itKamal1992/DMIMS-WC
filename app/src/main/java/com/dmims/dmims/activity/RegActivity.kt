package com.dmims.dmims.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.dmims.dmims.Generic.GenericPublicVariable
import com.dmims.dmims.Generic.GenericUserFunction
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.model.APIResponse
import com.dmims.dmims.model.NewUserInsert
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import dmax.dialog.SpotsDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegActivity : AppCompatActivity() {
    private lateinit var btn_NewReg: Button
    private lateinit var btn_Unable: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        GenericPublicVariable.btnGenOtp = findViewById<Button>(R.id.btn_genotp)
        btn_NewReg = findViewById<Button>(R.id.btn_NewReg)
        btn_Unable = findViewById<Button>(R.id.btn_Unable)
        GenericPublicVariable.editMobOtp = findViewById<EditText>(R.id.edit_mob_otp)
        GenericPublicVariable.btnGenOtp!!.setOnClickListener {
            if (GenericPublicVariable.editMobOtp!!.text.toString().isEmpty()) {
                Toast.makeText(this@RegActivity, "Please input mobile no.", Toast.LENGTH_SHORT).show()

            } else {

                verifyUserGenOtp(GenericPublicVariable.editMobOtp!!.text.toString())
            }
        }

        btn_NewReg.setOnClickListener {
            val intent = Intent(applicationContext, NewRegistration::class.java)
            startActivity(intent)
        }

        btn_Unable.setOnClickListener {
            val intent = Intent(applicationContext, NewUnaRegistration::class.java)
            startActivity(intent)
        }

    }

    private fun verifyUserGenOtp(mobile: String) {
        val dialog2: android.app.AlertDialog = SpotsDialog.Builder().setContext(this).build()

        dialog2.setMessage("Please Wait!!! \nwhile we are verifying OTP")
        dialog2.setCancelable(false)
        dialog2.show()
        if (InternetConnection.checkConnection(this@RegActivity)) {
            try{
            GenericPublicVariable.mServices.GetOtp(mobile)
                .enqueue(object : Callback<APIResponse> {

                    override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                        GenericUserFunction.showNegativePopUp(this@RegActivity, getString(R.string.failureSSApiVerErr))
                        dialog2.dismiss()
                    }

                    override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                        try{
                        val result: APIResponse? = response.body()
                        if (result!!.Responsecode == 204) {

                            var phpApiInterface: PhpApiInterface =
                                ApiClientPhp.getClient()
                                    .create(PhpApiInterface::class.java)
                            var call3: Call<NewUserInsert> =
                                phpApiInterface.VerifyUserDetails(
                                    GenericPublicVariable.editMobOtp!!.text.toString()
                                )

                            call3.enqueue(object :
                                Callback<NewUserInsert> {
                                override fun onFailure(
                                    call: Call<NewUserInsert>,
                                    t: Throwable
                                ) {
//                                    Toast.makeText(
//                                        this@RegActivity,
//                                        t.message,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                }

                                override fun onResponse(
                                    call: Call<NewUserInsert>,
                                    response: Response<NewUserInsert>
                                ) {
                                    val result3: NewUserInsert? =
                                        response.body()
                                    result3!!.response
                                    if (result3!!.response.equals("1")) {
                                        dialog2.dismiss()
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        intent.putExtra(
                                            "edit_mobotp",
                                            GenericPublicVariable.editMobOtp!!.text.toString()
                                        )
                                        intent.putExtra("otp_gen_user", "-")
                                        //5
                                        startActivity(intent)
                                    }
                                    if (result3!!.response.equals("2")) {
                                        //Toast.makeText(this@RegActivity, result.Status, Toast.LENGTH_SHORT).show()
                                        GenericUserFunction.showNegativePopUp(this@RegActivity, result.Status)
                                        dialog2.dismiss()
                                    }

                                }

                            })


                        } else {
                            dialog2.dismiss()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.putExtra("edit_mobotp", GenericPublicVariable.editMobOtp!!.text.toString())
                            intent.putExtra("otp_gen_user", result.Data!!.Otp)
                            startActivity(intent)
                        }

                    }
                        catch (ex: Exception) {
                            ex.printStackTrace()
                            GenericUserFunction.showApiError(
                                this@RegActivity,
                                "Sorry for inconvenience\nServer seems to be busy or you have enter wrong mobile no.,\nPlease try after some time."
                            )
                        }
                    }

                })
        } catch (ex: Exception) {
                dialog2.dismiss()
            ex.printStackTrace()
            GenericUserFunction.showApiError(
                this,
                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
            )
        }
        } else {
            GenericUserFunction.showInternetNegativePopUp(this@RegActivity, getString(R.string.failureNoInternetErr))
        }
    }



}


