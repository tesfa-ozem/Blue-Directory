package com.example.servicesforhome.models


import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("Error")
    val error: String,
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("message")
    val message: String
)