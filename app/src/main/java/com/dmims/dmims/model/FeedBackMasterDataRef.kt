package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedBackMasterDataRef {

    @SerializedName("FEEDBACK_NAME")
    @Expose
    var FEEDBACK_NAME: String = ""

    @SerializedName("FEEDBACK_OPTIONS")
    @Expose
    var FEEDBACK_OPTIONS: String = ""

    @SerializedName("URL")
    @Expose
    var URL: String = ""

    @SerializedName("SECTION_ID")
    @Expose
    var SECTION_ID: String = ""

    @SerializedName("ID")
    @Expose
    var ID: String = ""


}