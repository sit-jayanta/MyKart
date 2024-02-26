package com.example.mykart.classes

import com.google.gson.annotations.SerializedName

data class AddUser(

    @SerializedName("email")
    val email : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("password")
    val password : String,

    @SerializedName("name")
    val name : Name,

    @SerializedName("address")
    val address : Address,

    @SerializedName("phone")
    val phone : String


)
