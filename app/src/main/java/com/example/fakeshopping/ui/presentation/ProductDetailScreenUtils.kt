package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import com.example.fakeshopping.R
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.data.ShopApiProductsResponse

@Composable
fun ProductDetailsSection(modifier:Modifier, product: ShopApiProductsResponse, currentImageIndex: MutableState<Int>){


    Column(modifier=modifier) {

        ProductPreviewSection(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            productImagesUrl = arrayOf(product.image, product.image),
            currentImageIndex = currentImageIndex
        )

        Spacer(Modifier.height(8.dp))

        ProductTextDetails(
            modifier = Modifier.fillMaxWidth(),
            product = product
        )

    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductPreviewSection(modifier:Modifier, vararg productImagesUrl:String, currentImageIndex: MutableState<Int>){

    var downOffsetX = 0f
    var upOffsetX = 0f

    Box(modifier=modifier, contentAlignment = Alignment.BottomCenter){
        LazyRow(
            Modifier
                .pointerInteropFilter {
                    when (it.action) {

                        MotionEvent.ACTION_UP -> {

                            upOffsetX = it.x
                            if (upOffsetX - downOffsetX < -100f && currentImageIndex.value < (productImagesUrl.size - 1)) {
                                currentImageIndex.value++
                            } else if (upOffsetX - downOffsetX > 100f && currentImageIndex.value > 0) {
                                currentImageIndex.value--
                            } else {
                                Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")
                            }

                        }

                        MotionEvent.ACTION_DOWN -> {
                            downOffsetX = it.x
                        }

                    }

                    true

                }
                .fillMaxWidth()){
            items(productImagesUrl) { product ->

                Image(
                    painter = rememberAsyncImagePainter(model = product, placeholder = painterResource(
                        id = R.drawable.test_product_placeholder
                    )),
                    modifier= Modifier
                        .fillParentMaxWidth()
                        .aspectRatio(2f) ,
                    contentDescription = "Product Preview",
                    contentScale = ContentScale.Fit
                )

            }
        }

        DottedProgressIndicator( currentImageIndex ,productImagesUrl.size, Modifier.fillMaxSize())
        Spacer(Modifier.height(8.dp))
    }

}

@Composable
fun DottedProgressIndicator(currentSlideIndex:MutableState<Int>, totalSlides:Int, modifier:Modifier){

    Box(modifier=modifier, contentAlignment = Alignment.BottomCenter){
        Row(modifier=Modifier.fillMaxSize()) {

            for(index in 0 until totalSlides){
                if(index==currentSlideIndex.value){
                    Box(
                        Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray)
                            .padding(horizontal = 4.dp)
                            .border(1.dp, Color.Black))
                }else{
                    Box(
                        Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .padding(2.dp))
                }
            }

        }
    }

}

@Composable
fun ProductTextDetails(modifier: Modifier,product:ShopApiProductsResponse){

    Column(modifier = modifier.padding(horizontal = 12.dp)) {

        Text(text=product.title, fontFamily = FontFamily.SansSerif, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(text=product.description, fontFamily = FontFamily.SansSerif, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        Spacer(Modifier.height(8.dp))
        Text(text="$"+product.price, fontFamily = FontFamily.SansSerif, fontSize = 20.sp, fontWeight = FontWeight.Black)

    }

}

@Composable
fun BottomBarContent(modifier:Modifier) {

    Row() {

        Box(

            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .clickable {
                    //TODO
                }
        ) {
            Text(text = "ADD TO CART", fontSize = 16.sp, color = Color.Black)

        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color(0xFFFF5C28))
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .clickable {
                    //TODO
                }
        ) {
            Text(text = "BUY NOW", fontSize = 16.sp, color = Color.White)
        }
    }

}