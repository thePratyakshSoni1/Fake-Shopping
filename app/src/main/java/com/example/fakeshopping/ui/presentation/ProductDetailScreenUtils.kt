package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.presentation.components.RatingBar
import kotlin.math.roundToInt

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
                .height(243.dp),
            currentImageIndex = currentImageIndex,
            listState = currentProductPrevSlideState,
            product.image,product.image
        )

        Spacer(Modifier.height(18.dp))

        ProductTextDetails(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
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

    Card(modifier=modifier, shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp), elevation = 10.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .pointerInteropFilter {
                        when (it.action) {

                            MotionEvent.ACTION_UP -> {

                                upOffsetX = it.x
                                if (upOffsetX - downOffsetX < -70f && currentImageIndex.value < (productImagesUrl.size - 1)) {
                                    currentImageIndex.value++
                                    Log.i("SWIPE", "ActionUp ++: Curr: ${currentImageIndex.value}")
                                } else if (upOffsetX - downOffsetX > 70f && currentImageIndex.value > 0) {
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
                        modifier = Modifier.fillParentMaxWidth(),
                        contentDescription = "Product Preview",
                        contentScale = ContentScale.FillHeight
                    )

                }
            }

            DottedProgressIndicator(
                    currentImageIndex,
                    productImagesUrl.size,
                    Modifier.fillMaxSize()
                )
            Spacer(Modifier.height(21.dp))
        }
    }
}

@Composable
fun DottedProgressIndicator(
    currentSlideIndex: MutableState<Int>,
    totalSlides: Int,
    modifier: Modifier
) {

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        Row (Modifier.padding(bottom=12.dp)){

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

    Column(modifier = modifier.padding(start = 14.dp, end=12.dp)) {

        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()){
            Text(
                modifier= Modifier
                    .fillMaxWidth(0.65f)
                    .padding(end = 8.dp),
                text = product.title,
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )

            Box(modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(43.dp)
                .padding(top = 8.dp), contentAlignment = Alignment.TopEnd) {
                RatingBar(
                    modifier = Modifier.fillMaxSize(),
                    starsCount = 5,
                    ratingOutOfFive = product.rating.rate.roundToInt(),
                    isSmallSize = false
                )
            }
        }

        Spacer(Modifier.height(4.dp))
        Text(
            text = product.description,
            fontFamily = FontFamily.SansSerif,
            fontSize = 14.sp,
            color = Color.Black
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

    Row(Modifier.shadow(1140.dp)) {

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
fun RecommendationSectionTitle(
    txt:String,
    modifier:Modifier = Modifier.fillMaxWidth(), onArrowClick: () -> Unit
){

    Row(modifier= modifier){
        
        Text(
            text= txt,
            modifier=Modifier.fillMaxWidth(0.8f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
        
        Box(modifier=Modifier
            .clip(CircleShape)
            .fillMaxHeight().fillMaxWidth(0.2f).clickable { onArrowClick }
            .padding(2.dp),
            contentAlignment= Alignment.CenterEnd){
            
            Image(
                imageVector = Icons.Default.ArrowForward,
                contentDescription= "See All $txt",
                modifier= Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
            
        }
        
    }

}

@Composable
fun RecommendationSectionSeeAllButton(onButtonClick: () -> Unit){

    Box(
        modifier= Modifier
            .clip(RoundedCornerShape(2.dp))
            .clickable { onButtonClick() }
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text( "SEE ALL", color = Color.Blue )
    }

}

@Composable
fun OtherProductRecommendations(
    productsList:SnapshotStateList<ShopApiProductsResponse>,
    onNavigate:(ShopApiProductsResponse)->Unit,
    onOtherSeeAllBtnClick:() -> Unit
) {
    Column(
        modifier = Modifier
            .background(Brush.linearGradient(listOf(Color(0x260055FF), Color(0x268800FF))))
    ) {

        Spacer(modifier = Modifier.height(18.dp))
        RecommendationSectionTitle(
            txt = "Other Products", modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(start = 14.dp),
            onOtherSeeAllBtnClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {

            if (productsList.isNotEmpty()) {
                items(productsList) { product ->

                    ProductsCard(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(horizontal = 4.dp)
                            .fillParentMaxWidth(0.3f),
                        product = product,
                        onNavigate = onNavigate,
                        withEleveation = false
                    )
                }
            } else {
                item {
                    LoadingView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 1f), circleSize = 39.dp
                    )
                }
            }
            item {
                Spacer(Modifier.width(4.dp))
            }
//            item {
//                RecommendationSectionSeeAllButton(onOtherSeelBtnClick)
//            }
        }
    }
    Spacer(modifier = Modifier.height(18.dp))
}

@Composable
fun RelevantProductRecommendations( productsList:SnapshotStateList<ShopApiProductsResponse>, onNavigate:(ShopApiProductsResponse)->Unit, onSeeAllBtnClick: () -> Unit) {

    Column(
        modifier= Modifier
            .padding(vertical = 23.dp)
            .background(Brush.linearGradient(listOf(Color(0x260055FF), Color(0x268800FF))))
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        RecommendationSectionTitle(
            txt = "Relevant Products", modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .padding(start = 14.dp),
            onSeeAllBtnClick
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {

            if (productsList.isNotEmpty()) {
                items(productsList) { product ->

                    ProductsCard(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillParentMaxWidth(0.3f),
                        product = product,
                        onNavigate = onNavigate,
                        withEleveation = false
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            } else {
                item {
                    LoadingView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f / 1f), circleSize = 39.dp
                    )
                }
            }

            item {
                Spacer(Modifier.width(4.dp))
            }

//            item {
//                RecommendationSectionSeeAllButton(onSellAllBtnClick)
//            }
        }
        Spacer(modifier = Modifier.height(18.dp))
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

