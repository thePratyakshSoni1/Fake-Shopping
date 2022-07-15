package com.example.fakeshopping.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

object ToolbarProperties {

    val CollapsedToolbarColor:Brush = Brush.linearGradient(listOf(Color.Blue,Color(0xFF9500FF)))
    val ExpandedToolbarColor:Brush = Brush.linearGradient(listOf(Color.White,Color.White))

    val STATE_COLLAPSING_CONTENT_HEIGHT = 93f
    val STATE_STATIC_CONSTENT_HEIGHT = 206f

    val TOOLBAR_EXPANDED_HEIGHT =  STATE_STATIC_CONSTENT_HEIGHT + STATE_COLLAPSING_CONTENT_HEIGHT
    val TOOLBAR_COLLAPSED_HEIGHT = STATE_STATIC_CONSTENT_HEIGHT - STATE_COLLAPSING_CONTENT_HEIGHT

    @Composable
    fun Float.inDp(): Dp {
        return with(LocalDensity.current){
            this@inDp.toDp()
        }
    }


}

