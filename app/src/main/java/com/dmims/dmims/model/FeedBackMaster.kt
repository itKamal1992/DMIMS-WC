package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedBackMaster {

    @SerializedName("data")
    @Expose
    var Datav: ArrayList<FeedBackMasterDataRef>? = null
}