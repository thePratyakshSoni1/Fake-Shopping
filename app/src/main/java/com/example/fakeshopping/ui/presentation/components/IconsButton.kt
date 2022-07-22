package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.io.FileDescriptor

@Composable
fun IconButton(
    icon:ImageVector,
    iconTint: Color = Color.Black,
    onClick:() -> Unit,
    contentDescription: String?
) {
    Icon(
        modifier= Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(6.dp),
        imageVector = icon,
        contentDescription = contentDescription,
        tint = iconTint
    )
}