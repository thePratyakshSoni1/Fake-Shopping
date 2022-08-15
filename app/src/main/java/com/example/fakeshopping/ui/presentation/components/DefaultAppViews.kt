package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.fakeshopping.ui.theme.ColorWhiteVariant

@Preview(showBackground = true)
@Composable fun Test(){

    Box(Modifier.fillMaxSize(), contentAlignment= Alignment.Center){


    }

}

@Composable
fun OtpTextField(codeText: State<String>, onOtpFieldClick:()->Unit) {

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onOtpFieldClick()
            }
    ){
        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() ) codeText.value[0].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 2) codeText.value[1].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 3) codeText.value[2].toString() else "")

        OtpTextFieldBox(
            text = if (codeText.value.isNotEmpty() && codeText.value.length >= 4) codeText.value[3].toString() else "")

    }

}

@Composable
private fun OtpTextFieldBox(text:String) {

    Box(
        modifier = Modifier
            .width(50.dp)
            .height(TextFieldDefaults.MinHeight)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1F1F1)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )

    }

}



@Composable
fun AppTextField(
    value: State<String>,
    onValuechange:(String)->Unit,
    backgroundColor:Color,
    textColor:Color,
    hintColor: Color,
    hintTxt:String,
    textType:KeyboardType
){

    TextField(
        modifier= Modifier.fillMaxWidth(),
        value = value.value,
        onValueChange = {
            onValuechange(it)
        },
        placeholder = { Text(hintTxt, color = hintColor) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = textColor
        ),
        shape = RoundedCornerShape(12.dp),
        maxLines = 1,
        keyboardOptions = KeyboardOptions( keyboardType = textType)
    )


}