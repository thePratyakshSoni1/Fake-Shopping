package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo

@Composable
fun ProductDetailsSection(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    currentImageIndex: MutableState<Int>
) {

    val currentProductPrevSlideState = rememberLazyListState()
    val productImagesUrl = arrayOf(product.image, product.image)

    Column(modifier = modifier) {

        ProductPreviewSection(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
            currentImageIndex = currentImageIndex,
            listState = currentProductPrevSlideState,
            product.image,product.image
        )

        Spacer(
            Modifier
                .height(8.dp)
                .shadow(12.dp))

        ProductTextDetails(
            modifier = Modifier.fillMaxWidth(),
            product = product
        )

    }

    LaunchedEffect(key1 = currentImageIndex.value) {

        if (currentImageIndex.value < (productImagesUrl.size) && currentImageIndex.value >= 0) {
            currentProductPrevSlideState.animateScrollToItem(currentImageIndex.value)
            Log.i("SWIPE", "Scrolled to: ${currentImageIndex.value}")
        } else {
            Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")
        }

    }


}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductPreviewSection(
    modifier: Modifier,
    currentImageIndex: MutableState<Int>,
    listState: LazyListState,
    vararg productImagesUrl: String,
) {

    var downOffsetX = 0f
    var upOffsetX = 0f

    Box(modifier = modifier) {
        LazyRow(
            state = listState,
            modifier = Modifier
                .pointerInteropFilter {
                    when (it.action) {

                        MotionEvent.ACTION_UP -> {

                            upOffsetX = it.x
                            if (upOffsetX - downOffsetX < -100f && currentImageIndex.value < (productImagesUrl.size - 1)) {
                                currentImageIndex.value++
                                Log.i("SWIPE", "ActionUp ++: Curr: ${currentImageIndex.value}")
                            } else if (upOffsetX - downOffsetX > 100f && currentImageIndex.value > 0) {
                                currentImageIndex.value--
                                Log.i("SWIPE", "ActionUp -- : Curr: ${currentImageIndex.value}")
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
                .fillMaxWidth()) {
            items(productImagesUrl) { product ->

                Image(
                    painter = rememberAsyncImagePainter(
                        model = product, placeholder = painterResource(
                            id = R.drawable.test_product_placeholder
                        )
                    ),
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .aspectRatio(2f),
                    contentDescription = "Product Preview",
                    contentScale = ContentScale.FillBounds
                )

            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            DottedProgressIndicator(
                currentImageIndex,
                productImagesUrl.size,
                Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.height(14.dp))
    }

}

@Composable
fun DottedProgressIndicator(
    currentSlideIndex: MutableState<Int>,
    totalSlides: Int,
    modifier: Modifier
) {

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        Row {

            for (index in 0 until totalSlides) {
                if (index == currentSlideIndex.value) {

                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        Modifier
                            .shadow(12.dp)
                            .height(10.dp)
                            .width(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.DarkGray)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                } else {

                    Spacer(modifier = Modifier.width(0.5.dp))
                    Box(
                        Modifier
                            .shadow(12.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(0.5.dp))
                }
            }

        }
    }

}

@Composable
fun ProductTextDetails(modifier: Modifier, product: ShopApiProductsResponse) {

    Column(modifier = modifier.padding(horizontal = 12.dp)) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = product.title,
            fontFamily = FontFamily.SansSerif,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = product.description,
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.LightGray
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$" + product.price,
            fontFamily = FontFamily.SansSerif,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

    }

}

@Composable
fun BottomBarContent(modifier: Modifier) {

    Row(Modifier.shadow(114.dp)) {

        Box(

            modifier = Modifier
                .clickable {
                    //TODO
                }
                .weight(1f)
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp) ,
            contentAlignment = Alignment.Center

        ) {
            Text(text = "ADD TO CART", fontSize = 16.sp, color = Color.Black)

        }

        Box(
            modifier = Modifier
                .clickable {
                    //TODO
                }
                .weight(1f)
                .background(Color(0xFFFF5C28))
                .padding(horizontal = 8.dp, vertical = 12.dp) ,
            contentAlignment = Alignment.Center

        ) {
            Text(text = "BUY NOW", fontSize = 16.sp, color = Color.White)
        }
    }

}


@Composable
fun RecommendationSectionTitle(txt:String,modifier:Modifier = Modifier.fillMaxWidth()){

    Text(
        text= txt,
        modifier=modifier,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif
    )

}

@Composable
fun RecommendationSectionSeeAllButton(){

    Box(
        modifier= Modifier
            .size(69.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Blue, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "See All",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun OtherProductRecommendations(productsList:SnapshotStateList<ShopApiProductsResponse>, onNavigate:(ShopApiProductsResponse)->Unit){

    RecommendationSectionTitle(txt = "Other Products",modifier= Modifier
        .fillMaxWidth()
        .padding(start = 10.dp))
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier=Modifier.padding(start=12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        if (productsList.isNotEmpty()) {
            items(productsList) { product ->

                ProductsCard(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp)
                        .fillParentMaxWidth(0.3f),
                    product = product,
                    onNavigate = onNavigate
                )
            }
        } else {
            item {
                RecommendationSectionTitle(
                    txt = "Loading Products...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp)
                )
            }
        }

        item {
            Card(modifier = Modifier.fillParentMaxWidth(0.3f)) {
                RecommendationSectionSeeAllButton()
            }
        }
    }

}

@Composable
fun RelevantProductRecommendations( productsList:SnapshotStateList<ShopApiProductsResponse>, onNavigate:(ShopApiProductsResponse)->Unit) {

    RecommendationSectionTitle(txt = "Relevant Products",modifier= Modifier
        .fillMaxWidth()
        .padding(start = 10.dp))
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow(
        modifier = Modifier.padding(start=12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        if(productsList.isNotEmpty()){
            items(productsList) { product ->

                ProductsCard(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillParentMaxWidth(0.3f),
                    product = product,
                    onNavigate = onNavigate
                )
            }
        }else{
            item {
                RecommendationSectionTitle(txt = "Loading Products...",modifier= Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp))
            }
        }


        item {
            Card(modifier = Modifier.fillParentMaxWidth(0.3f)) {
                RecommendationSectionSeeAllButton()
            }
        }
    }

}

@Composable
fun BoxText(txt:String){

    Box(
        modifier= Modifier
            .wrapContentSize(unbounded = true)
            .padding(4.dp)
            .border(0.5.dp, Color.Blue, shape = RoundedCornerShape(2.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text= txt,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            modifier=Modifier.wrapContentSize(unbounded = true)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchByCategorySection(productCategory:List<String>,modifier:Modifier){

    //TODO

}

