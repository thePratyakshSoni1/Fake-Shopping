package com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.payment_viewmodels.WalletPaymentFragmentViewModel
import com.razorpay.Razorpay
import org.json.JSONObject


@Composable
fun WalletPaymentFragment(amount:Float, currentUser:Long, razorpay: Razorpay, sendPayRequest:(payload:JSONObject)->Unit){

    val viewModel:WalletPaymentFragmentViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        viewModel.initRazorpayAndCurrentUser(
            rPay= razorpay,
            currentUserId = currentUser,
            amount = amount
        )
    })

    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ){

        WalletOptionsList(
            walletList = viewModel.walletsLists,
            onOptionClick = {
                viewModel.onWalletChange(walletName = it)
                sendPayRequest(viewModel.payload)
            }
        )

    }

}

@Composable
private fun WalletOptionsList(walletList:List<String>, onOptionClick:(walletName:String)->Unit){

    Column {
        Text("Choose your wallet", fontSize= 16.sp, fontWeight= FontWeight.Bold, modifier= Modifier.padding(start=14.dp, top= 22.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 0.dp)) {

            for (wallet in walletList) {
                Text(
                    text = wallet,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionClick(wallet)
                        }
                        .padding(top = 12.dp, bottom = 12.dp, start= 14.dp),
                    fontSize = 16.sp
                )
            }

        }
    }

}