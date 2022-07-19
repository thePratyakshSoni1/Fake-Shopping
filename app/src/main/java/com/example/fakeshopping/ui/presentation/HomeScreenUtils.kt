package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil.compose.rememberAsyncImagePainter
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.presentation.components.RatingBar
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.ToolbarProperties.inDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMotionApi::class)
@Composable
fun CollapsingTopAppBar(
    motionScene: String,
    progress:MutableState<Float>,
    toolbarBackground:MutableState<Brush>,
    searchbarColor:State<Color>,
    searchText:MutableState<String>,
    categories:SnapshotStateList<String>,
    selectedCategory:MutableState<String>,
    onCategoryChange: (String) -> Unit,
    showDialog:MutableState<Boolean>
){

    MotionLayout(motionScene= MotionScene(content = motionScene), progress = progress.value,modifier=Modifier.fillMaxWidth()){

        Box(
            Modifier
                .layoutId("box")
                .background(toolbarBackground.value)
        )

        Box(
            modifier= Modifier
                .layoutId("collapsingContent")
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
                Text(
                    text = "Hello Pratyaksh",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 14.dp),
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                    textAlign = TextAlign.Center
                )

        }

        TextField(
            value = searchText.value,
            onValueChange = {
                searchText.value = it
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = searchbarColor.value,
                textColor = Color.Black,
                cursorColor = Color.Blue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .layoutId("searchbar"),
            shape = RoundedCornerShape(10.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
            ),
            placeholder = { Text("Search Products ...") }

        )

        Row(modifier=Modifier.layoutId("searchbarMenuButtons")){

            Image(
                modifier= Modifier
                    .clip(CircleShape)
                    .clickable { }
                    .padding(2.dp),
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Your Cart"
            )

            Image(
                modifier= Modifier
                    .clip(CircleShape)
                    .clickable { showDialog.value = true }
                    .padding(2.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Your Account"
            )
        }

        LazyRow(modifier=Modifier.layoutId("category"),contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(categories) { item ->

                HeaderSectionCategoryListItem(
                    isSelected = (item == selectedCategory.value),
                    categoryName = item,
                    onCategoryClick = onCategoryChange,
                    chipColor = searchbarColor
                )

            }
        }



    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BannerSlides(
    modifier: Modifier,
    bannersResource: SnapshotStateMap<String, Int>,
    userInteracted: MutableState<Boolean>
) {

    val currentDisplayBannerIndex = remember { mutableStateOf(0) }
    val bannerSlidesListState = rememberLazyListState()
    val scrollCoroutine = rememberCoroutineScope()

    var downOffsetX = 0f
    var upOffsetX = 0f


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Box{

            LazyRow( state = bannerSlidesListState,
                contentPadding= PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .pointerInteropFilter {

                        when (it.action) {

                            MotionEvent.ACTION_UP -> {

                                upOffsetX = it.x
                                if (upOffsetX - downOffsetX < -100f && currentDisplayBannerIndex.value < (bannersResource.size - 1)) {
                                    userInteracted.value = true
                                    currentDisplayBannerIndex.value++
                                } else if (upOffsetX - downOffsetX > 100f && currentDisplayBannerIndex.value > 0) {
                                    userInteracted.value = true
                                    currentDisplayBannerIndex.value--
                                } else {
                                    userInteracted.value = true
                                    Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")
                                }

                            }

                            MotionEvent.ACTION_DOWN -> {
                                downOffsetX = it.x
                            }

                        }

                        true

                    }) {

                items(bannersResource.keys.toList()) { bannerName ->

                    Spacer(Modifier.width(6.dp))
                    BannerViewItem(
                        modifier = Modifier.fillParentMaxWidth(),
                        bannerImage = painterResource(id = bannersResource[bannerName]!!)
                    )
                    Spacer(Modifier.width(6.dp))

                }
            }

            Box(modifier=Modifier.matchParentSize(), contentAlignment = Alignment.BottomCenter) {
                DottedProgressIndicator(
                    currentSlideIndex = currentDisplayBannerIndex,
                    totalSlides = bannersResource.size,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }

    }

    LaunchedEffect(key1 = currentDisplayBannerIndex.value) {

        if (currentDisplayBannerIndex.value < (bannersResource.size - 1)) swipeLeft(
            currentDisplayBannerIndex,
            scrollCoroutine,
            bannerSlidesListState
        )
        else if (currentDisplayBannerIndex.value > 0) swipeRight(
            currentDisplayBannerIndex,
            scrollCoroutine,
            bannerSlidesListState
        )
        else Log.i("SWIPE", "ActionUp: NONE CONDITION MET !")

    }

    LaunchedEffect(key1 = true, key2 = userInteracted.value) {
        Log.d("SCOPE", this.coroutineContext.toString())
        var i = 0
        while (true) {

            if (userInteracted.value) {
                Log.i("BANNER", "User interacted Delaying")
                delay(4500L)
                userInteracted.value = false
                i++
                Log.i("BANNER", "User Banner Delayed , Interacted: $i times")
            }

            delay(2400L)
            if (bannersResource.isNotEmpty() && currentDisplayBannerIndex.value >= (bannersResource.size - 1)) {
                currentDisplayBannerIndex.value = 0
                Log.i("BANNER", "COND1: Banner Set to 0/${bannersResource.size - 1}")
            } else if (bannersResource.isNotEmpty()) {
                currentDisplayBannerIndex.value++
                Log.i(
                    "BANNER",
                    "COND2: Banner Pos. Set to ${currentDisplayBannerIndex.value}/${bannersResource.size - 1} "
                )
            }
            Log.d(
                "BANNER",
                "I WAS RUNNING: ${currentDisplayBannerIndex.value}/${bannersResource.size - 1}"
            )


        }
    }

}

@Composable
fun BannerViewItem(modifier: Modifier, bannerImage: Painter, contentDescription: String? = null) {

    Card(
        shape = RoundedCornerShape(12.dp), elevation = 8.dp,
        modifier = modifier
            .aspectRatio(13f / 6f)
    ) {
        Image(
            painter = bannerImage,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun ProductsCard(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: (ShopApiProductsResponse) -> Unit,
    withEleveation:Boolean,
    borderColor:Brush = Brush.linearGradient(listOf(Color.DarkGray,Color.DarkGray))
) {

    val imageFromUrl = rememberAsyncImagePainter(
        model = product.image,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.test_product_placeholder),
    )


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(width = if(withEleveation) 1.dp else 0.dp, shape = RoundedCornerShape(12.dp), brush = borderColor)
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
                    backgroundColor=Color.White,
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

fun swipeLeft(
    currentDisplayBannerIndex: MutableState<Int>,
    scrollCoroutine: CoroutineScope,
    bannerSlidesListState: LazyListState
) {
    Log.i("SWIPE", "ActionUp: Swipe to Left")
    scrollCoroutine.launch {
        bannerSlidesListState.animateScrollToItem(currentDisplayBannerIndex.value)
    }
}

fun swipeRight(
    currentDisplayBannerIndex: MutableState<Int>,
    scrollCoroutine: CoroutineScope,
    bannerSlidesListState: LazyListState
) {
    Log.i("SWIPE", "ActionUp: Swipe to Right")
    scrollCoroutine.launch {
        bannerSlidesListState.animateScrollToItem(currentDisplayBannerIndex.value)
    }


}

@Composable
fun HeaderSectionCategoryListItem(
    isSelected: Boolean,
    categoryName: String,
    onCategoryClick: (String) -> Unit,
    chipColor: State<Color>
) {

    val backgroundColor: Color =
        if (isSelected) ColorYellow
        else chipColor.value

    val textColor: Color =
        if (isSelected) Color.White
        else Color.DarkGray

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(RoundedCornerShape(12.dp))
        .defaultMinSize(minWidth = 68.dp)
        .background(color = backgroundColor)
        .clickable {
            Log.d("CLICKED", "Category Header Section")
            onCategoryClick(categoryName)
        }
    ) {
        Text(
            text = categoryName,
            color = textColor,
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp,
            fontWeight=FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}