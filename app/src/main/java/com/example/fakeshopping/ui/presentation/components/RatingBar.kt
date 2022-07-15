package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow

@Composable
fun RatingBar(modifier:Modifier,starsCount:Int,ratingOutOfFive:Int, isSmallSize:Boolean){

    Box(modifier=modifier){
        Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {

            if(!isSmallSize) {
                repeat(starsCount) {
                    if (it < ratingOutOfFive) {
                        Image(
                            modifier=Modifier.fillMaxSize().weight(1f),
                            imageVector = Icons.Default.Star,
                            colorFilter = ColorFilter.tint(ColorYellow),
                            contentDescription = "Rating is $ratingOutOfFive stars",
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Image(
                            modifier=Modifier.fillMaxSize().weight(1f),
                            imageVector = Icons.Default.Star,
                            colorFilter = ColorFilter.tint(ColorWhiteVariant),
                            contentDescription = "Rating is $ratingOutOfFive stars",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }else{
                Text("$ratingOutOfFive/5", fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium)
                Image(
                    imageVector = Icons.Default.Star,
                    colorFilter = ColorFilter.tint(Color(0xFFF5EA00)),
                    contentDescription = "Rating is $ratingOutOfFive stars",
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun TestRatingBar(){

    Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        RatingBar(
            modifier = Modifier.wrapContentHeight(),
            starsCount = 5,
            ratingOutOfFive = 3,
            isSmallSize = true
        )
    }

}
