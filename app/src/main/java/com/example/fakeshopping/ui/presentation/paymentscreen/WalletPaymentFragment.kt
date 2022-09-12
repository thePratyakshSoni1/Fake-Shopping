package com.example.fakeshopping.ui.presentation.paymentscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WalletPaymentFragment(){



}

@Composable
private fun WalletOptionsList(bankList:List<String>){

    Column {
        Text("Choose your bank", fontSize= 16.sp, fontWeight= FontWeight.Bold, modifier= Modifier.padding(start=14.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)) {

            for (bank in bankList) {
                Text(
                    text = bank,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { }
                        .padding(vertical = 8.dp),
                    fontSize = 16.sp
                )
            }

        }
    }

}