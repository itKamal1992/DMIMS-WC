package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeTableData {

    @SerializedName("data")
    @Expose
    var Data: ArrayList<TimeTableDataRef>? = null
}