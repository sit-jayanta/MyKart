package com.example.mykart.classes

import com.google.gson.annotations.SerializedName

data class LoginDetails(

    @SerializedName("username")
    val username : String,

    @SerializedName("password")
    val password : String
)
