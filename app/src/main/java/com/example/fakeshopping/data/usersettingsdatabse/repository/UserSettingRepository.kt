package com.example.fakeshopping.data.usersettingsdatabse.repository

import androidx.room.Query
import androidx.room.Update
import com.example.fakeshopping.data.usersettingsdatabse.Settings

interface UserSettingRepository {

    suspend fun addUserSettings(userSetting: Settings)

    suspend fun updateUserSetting( updatedUserSetting: Settings)

    suspend fun isMailLettersEnabledForUser(userId:Long):Boolean

    suspend fun isOrderUpdatesNotificationEnabled(userId:Long):Boolean

    suspend fun isOtpCodesNotificationEnabled(userId:Long):Boolean

    suspend fun isAdressChangingSchemeEnabled(userId:Long):Boolean

    suspend fun getUserSettings(userId:String):Settings

}