package com.example.fakeshopping.deliverydepartment_notifications

import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.*
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.fakeshopping.otp_code_notification.OtpCode

class DeliveryNotificationReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("DELIVERY_NOTIF","Delivery Notification received")


//        val orderId= intent?.extras?.getLong("fakeshop_ORDERID")
//
        val actionIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://fakeshop.in/$5538278271122")
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

        pendingIntent.send()
//        Log.d("DELIVERY_NOTIF","Pending Intent sent with orderid: $orderId")



    }

//    override fun onReceive(context: Context?, intent: Intent?) {
//
//        val clipboardManager = context?.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("OTP CODE", OtpCode.code )
//        clipboardManager.setPrimaryClip(clip)
//
//        Toast.makeText(context,"Code copied ✔", Toast.LENGTH_SHORT).show()
//
//    }

}