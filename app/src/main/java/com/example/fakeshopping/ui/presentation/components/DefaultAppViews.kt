package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.collection.MutableVector
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.fakeshopping.R
import com.example.fakeshopping.ui.theme.ColorExtraDarkGray
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
            .fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                onOtpFieldClick()
            }
    ){
        OtpTextFieldBox(text = if (codeText.value.isNotEmpty() ) codeText.value[0].toString() else "")

        OtpTextFieldBox(text = if (codeText.value.isNotEmpty() && codeText.value.length >= 2) codeText.value[1].toString() else "")

        OtpTextFieldBox(text = if (codeText.value.isNotEmpty() && codeText.value.length >= 3) codeText.value[2].toString() else "")

        OtpTextFieldBox(text = if (codeText.value.isNotEmpty() && codeText.value.length >= 4) codeText.value[3].toString() else "")

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
fun PasswordTextField(
    value: State<String>,
    onValuechange:(String)->Unit,
    backgroundColor:Color,
    textColor:Color,
    hintColor: Color,
    hintTxt:String,
    textType:KeyboardType,
    isPasswordVisible:State<Boolean>,
    onTogglePassword:()->Unit
){

    TextField(
        modifier= Modifier.fillMaxWidth(),
        value = value.value,
        onValueChange = {
            onValuechange(it)
        },
        trailingIcon = {
            Icon(
                painter= if(isPasswordVisible.value) painterResource(R.drawable.ic_visibility) else painterResource(R.drawable.ic_visibility_off) ,
                contentDescription = "toggle password visibility",
                tint= ColorExtraDarkGray,
                modifier=Modifier.clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) {
                    onTogglePassword()
                }
            )
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
        keyboardOptions = KeyboardOptions( keyboardType = textType),
        visualTransformation = if( isPasswordVisible.value ) VisualTransformation.None else PasswordVisualTransformation()
    )

}

@Composable
fun AppTextField(
    value: State<String>,
    onValuechange:(String)->Unit,
    backgroundColor:Color,
    textColor:Color,
    hintColor: Color,
    hintTxt:String,
    textType:KeyboardType,
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
        keyboardOptions = KeyboardOptions( keyboardType = textType),
    )


}


@Composable
fun UserAddressTextFiled(
    country: State<String>,
    state: State<String>,
    city: State<String>,
    landmark: State<String>,
    pincode: State<String>,
    onCountryTextValueChange:(newValue:String)->Unit,
    onStateTextValueChange:(newValue:String)->Unit,
    onCityTextValueChange:(newValue:String)->Unit,
    onPincodeTextValueChange:(newValue:Int)->Unit,
    onLandmarkTextValueChange:(newValue:String)->Unit,
){

    Column(Modifier.fillMaxWidth()) {

        UserDetailItemUpdate(
            heading = "Country", country.value ,
            onValueChange = {
                onCountryTextValueChange(it)
            },
            keyBardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(8.dp))

        UserDetailItemUpdate(
            heading = "State", state.value,
            onValueChange = {
                onStateTextValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier= Modifier.fillMaxWidth()) {

            Box(modifier=Modifier.weight(1f)){

                UserDetailItemUpdate(
                    heading = "Pincode", pincode.value,
                    onValueChange = {
                        onPincodeTextValueChange(it.toInt())
                    },
                    keyBardType = KeyboardType.Phone
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier=Modifier.weight(1f)) {
                UserDetailItemUpdate(
                    heading = "City", city.value,
                    onValueChange = {
                        onCityTextValueChange(it)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        UserDetailItemUpdate(
            heading = "Landmark", landmark.value,
            onValueChange = {
                onLandmarkTextValueChange(it)
            },
            keyBardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(8.dp))

    }

}


@Composable
private fun UserDetailHeading(text:String){
    Text(
        text = text,
        color = Color.DarkGray,
        fontSize = 14.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun UserDetailValueUpdate(value:String, onValueChange:(String)->Unit, keyBardType:KeyboardType){
    TextField(
        value = value,
        onValueChange= {
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.5.dp, color = Color(0xFF350099), shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBardType
        ),
        maxLines = 1
    )
}

@Composable
private fun UserDetailItemUpdate( heading:String, value:String, onValueChange:(String)->Unit, keyBardType:KeyboardType = KeyboardType.Text ){
    Column(Modifier.fillMaxWidth()) {
        UserDetailHeading(text = heading)
        Spacer(modifier=Modifier.height(6.dp))
        UserDetailValueUpdate(value = value, onValueChange= onValueChange, keyBardType)
    }
}


