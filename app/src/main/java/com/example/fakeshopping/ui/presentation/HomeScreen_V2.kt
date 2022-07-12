package com.example.fakeshopping.ui.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarSizes
import com.example.fakeshopping.utils.ToolbarSizes.inDp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen_V2(navController: NavController){

    val viewmodel:HomeScreenViewmodel = hiltViewModel()
    val toolBaroffsetY:MutableState<Float> = remember{ mutableStateOf(0f) }

    val homefeedScrollOffset = rememberLazyGridState()


    Box(
        modifier= Modifier.fillMaxSize()
    ){

        Column(
            modifier= Modifier.nestedScroll(

                object :NestedScrollConnection {

                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                        Log.d("TOPBAR","${available.y}")
                        val delta = available.y
                        val newScrollOffset = toolBaroffsetY.value + delta

                        if(homefeedScrollOffset.firstVisibleItemIndex == 0){
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT , 0f)
                            Log.d("TOPBAR","TOOLBAR OFFSET: ${toolBaroffsetY.value}")
                        }else{
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT , -ToolbarSizes.STATE_COLLAPSING_CONTENT_HEIGHT)
                            Log.d("TOPBAR","TOOLBAR OFFSET: ${toolBaroffsetY.value}")
                        }

                        return Offset.Zero
                    }

                }
            )
        ){
            ContentSection(
                navController,
                viewmodel,
                homefeedScrollOffset
            )
        }

        HeaderSection(

            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(x = 0, y = toolBaroffsetY.value.toInt())
                },
            viewmodel.searchBoxText, //73 dp
            categories = viewmodel.categories,
            selectedCategory = viewmodel.selectedProductCategory,
            onCategoryChange = {
                viewmodel.onCategoryChange(it)
            },
            offsetReq = toolBaroffsetY

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentSection(
    navController:NavController,
    viewmodel:HomeScreenViewmodel, listState: LazyGridState
){

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = listState,
//        contentPadding = PaddingValues(top=ToolbarSizes.TOOLBAR_EXPANDED_HEIGHT.inDp())
    ){

        item(span = { GridItemSpan(3) }){
            Spacer(Modifier.height(ToolbarSizes.TOOLBAR_EXPANDED_HEIGHT.inDp() - ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item(span = { GridItemSpan(3) }){
            Spacer(Modifier.height(ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item( span= { GridItemSpan(3) } ){

            BannerSection(
                Modifier.padding(vertical=22.dp),
                viewmodel.bannerResources,
                viewmodel.userInteractedWithBanners

            )
        }

        allProductsSection(
            viewmodel.products
        ) { product ->
            navController.navigate("${Routes.productDetailScreen}/${product.id}")
        }

    }

}

