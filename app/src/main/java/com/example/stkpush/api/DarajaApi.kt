package com.example.stkpush.api

import com.example.stkpush.models.AccessTokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DarajaApi {

    // API call to get OAuth 2.0 Access Token
    @GET("oauth/v1/generate?grant_type=client_credentials")
    suspend fun getAccessToken(
        @Header("Authorization") auth: String
    ): Response<AccessTokenResponse>
}