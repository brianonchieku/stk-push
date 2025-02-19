package com.example.stkpush.api

import com.example.stkpush.models.AccessTokenResponse
import com.example.stkpush.models.StkPushRequest
import com.example.stkpush.models.StkPushResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DarajaApi {

    // API call to get OAuth 2.0 Access Token
    @GET("oauth/v1/generate?grant_type=client_credentials")
    suspend fun getAccessToken(
        @Header("Authorization") auth: String
    ): Response<AccessTokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    suspend fun initiateStkPush(
        @Header("Authorization") auth: String,
        @Body request: StkPushRequest
    ): Response<StkPushResponse>
}