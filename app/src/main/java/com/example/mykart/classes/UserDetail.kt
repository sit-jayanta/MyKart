package com.example.mykart.classes

import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("token")
    val token : String
)
