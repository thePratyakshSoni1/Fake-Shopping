package com.example.fakeshopping.deliverydepartment_notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import androidx.media.app.NotificationCompat
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fakeshopping.R
import com.example.fakeshopping.otp_code_notification.OtpCode.code
import com.example.fakeshopping.otp_code_notification.OtpCodeNotificationService
import com.example.fakeshopping.otp_code_notification.OtpNotificationReceiver
import okhttp3.internal.notify

class DeliveryNotificationService(private val context:Context) {

    val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

//    fun sendNotification(message:String, orderId:Long) {
//
//
//        val copyIntent = PendingIntent.getBroadcast(
//            context,
//            1,
//            Intent(context, DeliveryNotificationReceiver::class.java),
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) PendingIntent.FLAG_IMMUTABLE else 0
//        )
//
//        val notification = androidx.core.app.NotificationCompat.Builder(context,
//            DeliveryNotificationService.deliveryNotificationChannelId
//        )
//            .setSmallIcon(R.drawable.ic_message)
//            .setContentTitle(code)
//            .setContentText("is your one time password code , don't share this with anyone")
//            .addAction(
//                R.drawable.ic_copy,
//                "View",
//                copyIntent
//            )
//            .setOngoing(false)
//            .build()
//
//        notificationManager.notify(System.currentTimeMillis().toInt(),  notification)
//
//    }


    fun sendNotification(title:String, message:String, orderId:Long){
//        val newintent = PendingIntent.getBroadcast(
//            context,
//            1,
//            Intent(context,DeliveryNotificationReceiver::class.java)
//                .putExtra("fakeshop_ORDERID", orderId),
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) PendingIntent.FLAG_IMMUTABLE else 0
//        )

        val actionIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://fakeshop.in/$orderId")
        )

        val pendingIntent = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(actionIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }

        val ourNotification = androidx.core.app.NotificationCompat.Builder(
            context,
            deliveryNotificationChannelId
        ).setSmallIcon( R.drawable.ic_cart ).setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOngoing(false).build()

        notificationManager.notify(System.currentTimeMillis().toInt(), ourNotification )
    }

    companion object{
        const val deliveryNotificationChannelId = "FAKESHOP_DELIVERY_NOTIFIACTION_CHANNEL"
    }

}