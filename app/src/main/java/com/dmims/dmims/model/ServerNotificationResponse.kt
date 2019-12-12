package com.dmims.dmims.model

import com.google.gson.annotations.SerializedName

class ServerNotificationResponse {

     var multicast_id:String?=null
     var success:String?=null
     var failure:String?=null
     var canonical_ids:String?=null
     var results: List<MessegeIdToken>? = null

}