package com.example.fakeshopping

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.PaymentViewModel
import com.example.fakeshopping.ui.presentation.order_checkout.PaymentScreen
import com.example.fakeshopping.ui.theme.FakeShoppingTheme
import com.example.fakeshopping.utils.Routes
import com.razorpay.PaymentResultListener
import com.razorpay.Razorpay
import com.razorpay.ValidateVpaCallback
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

// not working - test: rzp_test_9uZj81WaUZURYH, sexret: TlPMOGgmIfFs2pzeNnNgA2pq
// working keys - test: rzp_test_mSc4yamsDh8f16, sexret: J7tP0kb0MUPB1bVqSTBuf4YE

@AndroidEntryPoint
class OrderPaymentActivity : PaymentResultListener, ComponentActivity()   {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val razorpay = Razorpay(this@OrderPaymentActivity)
        val startDestination = intent.extras?.getString("FAKESHOPPING_PAYMENT_ROUTE")!!
        val amountToBePaid = intent.extras?.getFloat("FAKESHOPPING_AMOUNT_TO_BE_PAID")!!
        val currentUserId = intent.extras?.getString("FAKESHOPPING_CURRENT_USER_ID")!!
        val itemsToBuy = intent.extras?.getString("FAKESHOPPING_PAYMENT_ITEMS_TO_BUY_LIST")!!
        val itemsToBuyQuantity = intent.extras?.getString("FAKESHOPPING_PAYMENT_ITEMS_TO_BUY_QUANTITY_LIST")!!

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
                    razorpay = razorpay,
                    amoutToBePaid = amountToBePaid,
                    currentUserId = currentUserId.toLong(),
                    onPaymentSuccessNav = {
                        onBackPressed()
                        MainActivity.getMainNavController().navigate("${Routes.homeScreen}?category={category}"){
                            popUpTo("${Routes.homeScreen}?category={category}") { inclusive = true }
                        }
                    },
                    onGoBack={
                        onBackPressed()
                    },
                    itemsToBuyListString = itemsToBuy,
                    itemsToBuyQuantityListString = itemsToBuyQuantity
                )
            }
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) { }

    override fun onPaymentError(p0: Int, p1: String?) { }


}
