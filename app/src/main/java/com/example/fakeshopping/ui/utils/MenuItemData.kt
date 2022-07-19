package com.example.fakeshopping.ui.utils

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

val menuItems:List<MenuItemData> = listOf<MenuItemData>(

    MenuItemData(
        "Favourites",
        Icons.Default.FavoriteBorder
    ) { },

    MenuItemData(
        "Orders",
        Icons.Default.Send
    ) { },

    MenuItemData(
        "Your Cart",
        Icons.Default.ShoppingCart
    ) { },

    MenuItemData(
        "Settings",
        Icons.Default.Settings
    ) { },


)
