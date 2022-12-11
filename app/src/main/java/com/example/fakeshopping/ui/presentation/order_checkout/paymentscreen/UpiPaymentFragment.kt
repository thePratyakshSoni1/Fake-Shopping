package com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.payment_viewmodels.UpiPaymentFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.razorpay.Razorpay
import org.json.JSONObject

@Composable
fun UpiPaymentFragment(razorpay: Razorpay, currentUser:Long, sendPayRequest:(payload:JSONObject)->Unit,  amountToPay:Float){

    val viewModel = hiltViewModel<UpiPaymentFragmentViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewModel.initViewModelWithuserandRazorpay(
            razorpay, currentUser, amountToPay
        )
    })

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 36.dp, start = 12.dp), horizontalAlignment = Alignment.CenterHorizontally){

        AppTextField(
            value = viewModel.upiId,
            onValuechange = { viewModel.onUpiIdTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "UPI ID",
            textType = KeyboardType.Phone
        )

        Spacer(Modifier.height(12.dp))
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp), horizontalArrangement = Arrangement.SpaceAround){

            Button(
                onClick = {
                    viewModel.onIntentFlowClick()
                    sendPayRequest(viewModel.payload)
                          },
                modifier=Modifier.weight(1f)
            ){
                Text("Intent Flow")
            }

            Button(
                onClick = {
                    viewModel.onIntentCollectClick()
                    sendPayRequest(viewModel.payload)
                          },
                modifier=Modifier.weight(1f)
            ){
                Text("Intent Collect")
            }

        }

    }


}