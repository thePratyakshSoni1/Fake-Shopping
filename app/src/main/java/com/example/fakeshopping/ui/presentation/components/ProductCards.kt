package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties
import kotlin.math.roundToInt

@Composable
fun ProductsCard(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: (ShopApiProductsResponse) -> Unit,
    isFavourite: Boolean?,
    onFavouriteButtonClick: (productId:Int) -> Unit

) {

    val imageFromUrl = rememberAsyncImagePainter(
        model = product.image,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.test_product_placeholder),
    )


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onNavigate(product) },
        contentAlignment = Alignment.Center
    ) {

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            modifier= Modifier
                .padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)) {
                Card(
                    backgroundColor= Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp
                ) {
                    Box(Modifier.fillMaxSize()){

                        Image(
                            painter = imageFromUrl,
                            contentDescription = "image of ${product.title}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )

                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(end = 8.dp, top = 8.dp), contentAlignment= Alignment.TopEnd){

                            IconButton(
                                icon = if(isFavourite == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                onClick = { onFavouriteButtonClick(product.id) },
                                contentDescription = if(isFavourite == true) "Remove to favourite" else "Add from favourites",
                                iconTint = if(isFavourite == true) Color(0xFFFF0048) else Color.LightGray
                            )

                        }

                    }
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


@Composable
fun SelectableHorizontalProductCard(
    product: ShopApiProductsResponse,
    isSelectionMode: State<Boolean>,
    toggleSelectionMode:(Boolean)->Unit,
    isSelectedItemListEmpty:()->Boolean,
    addNewSelectedItem:()->Unit,
    removeSelectedItem:(Int)->Unit,
    checkSelectedItemAvailability:()->Boolean,
    onNavigate: () -> Unit,
    onFavouriteButtonClick:()->Unit,
    alwaysVisibleQuantityMeter: Boolean,
    isFavourite: Boolean?
){
    Box(
        Modifier
            .padding(start = 12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { _ ->
                        if (!isSelectionMode.value) {
                            toggleSelectionMode(true)
                            addNewSelectedItem()
                        } else {
                            if (checkSelectedItemAvailability()) {
                                removeSelectedItem((product.id))
                                if (isSelectedItemListEmpty()) {
                                    toggleSelectionMode(false)
                                }
                            } else {
                                addNewSelectedItem()
                            }
                        }
                    },

                    onTap = { _ ->

                        if (isSelectionMode.value) {

                            if (checkSelectedItemAvailability()) {
                                removeSelectedItem(product.id)
                                if (isSelectedItemListEmpty()) {
                                    toggleSelectionMode(false)
                                }
                            } else {
                                addNewSelectedItem()
                            }

                        } else {
                            onNavigate()
                        }

                    }

                )
            }
    ) {
        HorizontalProductCard(
            product = product,
            isSelected = checkSelectedItemAvailability(),
            onClick = { Unit },
            onFavouriteButtonClick = {
                onFavouriteButtonClick()
            },
            alwaysVisibleQuantityMeter = alwaysVisibleQuantityMeter,
            itemQuantity = remember { mutableStateOf(1) },
            hasFavouriteButton = !isSelectionMode.value,
            isFavourite = isFavourite
        )
    }
}

@Composable
fun HorizontalProductCard(
    product:ShopApiProductsResponse,
    isSelected: Boolean,
    onClick: () -> Unit,
    onFavouriteButtonClick: () -> Unit,
    itemQuantity: MutableState<Int>,
    alwaysVisibleQuantityMeter: Boolean,
    hasFavouriteButton: Boolean,
    isFavourite: Boolean?
) {

        Box(Modifier.fillMaxWidth(0.97f)) {

            NormalHorizontalProductCardWithActionButtons(
                modifier = Modifier
//                    .padding(top = 10.dp)
                    .height(126.dp)
                    .fillMaxWidth(0.98f),
                product = product,
                onNavigate = { onClick() },
                withQuantityMeter = ( alwaysVisibleQuantityMeter || isSelected ) ,
                onFavouriteBtnClick = onFavouriteButtonClick,
                elevation = if(isSelected) 8.dp else 2.dp,
                itemQuantity = itemQuantity,
                hasFavouriteButton = hasFavouriteButton,
                isFavourite = isFavourite
            )

            if(isSelected) {
                HorizontalProductCardButtonsLayer()
            }
        }

}


@Composable
private fun NormalHorizontalProductCardWithActionButtons(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: () -> Unit,
    withQuantityMeter:Boolean,
    onFavouriteBtnClick:()->Unit,
    elevation: Dp,
    itemQuantity:MutableState<Int>,
    hasFavouriteButton:Boolean,
    isFavourite:Boolean?
) {

    val imageFromUrl = rememberAsyncImagePainter(
        model = product.image,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.test_product_placeholder),
    )

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

                    Box(Modifier.fillMaxSize()){
                        Image(
                            painter = imageFromUrl,
                            contentDescription = "image of ${product.title}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )

                        if(hasFavouriteButton) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                                IconButton(
                                    icon = if(isFavourite!!) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                    onClick = onFavouriteBtnClick,
                                    contentDescription = "Favourite Button",
                                    iconTint = if(isFavourite!!) Color(0xFFFF0059) else Color.White
                                )
                            }
                        }


                    }

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
                            text = product.price,
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

                    if (withQuantityMeter) {
                        Spacer(Modifier.width(16.dp))
                        QuantityMeter(itemQuantity)
                    }

                }

            }
        }
    }
}


