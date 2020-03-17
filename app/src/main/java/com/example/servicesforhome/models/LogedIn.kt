package com.example.servicesforhome.models


import com.google.gson.annotations.SerializedName

data class LogedIn(
    @SerializedName("message")
    val message: String
)