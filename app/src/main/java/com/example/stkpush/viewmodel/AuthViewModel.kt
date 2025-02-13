package com.example.stkpush.viewmodel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stkpush.api.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private var accessToken: String? = null
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun fetchAccessToken(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getAccessToken(RetrofitClient.getBasicAuth())

                if (response.isSuccessful) {
                    accessToken = response.body()?.accessToken
                    onResult(accessToken)
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