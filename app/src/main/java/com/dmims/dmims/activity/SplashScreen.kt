package com.dmims.dmims.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.DOA
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.keyAdminInfo
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.keyDrawerTitle
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.keyEnrollNo
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mobileNo
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.myCustomFont
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.myCustomFont2
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.mypref
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.password
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.permission
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.remainingPermissions
import com.dmims.dmims.Generic.GenericPublicVariable.Companion.userRole
import com.dmims.dmims.Generic.GenericUserFunction.Companion.showApiError
import com.dmims.dmims.Generic.GenericUserFunction.Companion.showInternetNegativePopUp
import com.dmims.dmims.Generic.GenericUserFunction.Companion.showInternetNegativePopUpSplash
import com.dmims.dmims.Generic.GenericUserFunction.Companion.showPerMissNegativePopUp
import com.dmims.dmims.Generic.GenericUserFunction.Companion.showSplashNegativePopUp
import com.dmims.dmims.Generic.InternetConnection
import com.dmims.dmims.R
import com.dmims.dmims.dashboard.*
import com.dmims.dmims.model.ApiVersion
import com.dmims.dmims.remote.ApiClientPhp
import com.dmims.dmims.remote.PhpApiInterface
import kotlinx.android.synthetic.main.activity_splash_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    var runnable = Runnable {
        run {
            receiveData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        myCustomFont =
            Typeface.createFromAsset(assets, "candyroundbtnbold.ttf")

        myCustomFont2 =
            Typeface.createFromAsset(assets, "robotoBold.ttf")

        tvSubHeading2.typeface = myCustomFont
        tvMainHeading.typeface = myCustomFont2
        tvMainHeading2.typeface = myCustomFont2
        if (InternetConnection.checkConnection(this@SplashScreen)) {
            try {

                var phpApiInterface: PhpApiInterface = ApiClientPhp.getClient().create(
                    PhpApiInterface::class.java
                )
                var call3: Call<ApiVersion> = phpApiInterface.api_version()
                call3.enqueue(object : Callback<ApiVersion> {
                    override fun onFailure(call: Call<ApiVersion>, t: Throwable) {
                        showInternetNegativePopUp(
                            this@SplashScreen,
                            getString(R.string.failureSSApiVerErr)
                        )
                    }

                    override fun onResponse(
                        call: Call<ApiVersion>,
                        response: Response<ApiVersion>
                    ) {
                        try {
                            val result: ApiVersion? = response.body()
                            if (result!!.response.isNotEmpty()) {
                                var pinfo = packageManager.getPackageInfo(packageName, 0)
                                var versionNumber = pinfo.versionCode
                                if (versionNumber.toString() == result.response) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        if (arePermissionsEnabled()) {
                                            var handler = Handler()
                                            handler.postDelayed(runnable, 1300)
                                        } else {
                                            requestMultiplePermissions()
                                        }
                                    } else {
                                        var handler = Handler()
                                        handler.postDelayed(runnable, 1300)
                                    }
                                } else {
                                    showSplashNegativePopUp(
                                        this@SplashScreen,
                                        getString(R.string.failureUpdateApiVerErr)
                                    )
                                }

                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            showApiError(
                                this@SplashScreen,
                                "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                            )
                        }
                    }
                })

//                mServices.AppVersion()
//                    .enqueue(object : Callback<APIResponse> {
//                        override fun onFailure(call: Call<APIResponse>, t: Throwable) {
//                            showInternetNegativePopUp(
//                                this@SplashScreen,
//                                getString(R.string.failureSSApiVerErr)
//                            )
//                        }
//
//                        override fun onResponse(
//                            call: Call<APIResponse>,
//                            response: Response<APIResponse>
//                        ) {
//                            try {
//                                val result: APIResponse? = response.body()
//                                if (result!!.Responsecode == 200 && result.Status == "ok") {
//                                    var pinfo = packageManager.getPackageInfo(packageName, 0)
//                                    var versionNumber = pinfo.versionCode
//                                    if (versionNumber.toString() == result.Data15!!.NEW_VERSION) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                            if (arePermissionsEnabled()) {
//                                                var handler = Handler()
//                                                handler.postDelayed(runnable, 1300)
//                                            } else {
//                                                requestMultiplePermissions()
//                                            }
//                                        } else {
//                                            var handler = Handler()
//                                            handler.postDelayed(runnable, 1300)
//                                        }
//                                    } else {
//                                        showSplashNegativePopUp(
//                                            this@SplashScreen,
//                                            getString(R.string.failureUpdateApiVerErr)
//                                        )
//                                    }
//                                } else {
//                                    showInternetNegativePopUp(
//                                        this@SplashScreen,
//                                        getString(R.string.failureServerApiVerErr)
//                                    )
//                                }
//                            } catch (ex: Exception) {
//                                ex.printStackTrace()
//                                showApiError(
//                                    this@SplashScreen,
//                                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
//                                )
//                            }
//                        }
//
//
//                    })
            } catch (ex: Exception) {
                ex.printStackTrace()
                showApiError(
                    this,
                    "Sorry for inconvenience\nServer seems to be busy,\nPlease try after some time."
                )
            }
        } else {
            showInternetNegativePopUpSplash(
                this@SplashScreen,
                getString(R.string.failureNoInternetErr)
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun arePermissionsEnabled(): Boolean {
        for (permission: String in permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestMultiplePermissions() {
        for (permission: String in permission) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                remainingPermissions.add(permission)
        }
        requestPermissions(
            remainingPermissions.toArray(arrayOf("" + remainingPermissions.size)),
            101
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        showPerMissNegativePopUp(
                            this@SplashScreen,
                            getString(R.string.failureStorageErr)
                        )
                    }
                    return
                } else {
                    var handler = Handler()
                    handler.postDelayed(runnable, 1300)
                }
            }

        }
    }

    private fun receiveData() {
        mypref = getSharedPreferences("mypref", Context.MODE_PRIVATE)
        keyDrawerTitle =
            mypref!!.getString("key_drawer_title", null)
        keyEnrollNo =
            mypref!!.getString("key_enroll_no", null)
        keyAdminInfo =
            mypref!!.getString("key_admin_info", null)
        mobileNo =
            mypref!!.getString("key_editmob", null)
        password =
            mypref!!.getString("key_password", null)
        userRole =
            mypref!!.getString("key_userrole", null)
        DOA = mypref!!.getString("key_doa", null)
        if (mobileNo != null || password != null || keyDrawerTitle != null || keyEnrollNo != null || keyAdminInfo != null) {
            if (userRole.equals(
                    "Student",
                    ignoreCase = true
                ) || userRole.equals("Parent", ignoreCase = true)
            ) {
                val intent = Intent(applicationContext, StudentDashboard::class.java)
                startActivity(intent)
            }
            if (userRole.equals("Admin", ignoreCase = true)) {
                val intent = Intent(applicationContext, AdminDashboard::class.java)
                startActivity(intent)
            }
            if (userRole.equals("Institute", ignoreCase = true)) {
                val intent = Intent(applicationContext, InstituteDashboard::class.java)
                startActivity(intent)
            }
            if (userRole.equals("Faculty", ignoreCase = true)) {
                val intent = Intent(applicationContext, FacultyDashboard::class.java)
                startActivity(intent)
            }

            if (userRole.equals("REGISTRAR", ignoreCase = true)) {
                val intent = Intent(applicationContext, RegisterarCellDashboard::class.java)
                startActivity(intent)
            }

            if (userRole.equals("GREVIANCE_CELL", ignoreCase = true)) {
                val intent = Intent(applicationContext, HodDashboard::class.java)
                startActivity(intent)
            }
            if (userRole.equals("EXAMINCHARGE", ignoreCase = true)) {
                val intent = Intent(applicationContext, ExamCellDashboard::class.java)
                startActivity(intent)
            }
            if((userRole!!.contains("Principal"))||(userRole!!.contains("Dean"))
                ||(userRole!!.contains("HOD",ignoreCase = true))){
                val intent = Intent(applicationContext, HodDashboard::class.java)
                startActivity(intent)
            }
//            if (userRole!!.contains("HOD", ignoreCase = true)) {
//                val intent = Intent(applicationContext, HodDashboard::class.java)
//                startActivity(intent)
//            }
        } else {
            val intent = Intent(applicationContext, RegActivity::class.java)
            startActivity(intent)
        }
    }

}
