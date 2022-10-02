package com.example.fakeshopping

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fakeshopping.ui.presentation.paymentscreen.PaymentScreen
import com.example.fakeshopping.ui.theme.FakeShoppingTheme
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.google.gson.Gson
import com.razorpay.PaymentResultListener
import com.razorpay.Razorpay
import com.razorpay.ValidateVpaCallback
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


// test: rzp_test_9uZj81WaUZURYH, sexret: TlPMOGgmIfFs2pzeNnNgA2pq

@AndroidEntryPoint
class OrderPaymentActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val razorpay = Razorpay(this@OrderPaymentActivity)
        val startDestination = intent.extras?.getString("FAKESHOPPING_PAYMENT_ROUTE")!!
        val amountToBePaid = intent.extras?.getFloat("FAKESHOPPING_AMOUNT_TO_BE_PAID")!!
        val currentUserId = intent.extras?.getString("FAKESHOPPING_CURRENT_USER_ID")!!

        setContent {
            FakeShoppingTheme {
                val context = LocalContext.current

                LaunchedEffect(key1 = true, block = {
                    razorpay.isValidVpa("pratyakshsmarty@okhdfcbank", object : ValidateVpaCallback {
                        override fun onResponse(b: JSONObject?) {
//                            if (b)
                            Log.d("RAZOR", b.toString())
                            Toast.makeText(context, "VPA is valid", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(context, "VPA is Not valid", Toast.LENGTH_LONG).show();
                        }

                        override fun onFailure() {
                            Toast.makeText(
                                context,
                                "Error in validating VPA/UPI ID",
                                Toast.LENGTH_LONG
                            ).show();
                        }

                    })

                })
                PaymentScreen(
                    stratDestination = startDestination,
                    razorpay,
                    this@OrderPaymentActivity,
                    amountToBePaid,
                    currentUserId = currentUserId.toLong()
                )
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }
}
