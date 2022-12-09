package com.example.fakeshopping.utils

object LoginStateDataStore {

    const val dataStoreName = "FAKESHOPPING_login_state_preferences"
    const val DATASTORE_USER_LOGIN_STATE_KEY = "com.example.fakeshopping_DATSTORE_USER_LOGIN_STATE_KEY"
    const val DATASTORE_USER_ID_KEY = "com.example.fakeshopping_DATSTORE_USER_ID_KEY"

}

object SettingStateDataStore {

    data class SettingsDataStoreKeys( val key:String )

    const val dataStoreName = "FAKESHOPPING_settings_state_preferences"
    val EMAIL_LETTERS_KEY = SettingsDataStoreKeys("FAKESHOPPING_settings_email_letters_key")
    val ADDRESS_CHANGE_SCHEME_KEY = SettingsDataStoreKeys("FAKESHOPPING_address_change_scheme")
    val ORDER_UPDATES_NOTIFICATION_KEY = SettingsDataStoreKeys("FAKESHOPPING_order_updates_notification_key")
    val OTP_CODES_NOTIFICATION_KEY = SettingsDataStoreKeys("FAKESHOPPING_otpCodes_notification_key")


}