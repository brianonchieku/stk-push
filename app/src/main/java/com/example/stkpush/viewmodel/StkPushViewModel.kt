package com.example.stkpush.viewmodel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stkpush.api.RetrofitClient
import com.example.stkpush.api.RetrofitClient.generatePassword
import com.example.stkpush.api.RetrofitClient.generateTimestamp
import com.example.stkpush.models.StkPushRequest
import com.example.stkpush.models.StkPushResponse
import kotlinx.coroutines.launch

class StkPushViewModel: ViewModel() {

    private var accessToken: String? = null
        private set

    var stkPushResponse: StkPushResponse? = null
        private set

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchAccessToken(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getAccessToken(RetrofitClient.getBasicAuth())

                if (response.isSuccessful) {
                    accessToken = response.body()?.accessToken
                    onResult(true)  // Success
                } else {
                    onResult(false) // Failure
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    fun initiateStkPush(
        phoneNumber: String,
        amount: String,
        onResult: (StkPushResponse?) -> Unit
    ) {
        if (accessToken.isNullOrEmpty()) {
            onResult(null) // No valid access token
            return
        }

        viewModelScope.launch {
            try {
                val timestamp = RetrofitClient.generateTimestamp()
                val password = RetrofitClient.generatePassword(
                    "174379",
                    "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                    timestamp
                )

                val stkPushRequest = StkPushRequest(
                    businessShortCode = "174379",
                    password = password,
                    timestamp = timestamp,
                    transactionType = "CustomerPayBillOnline",
                    amount = amount,
                    partyA = phoneNumber,
                    partyB = "174379",
                    phoneNumber = phoneNumber,
                    callbackUrl = "https://mydomain.com/path",
                    accountReference = "TestAccount",
                    transactionDesc = "Payment for test"
                )

                val response = RetrofitClient.api.initiateStkPush(
                    "Bearer $accessToken", // Use the stored token
                    stkPushRequest
                )

                if (response.isSuccessful) {
                    stkPushResponse = response.body()
                    onResult(stkPushResponse)
                } else {
                    onResult(null)
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }
}