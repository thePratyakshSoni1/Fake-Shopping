package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.ProductsDetailScreenViewModel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.Routes

@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {

    val viewModel: ProductsDetailScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.setProduct(productId)
    }

    if (viewModel.product.value == null) {
        LoadingView(modifier = Modifier.fillMaxSize(), circleSize = 64.dp)
    } else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true)
            ) {

//                Spacer(Modifier.height(12.dp))

                //Product Preview
                ProductDetailsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 180.dp),
                    product = viewModel.product.value!!,
                    currentImageIndex = viewModel.currentProductPreviewSlide
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorYellow
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        "BUY NOW",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(6.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(3.dp, ColorYellow),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        "ADD TO CART",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                //Recommendations
                ProductRecommendationsSection(
                    relevantProductList = viewModel.relevantproduct,
                    otherProductsList = viewModel.otherPproducts,
                    onNaviagte = {
                        navController.navigate(Routes.productDetailScreen + "/${it.id}")
                    },
                    onRelevantSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=${viewModel.product.value!!.category}")
                    },
                    onOtherSeeAllBtnClick = {
                        navController.navigate("${Routes.homeScreen}?category=All")
                    }

                )

                Spacer(Modifier.height(120.dp))

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
                onSeeAllBtnClick = onRelevantSeeAllBtnClick,
            )


            Spacer(Modifier.height(39.dp))

            OtherProductRecommendations(
                productsList = otherProductsList,
                onNavigate = onNaviagte,
                onOtherSeeAllBtnClick = onOtherSeeAllBtnClick,
            )

            Spacer(Modifier.height(6.dp))


    }

}