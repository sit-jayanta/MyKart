package com.example.mykart.utils

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.mykart.MyApplication

class MyPreferences {


        companion object{
            private var sharedPreference =
                MyApplication.instance.getSharedPreferences(
                    AppConstants.My_Preference,
                    AppCompatActivity.MODE_PRIVATE)!!
            private val editor: SharedPreferences.Editor = sharedPreference.edit()
            fun setValueString(key:String,value: String){
                editor.putString(key,value)
                editor.apply()
            }
            fun setValueInt(key:String, value: Int){
                editor.putInt(key,value)
                editor.apply()

            }
            fun getValueInt(key: String, default :Int): Int {
                return sharedPreference.getInt(key,default)
            }
            fun getValueString(key: String, default: String): String? {
                return sharedPreference.getString(key,default)
            }
        }

}