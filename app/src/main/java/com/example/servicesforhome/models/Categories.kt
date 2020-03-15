package com.example.servicesforhome.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class BaseResponse (


    @SerializedName("data") val data : List<Category>,
    @SerializedName("message") val message : String
)


data class Category (

    @SerializedName("icon") val icon : String,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("subcategories") val subcategories : List<Subcategories>
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.createTypedArrayList(Subcategories)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeTypedList(subcategories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}


data class Subcategories (

    @SerializedName("accounts") val accounts : List<String>,
    @SerializedName("category") val category : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("services_count") val services_count : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(accounts)
        parcel.writeInt(category)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(services_count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Subcategories> {
        override fun createFromParcel(parcel: Parcel): Subcategories {
            return Subcategories(parcel)
        }

        override fun newArray(size: Int): Array<Subcategories?> {
            return arrayOfNulls(size)
        }
    }
}

