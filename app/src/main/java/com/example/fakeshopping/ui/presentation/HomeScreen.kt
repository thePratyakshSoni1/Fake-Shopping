package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fakeshopping.R
import com.example.fakeshopping.ui.HomeScreenViewmodel


@Composable
fun HomeScreen(navController: NavController){

    val viewmodel:HomeScreenViewmodel = hiltViewModel()

    Column(modifier= Modifier.fillMaxSize()){

        HeaderSection(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f))

        BannerSection(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f) ,
            viewmodel.generateBannerSlidesResouuce()
        )

        AllProductsSection(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f))

    }


}

@Composable
fun HeaderSection (modifier:Modifier = Modifier.fillMaxWidth()){

}

@Composable
fun BannerSection (modifier:Modifier = Modifier.fillMaxWidth(), bannerResource:Map<String,Int>){

//    val testRes = mapOf<String,Int>(
//        "electronics" to R.drawable.electronics_category_display,
//        "men's clothes" to R.drawable.mensclothes_category_display,
//        "women's clothes " to R.drawable.womenclothes_category_display,
//        "jewellery" to R.drawable.jwellery_category_display,
//    )

    Box(modifier = modifier){
        BannerSlides(modifier = Modifier.fillMaxWidth(), bannersResource = bannerResource)
    }


}

@Composable
fun AllProductsSection (modifier:Modifier = Modifier.fillMaxWidth()){

}
