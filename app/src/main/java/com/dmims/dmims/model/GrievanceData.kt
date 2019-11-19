package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GrievanceData {
    @SerializedName("data18")
    @Expose
    var Data: List<GrievanceDataField>? = null
}