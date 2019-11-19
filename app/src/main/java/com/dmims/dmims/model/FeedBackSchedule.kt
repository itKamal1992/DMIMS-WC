package com.dmims.dmims.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedBackSchedule
{
    @SerializedName("data")
    @Expose
    var Data: List<FeedBackScheduleField>? = null

}