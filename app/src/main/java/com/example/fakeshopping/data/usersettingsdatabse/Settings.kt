package com.example.fakeshopping.data.usersettingsdatabse

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class Settings (

    @PrimaryKey var userId:Long,

    @TypeConverters(SettingValueToString::class)
    var isAddressChangeSchemeEnabled:Boolean,
    @TypeConverters(SettingValueToString::class)
    var isEmailLettersEnabled:Boolean,
    @TypeConverters(SettingValueToString::class)
    var isOrderUpdatesnotificationsEnabled:Boolean,
    @TypeConverters(SettingValueToString::class)
    var isOtpCodesNotifictionEnabled:Boolean

)