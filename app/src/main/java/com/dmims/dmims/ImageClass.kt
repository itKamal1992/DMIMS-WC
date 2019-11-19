package com.dmims.dmims

import com.google.gson.annotations.SerializedName

class ImageClass {


    @SerializedName("title")
    private var Title:String = "-"

    @SerializedName("image")
    private var Image:String = "-"

    @SerializedName("response")
    private var Response:String = ""

    @SerializedName("uploadPath")
    private var uploadPath:String = ""

    fun getResponse(): String {
        return Response
    }

    fun getuploadPath(): String {
        return uploadPath
    }

}