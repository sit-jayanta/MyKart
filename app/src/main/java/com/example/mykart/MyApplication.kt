package com.example.mykart

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        instance=this
    }
    companion object{
        lateinit var instance : Application
            private set
    }
}