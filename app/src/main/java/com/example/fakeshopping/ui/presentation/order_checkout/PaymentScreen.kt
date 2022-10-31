package com.example.fakeshopping.ui.presentation.order_checkout

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
import com.example.fakeshopping.ui.model.PaymentViewModel
import com.example.fakeshopping.ui.presentation.components.PaymentSuccesDialog
import com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen.CardPaymentFragment
import com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen.NetbankingFragment
import com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen.UpiPaymentFragment
import com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen.WalletPaymentFragment
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.razorpay.PaymentResultListener
import com.razorpay.Razorpay
import com.razorpay.ValidationListener
import org.json.JSONObject

@Composable
fun PaymentScreen(onPaymentSuccessNav:()->Unit, stratDestination:String,razorpay: Razorpay, amoutToBePaid:Float, currentUserId: Long, onGoBack:()->Unit, itemsToBuyListString:String, itemsToBuyQuantityListString:String) {

    val context = LocalContext.current
    val paymentWebView = WebView(context)
    val viewModel = hiltViewModel<PaymentViewModel>()
    paymentWebView.visibility = View.GONE

    fun setPaymentWebviewVisibility(setToVisible:Boolean){
        paymentWebView.visibility = if(setToVisible) View.VISIBLE else View.INVISIBLE
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.initViewModel(
            currentUserId,
            itemsToBuyListString,
            itemsToBuyQuantityListString,
            amoutToBePaid,
            methodpath = stratDestination
        )
    })

    val onPaymentFailure = {
        viewModel.setPaymentSuccessStatus(false)
        viewModel.setPaymentDialogVisibility(true)
    }

    val onPaymentSuccess:(razorpayPaymentId:String)->Unit = {
        viewModel.updateUserCartAfterPayment(it)
        viewModel.setPaymentSuccessStatus(true)
        viewModel.setPaymentDialogVisibility(true)
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
                            onSuccessPay = onPaymentSuccess,
                            onFailurePay = onPaymentFailure
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
                            setPaymentWebViewVisibility = { setPaymentWebviewVisibility(it) },
                            context = context,
                            onSuccessPay = onPaymentSuccess,
                            onFailurePay = onPaymentFailure
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
                if(viewModel.isPaymentSuccess.value){
                    onPaymentSuccessNav()
                }else{
                    onGoBack()
                }
            })
        }
    }


}


private fun sendPayRequest(
    payload: JSONObject,
    razorpay: Razorpay,
    setPaymentWebViewVisibility: (setToVisible: Boolean) -> Unit,
    context: Context,
    onSuccessPay: (razorpayPaymentId:String) -> Unit,
    onFailurePay: () -> Unit
):Boolean{

    var reqScceed = false

    razorpay.validateFields(payload, object : ValidationListener {
        override fun onValidationSuccess() {
            setPaymentWebViewVisibility(true)
            try {
                razorpay.submit(payload, object:PaymentResultListener{
                    override fun onPaymentSuccess(razorpayPaymentId: String?) {
                        onSuccessPay(razorpayPaymentId!!)
                        Log.d("PAYMENTSUCCESS_SCR","The Resp: ${razorpayPaymentId!!}")
                    }

                    override fun onPaymentError(p0: Int, p1: String?) {
                        onFailurePay()
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