package com.example.stkpush.api

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64

object RetrofitClient {

    private const val BASE_URL = "https://sandbox.safaricom.co.ke/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: DarajaApi = retrofit.create(DarajaApi::class.java)

    // Function to generate Base64 encoded Consumer Key and Secret
    @RequiresApi(Build.VERSION_CODES.O)
    fun getBasicAuth(): String {
        val consumerKey = "YOUR_CONSUMER_KEY"
        val consumerSecret = "YOUR_CONSUMER_SECRET"
        val credentials = "$consumerKey:$consumerSecret"
        return "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
    }
}