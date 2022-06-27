package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fakeshopping.ui.theme.Shapes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BannerSlides(modifier:Modifier, bannersResource:Map<String,Int>){

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
                        if (upOffsetX - downOffsetX < -100f) currentDisplayBannerIndex.value++
                        else if (upOffsetX - downOffsetX > 100f) currentDisplayBannerIndex.value--
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
        while (true){
            delay(2400L)
            currentDisplayBannerIndex.value++
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
fun ProductsCard(){

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