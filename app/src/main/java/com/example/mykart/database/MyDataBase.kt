package com.example.mykart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserDetails::class,ProductList::class,Cart::class,MultiAddress::class,Orders::class], version = 1)
@TypeConverters(com.example.mykart.database.TypeConverters::class)
abstract class MyDataBase : RoomDatabase(){

    abstract fun Dao(): Dao
    companion object {
        private var instance: MyDataBase? = null
        fun getInstance(context: Context): MyDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, MyDataBase::class.java,"myDatabase")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as MyDataBase
        }
    }
}