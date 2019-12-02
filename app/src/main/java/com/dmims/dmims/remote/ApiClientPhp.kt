package com.dmims.dmims.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import javax.xml.datatype.DatatypeConstants.MINUTES
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.logging.HttpLoggingInterceptor






class ApiClientPhp {
    companion object {
       // var PhpBaseUrl: String = "http://avbrh.gearhostpreview.com/"
      var PhpBaseUrl: String = "http://dmimsdu.in/web/"
        var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {

//                val gson = GsonBuilder()
//                    .setLenient()
//                    .create()
                var okHttpClient = OkHttpClient.Builder()

                    .retryOnConnectionFailure(true)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

//                val interceptor = HttpLoggingInterceptor()
//                interceptor.level = HttpLoggingInterceptor.Level.BODY
//                val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()




                retrofit = Retrofit.Builder()

                    .baseUrl(PhpBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }


    }
}