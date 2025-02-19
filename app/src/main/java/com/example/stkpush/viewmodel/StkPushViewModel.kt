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

    var stkPushResponse: StkPushResponse? = null
        private set

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    fun initiateStkPush(
        phoneNumber: String,
        amount: String,
        onResult: (StkPushResponse?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val timestamp = generateTimestamp()
                val password = generatePassword("YOUR_SHORTCODE", "YOUR_PASSKEY", timestamp)

                val stkPushRequest = StkPushRequest(
                    businessShortCode = "YOUR_SHORTCODE",
                    password = password,
                    timestamp = timestamp,
                    transactionType = "CustomerPayBillOnline",
                    amount = amount,
                    partyA = phoneNumber,
                    partyB = "YOUR_SHORTCODE",
                    phoneNumber = phoneNumber,
                    callbackUrl = "https://your-callback-url.com",
                    accountReference = "TestAccount",
                    transactionDesc = "Payment for test"
                )

                val response = RetrofitClient.api.initiateStkPush(
                    "Bearer ${RetrofitClient.getBasicAuth()}",
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