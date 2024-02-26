package com.example.mykart.database

import androidx.room.TypeConverter
import com.example.mykart.classes.Geolocation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {
    @TypeConverter
    fun fromDetails(countryLang: Geolocation?): String? {
        val type = object : TypeToken<Geolocation>() {}.type
        return Gson().toJson(countryLang, type)
    }
    @TypeConverter
    fun toDetails(countryLangString: String?): Geolocation? {
        val type = object : TypeToken<Geolocation>() {}.type
        return Gson().fromJson<Geolocation>(countryLangString, type)
    }

}