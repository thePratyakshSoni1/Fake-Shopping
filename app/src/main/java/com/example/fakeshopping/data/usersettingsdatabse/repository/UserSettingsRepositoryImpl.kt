package com.example.fakeshopping.data.usersettingsdatabse.repository

import com.example.fakeshopping.data.usersettingsdatabse.Settings
import com.example.fakeshopping.data.usersettingsdatabse.SettingsDao

class UserSettingsRepositoryImpl(private val dao:SettingsDao): UserSettingRepository {

    override suspend fun addUserSettings(userSetting: Settings) {
        dao.addUserSettings(userSetting)
    }

    override suspend fun updateUserSetting(updatedUserSetting: Settings) {
        dao.updateUserSetting(updatedUserSetting)
    }

    override suspend fun isMailLettersEnabledForUser(userId: Long): Boolean {
        return dao.isMailLettersEnabledForUser(userId)
    }

    override suspend fun isOrderUpdatesNotificationEnabled(userId: Long): Boolean {
        return dao.isOrderUpdatesNotificationEnabled(userId)
    }

    override suspend fun isOtpCodesNotificationEnabled(userId: Long): Boolean {
        return dao.isOtpCodesNotificationEnabled(userId)
    }

    override suspend fun isAdressChangingSchemeEnabled(userId: Long): Boolean {
        return dao.isAdressChangingSchemeEnabled(userId)
    }

    override suspend fun getUserSettings(userId: String): Settings {
        return dao.getUserSettings(userId)
    }
}