@Composable
fun NormalHorizontalProductCard(
    modifier: Modifier,
    product: ShopApiProductsResponse,
    onNavigate: () -> Unit,
    isFavourite: Boolean,
    onFavouriteButtonClick: (Int) -> Unit
) {

    /** User Must Define Height And Width of Card using the modifier parameter **/

    val imageFromUrl = rememberAsyncImagePainter(
        model = product.image,
        contentScale = ContentScale.FillWidth,
        placeholder = painterResource(id = R.drawable.test_product_placeholder),
    )

    Box(modifier = modifier.padding(vertical = 4.dp)) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onNavigate() },
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
                    Box(modifier=Modifier.fillMaxSize()){

                        Image(
                            painter = imageFromUrl,
                            contentDescription = "image of ${product.title}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )

                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
                            IconButton(
                                icon = if(isFavourite!!) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                onClick = { onFavouriteButtonClick(product.id) },
                                contentDescription = "Favourite Button",
                                iconTint = if(isFavourite!!) Color(0xFFFF0059) else Color.White
                            )
                        }

                    }

                }

                Spacer(Modifier.width(8.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp)) {

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
                        text = product.price,
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
                    tint = Color.Blue
                )

            }


        }

    }




    @Composable
    private fun QuantityMeter(currentQuantity:MutableState<Int>){

        Box(
            Modifier
                .border(
                    width = 2.5.dp,
                    shape = RoundedCornerShape(10.dp),
                    brush = ToolbarProperties.CollapsedToolbarColorBrush
                )
            ,
        ) {
            Column(
                Modifier
                    .width(40.dp)
                    .height(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(4.dp))

                Box(Modifier.weight(1f)) {
                    IconButton(
                        icon = Icons.Default.KeyboardArrowUp,
                        onClick = { currentQuantity.value++ },
                        contentDescription = "Decrease Quantity",
                        iconTint = Color.DarkGray
                    )
                }

                Spacer(Modifier.height(4.dp))
                Text(
                    text = currentQuantity.value.toString(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(Modifier.height(4.dp))

                Box(Modifier.weight(1f)) {
                    IconButton(
                        icon = Icons.Default.KeyboardArrowDown,
                        onClick = { if(currentQuantity.value > 1) currentQuantity.value-- else Unit },
                        contentDescription = "Increase Quantity",
                        iconTint = Color.DarkGray
                    )
                }
                Spacer(Modifier.height(4.dp))

            }

        }
    }


