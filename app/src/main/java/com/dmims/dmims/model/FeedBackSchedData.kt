package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedBackSchedData {

    @SerializedName("data")
    @Expose
    var Data: ArrayList<FeedBackSchedDataRef>? = null
}