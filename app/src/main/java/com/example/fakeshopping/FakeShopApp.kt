package com.example.fakeshopping

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.fakeshopping.deliverydepartment_notifications.DeliveryNotificationService
import com.example.fakeshopping.otp_code_notification.OtpCodeNotificationService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FakeShopApp : Application(){

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channelOtpCodes = NotificationChannel(
                OtpCodeNotificationService.OTP_CODES_CHANNEL_ID,
                "OTP codes",
                NotificationManager.IMPORTANCE_HIGH
            )
            channelOtpCodes.description = "Used to receieve simulated OTP codes"

            val channelDeliveryUpdates = NotificationChannel(
                DeliveryNotificationService.deliveryNotificationChannelId,
                "Delivery Department Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            channelDeliveryUpdates.description = "Updating you with your order delivery status"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(
                listOf( channelOtpCodes, channelDeliveryUpdates )
            )

        }

    }


}