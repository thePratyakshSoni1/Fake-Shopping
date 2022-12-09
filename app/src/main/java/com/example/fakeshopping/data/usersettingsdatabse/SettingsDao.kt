package com.example.fakeshopping.data.usersettingsdatabse

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface SettingsDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserSettings(userSetting:Settings)

    @Update
    suspend fun updateUserSetting( updatedUserSetting:Settings )

    @Query( "SELECT isEmailLettersEnabled FROM Settings WHERE userId =:userId" )
    suspend fun isMailLettersEnabledForUser(userId:Long):Boolean

    @Query( "SELECT isOrderUpdatesnotificationsEnabled FROM Settings WHERE userId =:userId" )
    suspend fun isOrderUpdatesNotificationEnabled(userId:Long):Boolean

    @Query( "SELECT isOtpCodesNotifictionEnabled FROM Settings WHERE userId =:userId" )
    suspend fun isOtpCodesNotificationEnabled(userId:Long):Boolean

    @Query( "SELECT isAddressChangeSchemeEnabled FROM Settings WHERE userId =:userId" )
    suspend fun isAdressChangingSchemeEnabled(userId:Long):Boolean

    @Query("SELECT * FROM Settings WHERE userId =:userId")
    suspend fun getUserSettings(userId:String):Settings



}