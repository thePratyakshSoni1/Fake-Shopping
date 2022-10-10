package com.example.fakeshopping.ui.presentation.paymentscreen

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.OrderPaymentActivity
import com.example.fakeshopping.ui.model.PaymentViewModel
import com.example.fakeshopping.ui.presentation.components.PaymentSuccesDialog
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.razorpay.PaymentResultListener
import com.razorpay.Razorpay
import com.razorpay.ValidationListener
import org.json.JSONObject

@Composable
fun PaymentScreen(stratDestination:String,razorpay: Razorpay, amoutToBePaid:Float, currentUserId: Long, onGoBack:()->Unit, itemsToBuyListString:String, itemsToBuyQuantityListString:String) {

    val viewModel:PaymentViewModel = hiltViewModel()

    viewModel.initViewModel(
        currentUserId,
        itemsToBuyListString,
        itemsToBuyQuantityListString
    )

    val context = LocalContext.current
    val paymentWebView = WebView(context)
    paymentWebView.visibility = View.GONE

    fun setPaymentWebviewVisibility(setToVisible:Boolean){
        paymentWebView.visibility = if(setToVisible) View.VISIBLE else View.INVISIBLE
    }


    val onPaymentFailure = {
        viewModel.setPaymentSuccessStatus(false)
        viewModel.setPaymentDialogVisibility(true)
    }

    val onPaymentSuccess = {
        viewModel.setPaymentSuccessStatus(true)
        viewModel.setPaymentDialogVisibility(true)
        viewModel.updateUserCartAfterPayment()
    }

    val paymentScreenNavController = rememberNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = paymentScreenNavController, startDestination = stratDestination) {

            composable(route = PaymentScreenRoutes.cardFragment) {
                razorpay.changeApiKey("rzp_test_9uZj81WaUZURYH")

                CardPaymentFragment(
                    sendRequest = { payload ->
                        sendPayRequest(
                            payload, razorpay,
                            context = context,
                            setPaymentWebViewVisibility = { setPaymentWebviewVisibility(it) },
                            onPaymentSuccess = onPaymentSuccess,
                            onPaymentFailure = onPaymentFailure
                        )
                    },
                    amountToBePaid = amoutToBePaid,
                    currentUserId = currentUserId,
                    onGoBack = {
                        onGoBack()
                    }
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
                        sendPayRequest(
                            payload, razorpay,
                            context = context,
                            setPaymentWebViewVisibility = { setPaymentWebviewVisibility(it) },
                            onPaymentSuccess = onPaymentSuccess,
                            onPaymentFailure = onPaymentFailure
                        )
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

        if(viewModel.isPaymentDialogVisible) {
            PaymentSuccesDialog(paymentSucceed = viewModel.isPaymentSuccess, onDialogueRemove = {
                viewModel.setPaymentDialogVisibility(false)
                onGoBack()
            })
        }
    }


}


private fun sendPayRequest(payload:JSONObject, razorpay: Razorpay, onPaymentSuccess:()->Unit, onPaymentFailure:()->Unit, setPaymentWebViewVisibility:(setToVisible:Boolean)->Unit, context: Context ):Boolean{

    var reqScceed = false

    razorpay.validateFields(payload, object : ValidationListener {
        override fun onValidationSuccess() {
            setPaymentWebViewVisibility(true)
            try {
                razorpay.submit(payload, object:PaymentResultListener{
                    override fun onPaymentSuccess(p0: String?) {
                        onPaymentSuccess()
                    }

                    override fun onPaymentError(p0: Int, p1: String?) {
                        onPaymentFailure()
                    }
                } )
                reqScceed = true

            } catch (e: Exception) {
                Log.e("Validation_Exception: ", "Exception: ", e)
            }

        }

        override fun onValidationError(error: MutableMap<String, String>?) {
            Log.d("Validation_Error:", "Validation failed: " + error?.get("field") + " " + error?.get("description"));
            Toast.makeText(context, "Validation: " + error?.get("field") + " " + error?.get("description"), Toast.LENGTH_SHORT).show();
        }

    })





    return reqScceed
}