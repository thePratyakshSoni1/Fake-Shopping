package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(){

    Column(modifier= Modifier.fillMaxSize()){

        HeaderSection(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f))
        BannerSection(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f))
        AllProductsSection(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f))

    }


}

@Composable
fun HeaderSection (modifier:Modifier = Modifier.fillMaxWidth()){

}

@Composable
fun BannerSection (modifier:Modifier = Modifier.fillMaxWidth()){

}

@Composable
fun AllProductsSection (modifier:Modifier = Modifier.fillMaxWidth()){

}
