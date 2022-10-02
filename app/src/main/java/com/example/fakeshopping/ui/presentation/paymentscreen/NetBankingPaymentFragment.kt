package com.example.fakeshopping.ui.presentation.paymentscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.payment_viewmodels.NetbankingFragmentViewModel
import com.razorpay.Razorpay
import org.json.JSONObject

@Composable
fun NetbankingFragment(razorpay:Razorpay, amount:Float, sendRequest:(payload:JSONObject)->Unit, curentUserId:Long){

    val viewModel = hiltViewModel<NetbankingFragmentViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewModel.initRaorpayForNetbanking(razorpay, amount, currentUserId = curentUserId)
    })

    Box(modifier=Modifier.fillMaxSize()){
        BanksOptionsList(
            bankNameList = viewModel.banksNameList,
            onClick = { banknameIndex ->
                viewModel.onBankChange(banknameIndex)
                sendRequest(viewModel.payload)
            }
        )
    }

}

@Composable
private fun BanksOptionsList(bankNameList:List<String>, onClick:(index:String)->Unit){

    Column {
        Text("Choose your bank", fontSize= 16.sp, fontWeight= FontWeight.Bold, modifier= Modifier.padding(start=14.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .verticalScroll(rememberScrollState())) {

            for (bankName in bankNameList) {
                Text(
                    text = bankName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(bankName)
                        }
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    fontSize = 16.sp
                )
            }

        }
    }

}