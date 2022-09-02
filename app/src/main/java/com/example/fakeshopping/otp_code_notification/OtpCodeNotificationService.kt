package com.example.fakeshopping.otp_code_notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fakeshopping.R
import java.util.*
import kotlin.random.Random

class OtpCodeNotificationService(private val context: Context) {

    private val notificationmanager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    fun sendOtpCode(code:String){

        val copyIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, OtpNotificationReceiver::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, OTP_CODES_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(code)
            .setContentText("is your one time password code , don't share this with anyone")
            .addAction(
                R.drawable.ic_copy,
                "Copy",
                copyIntent
            )
            .setOngoing(false)
            .build()

        notificationmanager.notify(System.currentTimeMillis().toInt(),  notification)

    }

    companion object{
        const val OTP_CODES_CHANNEL_ID = "verification_codes_channel"
    }


}