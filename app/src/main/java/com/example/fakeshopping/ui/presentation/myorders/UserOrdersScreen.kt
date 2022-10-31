package com.example.fakeshopping.ui.presentation.myorders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.ui.model.UserOrdersViewModel
import com.example.fakeshopping.ui.presentation.components.RatingBar
import com.example.fakeshopping.ui.presentation.components.SimpleAppScreensTopBar
import com.example.fakeshopping.ui.theme.ColorYellow
import kotlin.math.roundToInt

@Composable
fun UserOrdersScreen(currentUserId:String, onBackPress:()->Unit, goToOrderDetails:(orderId:Long)->Unit){

    val viewModel = hiltViewModel<UserOrdersViewModel>()
    var oldDate = ""
    LaunchedEffect(key1 = true){
        viewModel.setCurrentUser(currentUserId.toLong())
    }

    Scaffold(
        topBar = { SimpleAppScreensTopBar(title = "My Orders", onBackPress ) },
        modifier=Modifier.fillMaxSize()
    ){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            LazyColumn(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ){
                items(viewModel.userOrders){ order ->
                    Column( horizontalAlignment = Alignment.CenterHorizontally ){
                        if(oldDate != order.orderDateTime) {
                            oldDate = order.orderDateTime
                            Spacer(Modifier.height(12.dp))
                            Text(
                                modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ColorYellow).padding(vertical=4.dp, horizontal= 6.dp),
                                text = order.orderDateTime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                        UserOrderItem(
                            order = order,
                            orderProduct = viewModel.userOrderProductDetail
                                .find {
                                    it.id ==  order.productId[0]
                                }!!,
                            goToOrderDetails = goToOrderDetails
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }

}


@Composable
private fun UserOrderItem(order:UserOrders, orderProduct:ShopApiProductsResponse,  goToOrderDetails:(orderId:Long)->Unit){

    Card(
        modifier= Modifier
            .fillMaxWidth(0.95f)
            .clickable { goToOrderDetails(order.orderId) },
        shape= RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = orderProduct.image,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.test_product_placeholder),
                ),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .weight(1f)
                    .aspectRatio(1f),
            )
            Spacer(Modifier.width(8.dp))

            Column(modifier=Modifier.weight(2f)) {

                Text(
                    text = order.orderId.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )

                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (order.orderDelivered == true) "Delivered" else "Pending",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    color = if (order.orderDelivered == true) Color.Green else ColorYellow
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = orderProduct.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$${orderProduct.price}",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
                Spacer(Modifier.height(4.dp))
                RatingBar(
                    modifier = Modifier.fillMaxWidth(),
                    starsCount = 5,
                    ratingOutOfFive = orderProduct.rating.rate.roundToInt(),
                    isSmallSize = true
                )

            }
        }
    }
}