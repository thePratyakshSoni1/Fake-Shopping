package com.example.fakeshopping.ui.presentation.paymentscreen

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.OrderPaymentActivity
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.razorpay.Razorpay
import com.razorpay.ValidationListener
import org.json.JSONObject

@Composable
fun PaymentScreen(stratDestination:String,razorpay: Razorpay, orderActivity:OrderPaymentActivity, amoutToBePaid:Float, currentUserId: Long) {

    val context = LocalContext.current
    val paymentWebView = WebView(context)
    paymentWebView.visibility = View.GONE

    val paymentScreenNavController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = paymentScreenNavController, startDestination = stratDestination) {

            composable(route = PaymentScreenRoutes.cardFragment) {
                razorpay.changeApiKey("rzp_test_9uZj81WaUZURYH")

                CardPaymentFragment(
                    sendRequest = { payload ->
                        sendPayRequest(payload, razorpay, paymentWebView, context)
                    },
                    amountToBePaid = amoutToBePaid,
                    currentUserId = currentUserId
                )
            }

            composable(route = PaymentScreenRoutes.upiFragment) {
                UpiPaymentFragment()
            }

            composable(route = PaymentScreenRoutes.netBankingFragment) {
                NetbankingFragment(
                    razorpay =razorpay,
                    amount = amoutToBePaid,
                    sendRequest = { payload ->
                        Log.d("TOTAL AMOUNT", amoutToBePaid.toString())
                        sendPayRequest(payload, razorpay, paymentWebView, context)
                    },
                    currentUserId
                )
            }

            composable(route = PaymentScreenRoutes.walletFragment) {
                WalletPaymentFragment()
            }


        }

        AndroidView(factory = { paymentWebView }, Modifier.fillMaxSize())
        LaunchedEffect(key1 = true, block = {
            razorpay.setWebView(paymentWebView)
        })
    }




}

private fun sendPayRequest(payload:JSONObject, razorpay: Razorpay, paymentWebView: WebView, context: Context){
    razorpay.validateFields(payload, object : ValidationListener {
        override fun onValidationSuccess() {
            try {
                paymentWebView.visibility = View.VISIBLE
                razorpay.submit(payload, OrderPaymentActivity() )
            } catch (e: Exception) {
                Log.e("Validation_Exception: ", "Exception: ", e)
            }
        }

        override fun onValidationError(error: MutableMap<String, String>?) {
            Log.d("Validation_Error:", "Validation failed: " + error?.get("field") + " " + error?.get("description"));
            Toast.makeText(context, "Validation: " + error?.get("field") + " " + error?.get("description"), Toast.LENGTH_SHORT).show();
        }

    })
}