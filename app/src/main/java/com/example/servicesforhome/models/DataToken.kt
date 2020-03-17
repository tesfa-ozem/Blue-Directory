package com.example.servicesforhome.models


import com.google.gson.annotations.SerializedName

data class DataToken(
    @SerializedName("token")
    val token: String
)