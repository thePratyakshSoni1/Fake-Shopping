package com.example.fakeshopping.ui.presentation

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.util.toRange
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.theme.Shapes
import kotlinx.coroutines.*


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BannerSlides(modifier:Modifier, bannersResource: SnapshotStateMap<String, Int>, userInteracted:MutableState<Boolean>){

    val currentDisplayBannerIndex = remember{ mutableStateOf(0) }
    val bannerSlidesListState = rememberLazyListState()
    val scrollCoroutine = rememberCoroutineScope()

    var downOffsetX = 0f
    var upOffsetX = 0f


    Column(
        horizontalAlignment = Alignment.CenterHorizontally ,
        modifier = modifier
    ){
        
        LazyRow( state = bannerSlidesListState, modifier= Modifier
            .padding(horizontal = 4.dp)
            .pointerInteropFilter {

                when (it.action) {
                    MotionEvent.ACTION_UP -> {

                        upOffsetX = it.x
                        if (upOffsetX - downOffsetX < -100f && currentDisplayBannerIndex.value < (bannersResource.size - 1)) currentDisplayBannerIndex.value++
                        else if (upOffsetX - downOffsetX > 100f && currentDisplayBannerIndex.value > 0) currentDisplayBannerIndex.value--
                        else Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")

                    }

                    MotionEvent.ACTION_DOWN -> {
                        downOffsetX = it.x
                    }
                }

                true

            } ){
            
            items(bannersResource.keys.toList()){ bannerName ->
                BannerViewItem(modifier = Modifier.fillMaxHeight(0.75f),
                    bannerImage = painterResource(id = bannersResource[bannerName]!!)
                )
            }
        }
        
    }

    LaunchedEffect(key1 = currentDisplayBannerIndex.value ){

        if (currentDisplayBannerIndex.value < (bannersResource.size - 1))  swipeLeft(currentDisplayBannerIndex, scrollCoroutine, bannerSlidesListState)
        else if (currentDisplayBannerIndex.value > 0) swipeRight(currentDisplayBannerIndex, scrollCoroutine, bannerSlidesListState)
        else Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")

    }

    LaunchedEffect(key1 = true ){
        Log.d("SCOPE",this.coroutineContext.toString())
        var i = 1
        while (true){

//            if(userInteracted.value) {
//                delay(2500L)
//                userInteracted.value = false
//                i++
//            }

            delay(2400L)
            if(bannersResource.isNotEmpty() && currentDisplayBannerIndex.value >= (bannersResource.size - 1)) {
                currentDisplayBannerIndex.value = 0
                Log.i("BANNER","COND1: Banner Set to 0/${bannersResource.size-1}")
            }else if(bannersResource.isNotEmpty()){
                currentDisplayBannerIndex.value++
                Log.i("BANNER","COND2: Banner Pos. Set to ${currentDisplayBannerIndex.value}/${bannersResource.size-1} ")
            }
            Log.d("BANNER","I WAS RUNNING: ${currentDisplayBannerIndex.value}/${bannersResource.size-1}")
        }
    }
    
}

@Composable
fun BannerViewItem(modifier: Modifier, bannerImage: Painter, contentDescription:String? = null){
    
    Card(modifier= modifier
        .aspectRatio(13f / 6f)
        .padding(horizontal = 4.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
        Image(painter = bannerImage, contentDescription =  contentDescription, contentScale = ContentScale.FillBounds)
    }
    
}

@Composable
fun CategoriesSection(){

}

@Composable
fun ProductsCard(modifier: Modifier, product:ShopApiProductsResponse, painter: Painter = painterResource(R.drawable.test_product_placeholder), onNavigate:()->Unit){

    Box(modifier=modifier, contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.clickable { onNavigate() }.fillMaxSize(), elevation = 8.dp) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "image of ${product.title}",
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .fillMaxWidth()
                )

                Text(
                    text = product.title,
                    overflow = TextOverflow.Clip,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 16.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "$${product.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

            }

        }
    }
}

fun swipeLeft(currentDisplayBannerIndex:MutableState<Int>, scrollCoroutine:CoroutineScope, bannerSlidesListState:LazyListState) {
    Log.i("SWIPE", "ActionUp: Swipe to Left")
    scrollCoroutine.launch {
        bannerSlidesListState.animateScrollToItem(currentDisplayBannerIndex.value)
    }
}

fun swipeRight(currentDisplayBannerIndex:MutableState<Int>, scrollCoroutine:CoroutineScope, bannerSlidesListState:LazyListState) {
    Log.i("SWIPE", "ActionUp: Swipe to Right")
    scrollCoroutine.launch {
        bannerSlidesListState.animateScrollToItem(currentDisplayBannerIndex.value)
    }


}

@Composable
fun HeaderSectionCategoryListItem(isSelected:Boolean, categoryName:String, onCategoryClick:(String)->Unit){

    val backgroundColor:Color =
        if(isSelected) Color.White
        else Color.Blue

    val textColor:Color =
        if(isSelected) Color.Black
        else Color.White

    Box(contentAlignment = Alignment.BottomCenter
        ,modifier= Modifier
            .padding(horizontal = 4.dp)
            .defaultMinSize(minWidth = 46.dp)
            .clickable {
                onCategoryClick(categoryName)
            }
    ) {
        Card( backgroundColor = backgroundColor, shape = RoundedCornerShape(topStart=8.dp,topEnd=8.dp), elevation = 0.dp){
            Text(text = categoryName, color = textColor, fontFamily = FontFamily.SansSerif,fontSize = 14.sp, modifier=Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }

}