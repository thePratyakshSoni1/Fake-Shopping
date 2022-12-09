package com.example.fakeshopping.data.usersettingsdatabse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [ Settings::class ],
    version= 1
)

abstract class UserSettingsDatabase :RoomDatabase(){

    abstract val dao:SettingsDao

}