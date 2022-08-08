package com.example.fakeshopping.data.userdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fakeshopping.data.userdatabase.dao.UserDao

@Database(
    entities = [ Users::class ],
    version = 1
)

abstract class UserDatabase: RoomDatabase() {

    abstract val dao:UserDao

}