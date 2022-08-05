package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(modifier: Modifier, circleSize: Dp){

    Box(modifier= modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = Color(0xFF2D0081),
            modifier = Modifier.size(circleSize).padding(6.dp)
        )
    }

}