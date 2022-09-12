package com.example.fakeshopping.ui.presentation.paymentscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@Composable
fun UpiPaymentFragment(){

    val viewModel = hiltViewModel<UpiPaymentFragmentViewModel>()

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 36.dp, start=12.dp), horizontalAlignment = Alignment.CenterHorizontally){

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
        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){

            Button(
                onClick = {},
                modifier=Modifier.weight(1f)
            ){
                Text("Intent Flow")
            }

            Button(
                onClick = {},
                modifier=Modifier.weight(1f)
            ){
                Text("Intent Flow")
            }

        }

    }


}