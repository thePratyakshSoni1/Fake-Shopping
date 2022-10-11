package com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.R
import com.example.fakeshopping.ui.model.payment_viewmodels.NetbankingFragmentViewModel
import com.razorpay.Razorpay
import org.json.JSONObject

@Composable
fun NetbankingFragment(razorpay:Razorpay, amount:Float, sendRequest:(payload:JSONObject)->Unit, curentUserId:Long ){

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
        Text("Choose your bank", fontSize= 18.sp, fontWeight= FontWeight.Bold, modifier= Modifier.padding(start=18.dp, top= 24.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .verticalScroll(rememberScrollState())) {

            for (bankName in bankNameList) {
                Row(modifier= Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clickable {
                        onClick(bankName)
                    }, verticalAlignment = Alignment.CenterVertically){

                    Row{
                        Icon( painter = painterResource(R.drawable.ic_bank), tint = Color.LightGray, contentDescription = null, modifier = Modifier.padding(start= 14.dp))
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = bankName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            fontSize = 16.sp
                        )
                    }

                }
            }

        }
    }

}