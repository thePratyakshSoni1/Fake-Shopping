package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.ProductsDetailScreenViewModel
import com.example.fakeshopping.utils.Routes

@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {

    val viewModel: ProductsDetailScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.setProduct(productId)
    }

    if (viewModel.product.value == null) {
        Text("Loading", fontSize = 18.sp, color = Color.DarkGray)
    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBarContent(
                    modifier = Modifier
                )
            }
        ) {

            Column(
                modifier=Modifier.verticalScroll(state = rememberScrollState(), enabled = true)
            ) {

                Spacer(Modifier.height(12.dp))

                //Product Preview
                ProductDetailsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 180.dp),
                    product = viewModel.product.value!!,
                    currentImageIndex = viewModel.currentProductPreviewSlide
                )

                Spacer(Modifier.height(24.dp))

                //Recommendations
                ProductRecommendationsSection(
                    relevantProductList = viewModel.relevantproduct,
                    otherProductsList = viewModel.otherPproducts,
                    onNaviagte = {
                        viewModel.setProduct(it)
                    },
                    onRelevantSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=${viewModel.product.value!!.category}")
                    },
                    onOtherSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=All")
                    }

                )

                Spacer(Modifier.height(32.dp))

            }


        }
    }

}

@Composable
fun ProductRecommendationsSection(
    relevantProductList: SnapshotStateList<ShopApiProductsResponse>,
    otherProductsList:SnapshotStateList<ShopApiProductsResponse>,
    onNaviagte:(ShopApiProductsResponse)->Unit,
    onRelevantSeeAllBtnClick: () -> Unit,
    onOtherSeeAllBtnClick: () -> Unit,
){

    Column(modifier= Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    ){

            RelevantProductRecommendations(
                productsList = relevantProductList,
                onNavigate = onNaviagte,
                onSellAllBtnClick = onRelevantSeeAllBtnClick,
            )


            Spacer(Modifier.height(39.dp))

            OtherProductRecommendations(
                productsList = otherProductsList,
                onNavigate = onNaviagte,
                onOtherSeelBtnClick = onOtherSeeAllBtnClick,
            )

            Spacer(Modifier.height(6.dp))


    }

}