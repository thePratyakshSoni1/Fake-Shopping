package com.example.fakeshopping.otp_code_notification

import android.app.Service
import android.content.*
import android.widget.Toast

class OtpNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val clipboardManager = context?.getSystemService(Service.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("OTP CODE", OtpCode.code )
        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(context,"Code copied ✔", Toast.LENGTH_SHORT).show()

    }

}