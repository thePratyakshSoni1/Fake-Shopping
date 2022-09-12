package com.example.fakeshopping

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.fakeshopping.ui.presentation.paymentscreen.PaymentScreen
import com.example.fakeshopping.ui.theme.FakeShoppingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderPaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeShoppingTheme {
                val startDestination= intent.extras?.getString("FAKESHOPPING_PAYMENT_ROUTE")!!
                PaymentScreen(stratDestination = startDestination)
            }
        }
    }
}
