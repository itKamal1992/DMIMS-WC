package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeTableDataRef {

    @SerializedName("INSTITUTE")
    @Expose
    var INSTITUTE: String = ""

    @SerializedName("SEMESTER")
    @Expose
    var SEMESTER: String = ""

    @SerializedName("YEAR")
    @Expose
    var YEAR: String = ""

    @SerializedName("TYPE")
    @Expose
    var TYPE: String = ""

    @SerializedName("URL")
    @Expose
    var URL: String = ""

    @SerializedName("TIME_TABLE_NAME")
    @Expose
    var TIME_TABLE_NAME: String = ""

    @SerializedName("ID")
    @Expose
    var ID: String = ""


}