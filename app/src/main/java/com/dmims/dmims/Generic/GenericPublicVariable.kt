package com.dmims.dmims.Generic

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.dmims.dmims.R
import com.dmims.dmims.common.Common

class GenericPublicVariable {
    companion object {


        //Array
        var permission = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        //ArrayList
        var remainingPermissions: ArrayList<String> = ArrayList<String>()

        //Connection Variable
        var mServices = Common.getAPI()

        //font
        lateinit var myCustomFont: Typeface
        lateinit var myCustomFont2: Typeface

        //AlertDialog
        lateinit var CustDialog:Dialog

        //Shared Preferences var
        var mypref: SharedPreferences? = null
        var keyDrawerTitle:String? = null
        var keyEnrollNo:String? = null
        var keyAdminInfo:String? = null
        var mobileNo:String? = null
        var password:String? = null
        var userRole:String? = null
        var DOA:String? = null

        //RegActivity
        var editMobOtp: EditText? = null
        var btnGenOtp: Button? = null
        var progressbarlogin: ProgressBar? = null
    }
}