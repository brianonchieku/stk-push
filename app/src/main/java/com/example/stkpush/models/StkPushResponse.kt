package com.example.stkpush.models

data class StkPushResponse(
    val CheckoutRequestID: String,
    val CustomerMessage: String,
    val MerchantRequestID: String,
    val ResponseCode: String,
    val ResponseDescription: String
)