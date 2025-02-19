package com.example.stkpush.api

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale

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
        val consumerKey = "VzABYh4DHXQXsQd9SusfGeeUV33UQCnYVlG3Cp27jrAvpD1E"
        val consumerSecret = "QE01WMtXUBjPYhYhtgwGAkM1QGsOjXuskqFV72zxOabDllRlGga0fzxofq96L2nr"
        val credentials = "$consumerKey:$consumerSecret"
        return "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
    }

    fun generateTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generatePassword(shortCode: String, passkey: String, timestamp: String): String {
        val dataToEncode = "$shortCode$passkey$timestamp"
        return Base64.getEncoder().encodeToString(dataToEncode.toByteArray())
    }
}