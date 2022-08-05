package com.example.fakeshopping.ui.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItemData(
    val text:String,
    val icon:ImageVector,
    val onClickListener: () -> Unit
)

