package com.example.stkpush.models

data class StkPushRequest(
    val AccountReference: String,
    val Amount: String,
    val BusinessShortCode: String,
    val CallBackURL: String,
    val PartyA: String,
    val PartyB: String,
    val Password: String,
    val PhoneNumber: String,
    val Timestamp: String,
    val TransactionDesc: String,
    val TransactionType: String
)