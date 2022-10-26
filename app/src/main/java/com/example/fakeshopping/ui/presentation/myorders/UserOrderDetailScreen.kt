package com.example.fakeshopping.ui.presentation.myorders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.ui.model.UserOrderDetailsViewModel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.presentation.components.SimpleAppScreensTopBar
import com.example.fakeshopping.utils.PaymentOptionId
import com.google.android.gms.wallet.WalletConstants.PaymentMethod


@Composable
fun UserOrderDetailScreen( currentUserId:String, orderId:Long, onBackPress:()->Unit ){

    val viewModel = hiltViewModel<UserOrderDetailsViewModel>()
    LaunchedEffect(key1 = true, block = {
        viewModel.setCurrentUserAndOrder(currentUserId, orderId)
    })

    Scaffold(
        modifier= Modifier.fillMaxSize(),
        topBar = { SimpleAppScreensTopBar(title = "Order #${orderId}", onBackPress) }
    ) {

        Spacer(Modifier.height(12.dp))
        if(viewModel.order.value != null) {
            OrderSummaryCard(
                order = viewModel.order.value!!,
                totalItems = viewModel.totalItems.value
            )
        }else{
            LoadingView(modifier = Modifier.fillMaxHeight(0.4f), circleSize = 32.dp)
        }
    }

}

@Composable
private fun OrderSummaryCard(order: UserOrders, totalItems:Int) {

    Card(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(top = 14.dp, bottom = 12.dp, start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            OrderSummaryCardTextItem(heading = "Total items", value = totalItems.toString())
            Spacer(Modifier.height(4.dp))
            OrderSummaryCardTextItem(heading = "Order Date", value = order.orderDateTime)
            Spacer(Modifier.height(4.dp))
            OrderSummaryCardTextItem(heading = "Total Amount", value = order.totalAmountPaid.toString())
            Spacer(Modifier.height(4.dp))
            OrderSummaryCardTextItem( heading = "Method: ",
                value = when(order.paymentMethod){
                    PaymentOptionId.OPTION_CARD.id -> { "Card" }
                    PaymentOptionId.OPTION_POD.id -> { "POD" }
                    PaymentOptionId.OPTION_UPI.id -> { "UPI" }
                    PaymentOptionId.OPTOIN_WALLET.id -> { "Wallet" }
                    PaymentOptionId.OPTOIN_NETBANKING.id -> { "Net Banking" }
                    else -> { "Error" }
                }
            )
            Spacer(Modifier.height(4.dp))
            OrderSummaryCardTextItem(heading = "Address", value = order.orderDeliveryAddress)
            Spacer(Modifier.height(4.dp))
            OrderSummaryCardTextItem(heading = "Status", value = if(order.orderDelivered == true) "Delivered" else "Peding")
            Spacer(Modifier.height(4.dp))

        }

    }

}

@Composable
private fun OrderSummaryCardTextItem(heading:String, value:String){
    Row(
        modifier=Modifier.fillMaxWidth()
    ){
        Text(
            modifier=Modifier.fillMaxWidth(0.35f),
            text = "$heading:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
        Spacer(Modifier.width(4.dp))
        Text(
            modifier=Modifier.fillMaxWidth().weight(1f),
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
    }
}