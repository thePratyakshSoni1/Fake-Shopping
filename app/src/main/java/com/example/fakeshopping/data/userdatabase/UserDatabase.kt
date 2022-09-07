package com.example.fakeshopping.data.userdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.fakeshopping.data.userdatabase.dao.UserDao

@Database(
    entities = [ Users::class ],
    version = 1
)

@TypeConverters(IntListTypeConverters::class, MapTypeConverters::class, UserOrderListTypeConverters::class, UserAddressToStringConverters::class)

abstract class UserDatabase: RoomDatabase() {

    abstract val dao:UserDao

}