package com.example.mykart.classes

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    val city : String,

    @SerializedName("street")
    val street : String,

    @SerializedName("number")
    val number : String,

    @SerializedName("zipcode")
    val zipcode : String,

    @SerializedName("geolocation")
    val geolocation: Geolocation


)
