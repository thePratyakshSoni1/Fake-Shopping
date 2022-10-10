package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.model.FavouriteScreenViewmodel
import com.example.fakeshopping.ui.presentation.components.HorizontalProductCard
import com.example.fakeshopping.ui.presentation.components.IconButton
import com.example.fakeshopping.ui.presentation.components.SelectableHorizontalProductCard
import com.example.fakeshopping.ui.theme.ColorExtraDarkGray
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.ui.theme.ColorYellowVarient
import com.example.fakeshopping.utils.Routes


@Composable
fun FavouritesScreen(navController: NavController, currentUser:String){

    val viewModel:FavouriteScreenViewmodel = hiltViewModel()

    BackHandler(enabled = viewModel.selectedProducts.isNotEmpty()) {
        viewModel.clearSelection()
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.setCurrentUserAndFavs(currentUser)
    })

    Scaffold(
        topBar = {

            FavouritesTopBar(
                isSelectionMode = viewModel.isSelectionMode,
                onCancelSelection = {
                    viewModel.clearSelection()
                },
                onBackArrowPress = {
                    navController.popBackStack()
                },
                removeSelected = {
                    viewModel.removeAllSelectedFromFavourites()
                }
            )

        },
        bottomBar = {
            if(viewModel.isSelectionMode.value){
                FavouritesBottomBar(
                    onMovieToCart = {
                        viewModel.moveToCart()
                     },
                    onCheckout = {
                        val itemsToBuy = viewModel.selectedProducts.keys.toList().toString()
                        val itemsToBuyQuantity = viewModel.selectedProducts.values.toList().toString()
                        navController.navigate("${ Routes.checkOutOverviewScreen }/$itemsToBuy/$itemsToBuyQuantity")
                    }
                )
            }
        },
        modifier=Modifier.statusBarsPadding()
    ) {

        LazyColumn(contentPadding = PaddingValues(top = 18.dp, bottom = 18.dp)){

            items( viewModel.favouriteProducts.values.toList(), key = { it.id } ){ product ->

                val context = LocalContext.current
                SelectableHorizontalProductCard(
                    product = ( product to (viewModel.selectedProducts[product.id] ?: 1) ),
                    isSelectionMode = viewModel.isSelectionMode,
                    alwaysVisibleQuantityMeter = false,
                    isSelectedItemListEmpty = {
                        viewModel.selectedProducts.isEmpty()
                    },
                    addNewSelectedItem = {
                        if(!viewModel.addNewSelectedItem(product.id)){
                            Toast.makeText(context,"Item Already Added",Toast.LENGTH_SHORT).show()
                        }
                    },
                    removeSelectedItem = { id->
                        viewModel.onItemRemove(id)
                    },
                    checkSelectedItemAvailability = {
                        viewModel.selectedProducts.contains(product.id)
                    },
                    onNavigate = {
                        navController.navigate("${Routes.productDetailScreen}/${product.id}")
                    },

                    onFavouriteButtonClick = {
                        viewModel.toggleFavourite(product.id)
                                             },
                    toggleSelectionModeTo = viewModel.changeSelectionModeTo,
                    isFavourite = viewModel.favouriteProducts.containsKey(product.id),
                    onQuantityChange = {
                        viewModel.changeQuantity(it,product.id)
                    },
                    hasDeleteFunctionality = false
                )

            }

        }

    }


}


@Composable
private fun FavouritesTopBar(isSelectionMode: State<Boolean>, onCancelSelection:()->Unit, onBackArrowPress:()->Unit, removeSelected:()->Unit) {

    Box(Modifier.shadow(elevation = 4.dp)) {
        TopAppBar(
            backgroundColor = Color.White,
            title = {
                Text(
                    "Your Favourites",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },

            navigationIcon = {

                IconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = { onBackArrowPress() },
                    contentDescription = "Go back"
                )

            },
            actions = {

                if (isSelectionMode.value) {

                    IconButton(
                        icon = Icons.Default.Delete,
                        onClick = removeSelected,
                        contentDescription = "Remove Selected Items",
                        iconTint= Color.Red
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "Cancel",
                        color = ColorExtraDarkGray,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                onCancelSelection()
                            },
                    )
                }

            }
        )
    }

}

@Composable
private fun FavouritesBottomBar(onMovieToCart:()->Unit, onCheckout:()->Unit){

    Box(
        Modifier
            .background(Color.White)
            .shadow(elevation = 4.dp))
    {

            Row(horizontalArrangement = Arrangement.Center, modifier= Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp, horizontal = 8.dp)){

                Button(
                    onClick = { onMovieToCart() },
                    modifier = Modifier.border(width = 2.5.dp, shape = RoundedCornerShape(12.dp), color = ColorYellow),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        disabledContentColor = ColorYellowVarient,
                    ),
                    elevation = ButtonDefaults.elevation(pressedElevation = 0.4.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
                ) {
                    Text(
                        modifier= Modifier.padding(horizontal = 9.5.dp, vertical=1.5.dp),
                        text="Move to Cart",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color=Color.Black
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Add to cart",
                        tint=Color.Black,
                    )

                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorYellow,
                        disabledBackgroundColor = ColorYellowVarient
                    )
                ) {
                    Text(
                        "Checkout",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        color=Color.Black
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to cart",
                        tint=Color.Black,
                    )
                }

            }
        }
//    }


}