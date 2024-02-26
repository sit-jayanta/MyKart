package com.example.mykart.interfaces

import com.example.mykart.classes.AddUser
import com.example.mykart.classes.LoginDetails
import com.example.mykart.database.ProductList
import com.example.mykart.database.UserDetails
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("auth/login")
    suspend fun authUser(@Body loginDetails: LoginDetails) : Response<UserDetails>

    @POST("users")
    suspend fun addUser(@Body addUser : UserDetails) : Response<ResponseBody>

    @GET("users")
    suspend fun allUserDetails() : Response<List<UserDetails>>

    @GET("products")
    suspend fun getAllProducts() :Response<List<ProductList>>


}