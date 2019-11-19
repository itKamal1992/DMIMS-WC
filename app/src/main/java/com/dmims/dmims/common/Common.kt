package com.dmims.dmims.common

import com.dmims.dmims.remote.IMyAPI
import com.dmims.dmims.remote.RetrofitClient
//   http://103.68.25.26/dmims/api/
// "http://192.168.1.234/dmims/api/"
// Static IP Base URL class //
class Common {
    companion object {
        var BASE_URL: String = "http://103.68.25.26/dmims/api/"
        fun getAPI(): IMyAPI {
            return RetrofitClient.getClient(BASE_URL)!!.create(IMyAPI::class.java)
        }
    }
}