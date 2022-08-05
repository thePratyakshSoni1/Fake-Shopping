package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.fakeshopping.ui.theme.ColorExtraDarkGray

@Composable
fun IconButton(
    icon:ImageVector,
    iconTint: Color = ColorExtraDarkGray,
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