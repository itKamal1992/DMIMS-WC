package com.dmims.dmims.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
//    val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//    val isConnected: Boolean = activeNetwork?.isConnected == true
    companion object {
        var retrofit: Retrofit? = null
        fun getClient(baseUrl: String): Retrofit? {
            val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}