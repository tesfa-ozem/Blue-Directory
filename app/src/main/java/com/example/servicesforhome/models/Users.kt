package com.example.servicesforhome.models


import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String
)