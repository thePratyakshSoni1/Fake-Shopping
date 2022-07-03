package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.ui.ProductsDetailScreenViewModel
import com.example.fakeshopping.utils.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun ProductDetailScreen(navController: NavController, productId:Int) {

    val viewModel:ProductsDetailScreenViewModel = hiltViewModel()

    LaunchedEffect(key1 = true ){
            viewModel.setProduct(productId)
    }

    if(viewModel.product.value == null){
        Text("Loading", fontSize = 18.sp,color=Color.DarkGray)
    }else {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBarContent(
                    modifier = Modifier
                )
            }
        ) {


            //Product Preview
            ProductDetailsSection(
                modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 180.dp),
                product = viewModel.product.value!!,
                currentImageIndex = viewModel.currentProductPreviewSlide
            )

            //Recommendations: Implementing Soon ...


        }
    }

}