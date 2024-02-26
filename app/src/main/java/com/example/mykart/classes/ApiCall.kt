package com.example.mykart.classes

import com.example.mykart.interfaces.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiCall {

    private var retrofit: Retrofit? = null

    private fun retrofitInstance(base : String): Retrofit?{
        if (retrofit== null){
            retrofit = Retrofit.Builder()
                .baseUrl(base)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        return retrofit
    }



    fun getService(base : String): ApiInterface? {
        val service = retrofitInstance(base)?.create(ApiInterface::class.java)
        return service
    }



}