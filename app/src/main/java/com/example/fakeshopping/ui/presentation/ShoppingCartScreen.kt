package com.example.fakeshopping.ui.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.model.ShoppingCartScreenViewModel
import com.example.fakeshopping.ui.presentation.components.HorizontalProductCard
import com.example.fakeshopping.ui.presentation.components.IconButton
import com.example.fakeshopping.ui.presentation.components.SelectableHorizontalProductCard
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.ui.theme.ColorYellowVarient
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties

@Composable
fun ShopingCartScreen(navController:NavController, currentUserId:String){

    val viewModel:ShoppingCartScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        viewModel.initalizeViewModel(currentUserId)
    })

    Scaffold(
        topBar = {
            ShoppingCartTopBar(onBackArrowPress = { navController.popBackStack() } )
        },
        modifier=Modifier.statusBarsPadding()
    ) {
        Column{

            LazyColumn(contentPadding = PaddingValues(top = 18.dp, bottom = 18.dp), modifier=Modifier.weight(1f)) {

                items( viewModel.cartItems.keys.toList(), key = { it } ) { productId ->

                    SelectableHorizontalProductCard(
                        product = ( viewModel.getProductById(productId) to viewModel.cartItems[productId]!!),
                        alwaysVisibleQuantityMeter = true,
                        isSelectedItemListEmpty = { true },
                        addNewSelectedItem = { Unit },
                        removeSelectedItem = { Unit },
                        checkSelectedItemAvailability = { false },
                        onNavigate = {
                            navController.navigate("${Routes.productDetailScreen}/${productId}")
                        },
                        onFavouriteButtonClick = {
                            viewModel.toggleFavourite(productId)
                        },
                        toggleSelectionModeTo = { Unit },
                        isFavourite = viewModel.userFavs.contains(productId),
                        onQuantityChange = { isIncreasing ->
                            viewModel.changeQuantity(isIncreasing, productId)
                        },
                        hasDeleteFunctionality = true
                    )

                }

            }

            ShoppingCartBottomBar(
                onCheckOut = { navController.navigate( Routes.checkOutOverviewScreen ) },
                totalItems = viewModel.cartItems.size,
                totalCost = viewModel.totalCost.value,
                isDisabled = viewModel.cartItems.isEmpty()
            )

        }
    }
}

@Composable
private fun ShoppingCartTopBar(onBackArrowPress:()->Unit,){

    Box(Modifier.shadow(elevation = 4.dp)){
        TopAppBar(
            backgroundColor=Color.White,
            title = {
                Text(
                    "Cart",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    modifier=Modifier.padding(start=12.dp)
                )
            },
            navigationIcon = {

                IconButton(
                    icon =Icons.Default.ArrowBack ,
                    onClick = {
                        onBackArrowPress()
                    },
                    contentDescription = "Go back"
                )

            },
        )
    }


}

@Composable
private fun ShoppingCartBottomBar( onCheckOut:()->Unit, totalItems:Int, totalCost:Float, isDisabled:Boolean ) {

    Box(Modifier.shadow(elevation = 8.dp)){
        Column(
            modifier = Modifier
                .background(Color.White)
                .wrapContentHeight()
                .background(Color.White)
                .padding(top = 18.dp),
        ) {
            Column(modifier=Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp) ){

                Spacer(Modifier.height(12.dp))
                Text(text = "$totalItems Items", fontSize = 12.sp, color=Color.LightGray, modifier = Modifier.padding(start= 12.dp))
                Spacer(Modifier.height(8.dp))
                Text( text= "$ $totalCost", fontSize = 16.sp, color= Color.Black, fontWeight = FontWeight.Bold , modifier = Modifier.padding(start= 12.dp))
                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth()){

                    Button(
                        onClick = { onCheckOut() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorYellow,
                            disabledBackgroundColor = ColorYellowVarient
                        ),
                        enabled = !isDisabled,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Checkout",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium
                        )
                    }


                }
            }

        }
    }


}