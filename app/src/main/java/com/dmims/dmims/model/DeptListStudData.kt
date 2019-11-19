package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeptListStudData {

    @SerializedName("data")
    @Expose
    var Data: ArrayList<DeptListStudDataRef>? = null
}