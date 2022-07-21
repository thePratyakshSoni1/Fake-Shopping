package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ProductRating
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.utils.ToolbarProperties
import kotlin.math.roundToInt



@Preview(showBackground = true)
@Composable
fun ItemPreviewFunc(){

    Surface(Modifier.fillMaxSize(), color = Color.Yellow) {

        LazyColumn(contentPadding = PaddingValues(start=12.dp)) {

            items(12){ it ->

                if(it%2 == 0){
                    Box(Modifier.fillMaxWidth(0.97f)){

                        NormalHorizontalProductCard(
                            modifier = Modifier
                                .height(133.dp)
                                .fillMaxWidth(0.98f),
                            product = ShopApiProductsResponse(
                                2,"Product Name X","$69", "electronic", "xyz","www.google.com",
                                ProductRating(3f, 155)
                            ),
                            onNavigate = {  },
                            withQuantityMeter = true,
                            onRemoveBtnClick = {  },
                            8.dp
                        )

                        HorizontalProductCardButtonsLayer()

                    }
                }else{

                    Box(Modifier.fillMaxWidth(0.97f)){

                        NormalHorizontalProductCard(
                            modifier = Modifier
                                .height(133.dp)
                                .fillMaxWidth(),
                            product = ShopApiProductsResponse(
                                2,"Product Name X","$69", "electronic", "xyz","www.google.com",
                                ProductRating(3f, 155)
                            ),
                            onNavigate = {  },
                            withQuantityMeter = false,
                            onRemoveBtnClick = {  },
                            2.dp
                        )

                    }

                }
                Spacer(Modifier.height(12.dp))

            }

        }

    } }

@Composable
fun HorizontalProductCard(
    product:ShopApiProductsResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
    onRemoveBtnClick: () -> Unit
){

    if( isSelected ) {
        Box(Modifier.fillMaxWidth(0.97f)) {

            NormalHorizontalProductCard(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(126.dp)
                    .fillMaxWidth(0.98f),
                product = product,
                onNavigate = { onClick() },
                withQuantityMeter = true,
                onRemoveBtnClick = { onRemoveBtnClick() },
                elevation= 8.dp
            )

            HorizontalProductCardButtonsLayer()

        }
    }else{

        Box(Modifier.fillMaxWidth(0.97f)){

            NormalHorizontalProductCard(
                modifier = Modifier
                    .height(126.dp)
                    .fillMaxWidth(),
                product = product,
                onNavigate = { onClick() },
                withQuantityMeter = false,
                onRemoveBtnClick = { onRemoveBtnClick() },
                elevation= 2.dp
            )

        }

    }
}


@Composable
fun NormalHorizontalProductCard(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: () -> Unit,
    withQuantityMeter:Boolean,
    onRemoveBtnClick:()->Unit,
    elevation: Dp
) {

//    val imageFromUrl = rememberAsyncImagePainter(
//        model = product.image,
//        contentScale = ContentScale.FillWidth,
//        placeholder = painterResource(id = R.drawable.test_product_placeholder),
//    )

    Box(modifier = modifier.padding(vertical = 4.dp)) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            elevation = 2.dp
        ) {

            Row {

                Card(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test_product_placeholder),
                        contentDescription = "image of ${product.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    )

//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Brush.linearGradient(listOf(Color.Blue, Color.Red))),
//                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.fillMaxWidth(0.65f)) {

                        Text(
                            text = product.title,
                            overflow = TextOverflow.Clip,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "${product.price}",
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth(0.8f)
                        )
                        RatingBar(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .fillMaxWidth(0.5f),
                            starsCount = 5,
                            ratingOutOfFive = product.rating.rate.roundToInt(),
                            false
                        )

                    }

                    if(withQuantityMeter) {
                        Spacer(Modifier.width(16.dp))
                        QuantityMeter()
                    }else{

                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "remove item ${product.title}",
                                Modifier
                                    .padding(end=16.dp,start=8.dp)
                                    .clip(CircleShape)
                                    .clickable { onRemoveBtnClick() },
                                tint = Color.Red

                            )
                        }
                    }

                }

            }
        }
    }
}


@Composable
private fun HorizontalProductCardButtonsLayer(  ){

    Box(Modifier.fillMaxSize()){

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){


            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Increment Quantity",
                tint = Color.DarkGray
            )

        }


    }

}

@Composable
fun QuantityMeter(){

    Box(
        Modifier
            .border(
                width = 2.5.dp,
                shape = RoundedCornerShape(10.dp),
                brush = ToolbarProperties.CollapsedToolbarColor
            )
            ,
    ) {
            Column(Modifier.width(40.dp).height(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increment Quantity",
                    tint = Color.DarkGray,
                    modifier=Modifier.weight(1f)
                )

                Spacer(Modifier.height(4.dp))
                Text(
                        text = "1",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                    )

                Spacer(Modifier.height(4.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Increment Quantity",
                    tint = Color.DarkGray,
                    modifier=Modifier.weight(1f)
                )
                Spacer(Modifier.height(4.dp))

            }

    }
}


@Composable
fun ProductsCard(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: (ShopApiProductsResponse) -> Unit,
    withEleveation:Boolean,
    borderColor: Brush = Brush.linearGradient(listOf(Color.DarkGray, Color.DarkGray))
) {

    val imageFromUrl = rememberAsyncImagePainter(
        model = product.image,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.test_product_placeholder),
    )


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                brush = borderColor,
                width = if (withEleveation) 1.dp else 0.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onNavigate(product) },
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            modifier= Modifier
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)) {
                Card(
                    backgroundColor= Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .aspectRatio(1f / 1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp
                ) {
                    Image(
                        painter = imageFromUrl,
                        contentDescription = "image of ${product.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = product.title,
                    overflow = TextOverflow.Clip,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "$${product.price}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(8.dp))
                RatingBar(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(0.8f),
                    starsCount = 5,
                    ratingOutOfFive = product.rating.rate.roundToInt(),
                    false
                )
                Spacer(Modifier.height(6.dp))

            }

        }
    }
}

