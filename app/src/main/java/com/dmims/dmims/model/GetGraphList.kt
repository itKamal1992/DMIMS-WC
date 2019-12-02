package com.dmims.dmims.model

import com.dmims.dmims.dataclass.GraphFields
import com.dmims.dmims.dataclass.McqFields
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetGraphList {

    @SerializedName("data")
    @Expose
    var Data: ArrayList<GraphFields>? = null
}