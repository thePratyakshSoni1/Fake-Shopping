package com.example.fakeshopping.ui.presentation.myorders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.ui.model.UserOrderDetailsViewModel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.presentation.components.RatingBar
import com.example.fakeshopping.ui.presentation.components.SimpleAppScreensTopBar
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.OrderDeliveryStatus
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties
import com.google.android.gms.wallet.WalletConstants.PaymentMethod
import kotlin.math.roundToInt


@Composable
fun UserOrderDetailScreen( currentUserId:String, orderId:Long, onBackPress:()->Unit, rootnavController:NavController){

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
            Column(horizontalAlignment = Alignment.CenterHorizontally){


                LazyColumn(content = {

                    item{

                        OrderSummaryCard(
                            order = viewModel.order.value!!,
                            totalItems = viewModel.totalItems.value
                        )
                        Spacer(Modifier.height(18.dp))

                    }

                    items(viewModel.orderProductDetails.keys.toList()){ it ->
                        OrderSummaryProductItemsDetails(
                            product = it, quantity = viewModel.orderProductDetails[it]!!,
                            onReturn = {  } ,
                            onviewProduct = { rootnavController.navigate("${Routes.productDetailScreen}/$it") }
                        )

                        Spacer(Modifier.height(8.dp))
                    }

                },
                    modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                )

            }
        }else{
            LoadingView(modifier = Modifier.fillMaxHeight(), circleSize = 32.dp)
        }
    }

}

@Composable
private fun OrderSummaryCard(order: UserOrders, totalItems:Int) {

    Column{

        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(top = 14.dp, bottom = 14.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp
        ) {

            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    OrderSummaryCardTextItem(heading = "Total items", value = totalItems.toString())
                    Spacer(Modifier.height(4.dp))
                    OrderSummaryCardTextItem(heading = "Order Date", value = order.orderDateTime)
                    Spacer(Modifier.height(4.dp))
                    OrderSummaryCardTextItem(heading = "Total Amount", value = order.totalAmountPaid.toString())
                    Spacer(Modifier.height(4.dp))
                    OrderSummaryCardTextItem(heading = "Payment Id", value = order.razorpayPaymentId)
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
                    OrderSummaryCardTextItem(heading = "Status",
                        value = if(order.orderDeliveryStatus == OrderDeliveryStatus.STATUS_DELIVERED.value) "Delivered" else "Pending")
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd=12.dp))
                        .background(ToolbarProperties.CollapsedToolbarColorBrush)
                ) {
                    Spacer(Modifier.height(14.dp))
                    Box(
                        Modifier.fillMaxWidth().padding(horizontal = 42.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        var progress =  when(order.orderDeliveryStatus){
                            OrderDeliveryStatus.STATUS_DELIVERED.value -> 1.0f
                            OrderDeliveryStatus.STATUS_SHIPPED.value -> 0.5f
                            OrderDeliveryStatus.STATUS_PLACED.value -> 0.1f
                            else -> { 0f }
                        }

                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxWidth().padding(horizontal= 4.dp),
                            color = ColorYellow,
                            backgroundColor = Color.White
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                Modifier.clip(CircleShape)
                                    .size( if( progress > 0f ) 18.dp else 12.dp)
                                    .background( if( progress > 0f ) ColorYellow else Color.White)
                            )
                            Box(
                                Modifier.clip(CircleShape).size(18.dp)
                                    .size( if(progress >= 0.5f ) 18.dp else 12.dp)
                                    .background( if(progress >= 0.5f ) ColorYellow else Color.White)
                            )
                            Box(
                                Modifier.clip(CircleShape).size(12.dp)
                                    .size( if(progress == 1f ) 18.dp else 12.dp)
                                    .background( if(progress == 1f ) ColorYellow else Color.White)
                            )
                        }
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Order Placed",
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f).padding(vertical = 6.dp)
                        )
                        Text(
                            text = "Shipped",
                            fontSize = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f).padding(vertical = 6.dp)
                        )
                        Text(
                            text = "Delivered",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            modifier = Modifier.weight(1f).padding(vertical = 6.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }

            }

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
            modifier= Modifier
                .fillMaxWidth()
                .weight(1f),
            text = value,
            fontSize = 14.sp,
            textAlign = TextAlign.Left
        )
    }
}

@Composable
private fun OrderSummaryProductItemsDetails( product:ShopApiProductsResponse, quantity:Int, onReturn:()->Unit, onviewProduct:(id:Int)->Unit ){

    val productImageRes = rememberAsyncImagePainter(
        model = product.image,
        placeholder = painterResource(id = R.drawable.test_product_placeholder)
    )

    Card( modifier= Modifier
        .fillMaxWidth(0.95f),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ){

        Column(Modifier.fillMaxWidth()){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ){

                Spacer(Modifier.width(8.dp))
                Image(
                    painter = productImageRes,
                    contentDescription = null,
                    modifier= Modifier
                        .width(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .aspectRatio(1f)
                )

                Spacer( Modifier.width(12.dp) )

                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){

                    Text(
                        text= product.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth()){
                        Box(Modifier.weight(1f)){
                            OrderSummaryCardTextItem(heading = "Qty", value = quantity.toString())
                        }
                        Spacer(modifier = (Modifier.width(8.dp)))
                        Box(Modifier.weight(1f)) {
                            OrderSummaryCardTextItem(heading = "Price", value = "$${product.price}")
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    RatingBar(
                        modifier = Modifier
                            .fillMaxWidth(0.4f),
                        starsCount = 5, ratingOutOfFive = product.rating.rate.roundToInt(), isSmallSize = false
                    )

                }


            }
            Spacer(Modifier.height(12.dp))
            ProductDetailCardButtons(onViewProduct = { onviewProduct(product.id) }, onReturnItem = onReturn)
        }

    }

}

@Composable
private fun ProductDetailCardButtons( onViewProduct:()->Unit, onReturnItem:()->Unit ){

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

        Button(
            onClick = { onViewProduct() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorYellow
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevation(0.dp)
        ) {
            Text(
                "VIEW PRODUCT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        Button(
            onClick = { onReturnItem() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(3.dp, ColorYellow),
            elevation = ButtonDefaults.elevation(
                pressedElevation = 0.4.dp,
                defaultElevation = 0.dp,
                focusedElevation = 0.dp
            )
        ) {
            Text(
                "RETURN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }

}