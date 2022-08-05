package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.theme.ColorYellow

@Preview(showBackground = true)
@Composable
fun ProductCheckoutScreen() {

    Scaffold(
        topBar = { CheckoutTopBar() },
        bottomBar = {
            CheckoutScreenBottomBar()
        }
    ) {

        val itemList = mutableListOf<ShopApiProductsResponse>()

        repeat(8){
            itemList.add(
                ShopApiProductsResponse(
                    id = (it+1),
                    title = "Product Name Goes here with respect",
                    "${it}9",
                    "electronics",
                    "Very well re-furbished but best product ever",
                    "#xyz",
                    ShopApiProductsResponse.ProductRating(3.8f,150)
                )
            )
        }

        Box(modifier= Modifier
            .fillMaxSize()
            .padding(top = 64.dp), contentAlignment = Alignment.TopCenter){

            PriceDetailsCard(checkoutProductList = itemList)

        }

    }

}

@Composable
private fun CheckoutTopBar(){

    SmallTopAppBar(
        modifier = Modifier.padding(start=12.dp),
        title = {
            Text(
                "Overview",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                modifier=Modifier.padding(start=12.dp)
            )
        },
        navigationIcon = {

            com.example.fakeshopping.ui.presentation.components.IconButton(
                icon =Icons.Default.ArrowBack ,
                onClick = {  },
                contentDescription = "Go back"
            )

        },
    )


}


@Composable
private fun CheckoutScreenBottomBar(){

    Row(modifier= Modifier
        .fillMaxWidth(1f)
        .padding(vertical = 12.dp, horizontal = 12.dp)
        .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ){

        Button(
            onClick = { /*TODO*/ },
            modifier=Modifier.weight(0.16f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            border = BorderStroke(2.dp, ColorYellow)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Proceed to Payment ?",
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier=Modifier.weight(0.8f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorYellow
            )
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Proceed", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Proceed to Payment ?",
                    )
                }
            }
        }



    }

}

@Composable
private fun PriceDetailsCard(checkoutProductList:List<ShopApiProductsResponse>){

    Card(modifier= Modifier
        .fillMaxWidth(0.85f),
        shape=RoundedCornerShape(12.dp),
        elevation= 12.dp
    )
    {
        Column {
            Text(
                text = "Overview", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp,bottom=12.dp, start = 21.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Box(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(Color.White), contentAlignment = Alignment.Center){

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items(checkoutProductList) { item ->

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {

                                Text(
                                    text = item.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .padding(start = 10.dp)
                                )
                                Text(
                                    text = "$${ item.price }",
                                    maxLines = 1,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 8.dp),
                                    textAlign = TextAlign.End
                                )

                            }
                        }

                    }

                }
            }

            Card(Modifier.fillMaxWidth(), elevation = 8.dp) {
                Column(
                    Modifier
                        .background(ColorYellow)
                ) {

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {

                        Text(
                            "GST", modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 18.dp), fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$10",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {

                        Text(
                            "Total",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "$119",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
}