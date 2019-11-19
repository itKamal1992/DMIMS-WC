package com.dmims.dmims.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
//    val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//    val isConnected: Boolean = activeNetwork?.isConnected == true
    companion object {
        var retrofit: Retrofit? = null
        fun getClient(baseUrl: String): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}