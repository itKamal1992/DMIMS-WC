package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedBackSchedDataRef {

    @SerializedName("INSTITUTE_NAME")
    @Expose
    var INSTITUTE_NAME: String = ""

    @SerializedName("COURSE_NAME")
    @Expose
    var COURSE_NAME: String = ""

    @SerializedName("SCHEDULE_DATE")
    @Expose
    var SCHEDULE_DATE: String = ""

    @SerializedName("FEEDBACK_NAME")
    @Expose
    var FEEDBACK_NAME: String = ""

    @SerializedName("START_DATE")
    @Expose
    var START_DATE: String = ""

    @SerializedName("END_DATE")
    @Expose
    var END_DATE: String = ""

    @SerializedName("ID")
    @Expose
    var ID: String = ""

    @SerializedName("DEPT_NAME")
    @Expose
    var DEPT_NAME: String = ""

}