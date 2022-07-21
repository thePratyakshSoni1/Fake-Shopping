package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fakeshopping.data.ProductRating
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.presentation.components.HorizontalProductCard
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.ui.theme.ColorYellowVarient
import com.example.fakeshopping.utils.Routes


@Composable
fun FavouritesScreen(navController: NavController){

    val isSelectionMode = remember{ mutableStateOf(false) }
    val selectedProductsList: SnapshotStateList<Int> = remember{ mutableStateListOf() }

    Scaffold(
        topBar = {
            FavouritesTopBar()
        },
        bottomBar = {
            if(isSelectionMode.value){
                FavouritesBottomBar()
            }
        }
    ) {

        LazyColumn(contentPadding = PaddingValues(top = 18.dp, bottom = 18.dp)){

            items(16){

                val tempProduct = ShopApiProductsResponse(
                    id = (it+1),
                    "Product Name ${it + 1} is here",
                    "$79",
                    "all",
                    "Product Desc ;)",
                    "#xyz",
                    ProductRating(
                        rate = 3.5f,
                        count = 150
                    )
                )

                Box(
                    Modifier
                        .padding(start = 12.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { _ ->
                                    if (!isSelectionMode.value) {
                                        isSelectionMode.value = true
                                        selectedProductsList.add(it + 1)
                                    } else {
                                        if (selectedProductsList.contains(it + 1)) {
                                            selectedProductsList.remove((it + 1))

                                        } else {
                                            selectedProductsList.add((it + 1))
                                        }
                                    }
                                },

                                onTap = { _ ->

                                    if(isSelectionMode.value){

                                        if (selectedProductsList.contains(it+1)) {
                                            selectedProductsList.remove(it+1)

                                        } else{
                                            selectedProductsList.add(it+1)
                                        }

                                    }else{
                                        navController.navigate(Routes.productDetailScreen + "/${it+1}")
                                    }

                                }

                            )
                        }
                ) {
                    HorizontalProductCard(
                        product = tempProduct,
                        isSelected = selectedProductsList.contains(tempProduct.id),
                        onClick = {
                            if(isSelectionMode.value){

                                if (selectedProductsList.contains(it+1)) {
                                    selectedProductsList.remove(it+1)

                                } else{
                                    selectedProductsList.add(it+1)
                                }

                            }else{
                                navController.navigate(Routes.productDetailScreen + "/${it+1}")
                            }
                        },
                        onRemoveBtnClick = {

                        }
                    )
                }

            }

        }

    }


}

@Composable
private fun FavouritesTopBar(){

    Box(Modifier.shadow(elevation = 2.dp)) {
        SmallTopAppBar(
            title = {
                Text(
                    "Your Favourites",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp
                )
            },
            navigationIcon = {

                Icon(
                    modifier = Modifier
                        .clickable {

                        }
                        .clip(CircleShape),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go back"
                )
            },
        )
    }

}

@Composable
private fun FavouritesBottomBar(){

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ){

        Button(
            onClick = { /*TODO*/ },
            modifier= Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorYellow,
                disabledBackgroundColor = ColorYellowVarient
            )
        ) {
            Text("Move to Cart", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = { /*TODO*/ },
            modifier= Modifier.padding(horizontal = 12.dp, vertical = 12.dp).border(width= 2.dp,shape= RoundedCornerShape(8.dp),color= ColorYellow),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = ColorYellow,
                disabledContentColor = ColorYellowVarient,
            )
        ) {
            Text("Move to Cart", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium)
        }


    }

}