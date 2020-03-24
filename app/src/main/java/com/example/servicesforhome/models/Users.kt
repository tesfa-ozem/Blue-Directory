package com.example.servicesforhome.models


import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("account")
        val account: List<Any>,
        @SerializedName("date_of_birth")
        val dateOfBirth: Any,
        @SerializedName("email")
        val email: String,
        @SerializedName("experience")
        val experience: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_provider")
        val isProvider: Boolean,
        @SerializedName("name")
        val name: Any,
        @SerializedName("next_of_kin")
        val nextOfKin: Any,
        @SerializedName("password_hash")
        val passwordHash: String,
        @SerializedName("path_business_license")
        val pathBusinessLicense: Any,
        @SerializedName("path_identification")
        val pathIdentification: Any,
        @SerializedName("path_photo")
        val pathPhoto: Any,
        @SerializedName("phone")
        val phone: Any,
        @SerializedName("professional_detail")
        val professionalDetail: Any,
        @SerializedName("service_documentation")
        val serviceDocumentation: Any,
        @SerializedName("username")
        val username: String
    )
}