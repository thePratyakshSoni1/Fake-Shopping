package com.example.fakeshopping.ui.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.ui.model.SupportScreenViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.SimpleAppScreensTopBar
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow

@Composable
fun SupportScreen(navController:NavHostController){

    val viewModel = hiltViewModel<SupportScreenViewModel>()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleAppScreensTopBar(title = "Support") {
                navController.popBackStack()
            }
        }
    ) {

        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .scrollable(
                    rememberScrollState(),
                    enabled = true,
                    orientation = Orientation.Vertical
                )
        ){

            Spacer( Modifier.height(8.dp) )
            Text(
                text = "For any furthur query or for any kind of problem tell us about your problem in below message and send it," +
                        " and we'll understand your problem and will try to fix it as soon as possible .",
            )
            Spacer( Modifier.height(12.dp) )
            AppTextField(
                    value = viewModel.mailTitle,
                    onValuechange = { viewModel.onMailTitleChange(it) },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.LightGray,
                    hintTxt = "Feedback title",
                    textType = KeyboardType.Text
            )
            Spacer( Modifier.height(8.dp) )

            TextField(
                value = viewModel.mailContent.value,
                onValueChange = {
                    viewModel.onMailContentChange(it)
                },
                placeholder = { Text("Your description goes here") },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = ColorWhiteVariant,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    placeholderColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer( Modifier.height(12.dp) )

            SupportScreenActionButtons(
                onSendClick = {
                    context.startActivity(
                        Intent.createChooser(viewModel.generateFeedbackIntent(), "Choose a mail client" )
                    )
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
            Spacer( Modifier.height(12.dp) )

        }

    }

}

@Composable
private fun SupportScreenActionButtons(
    onSendClick:()->Unit,
    onCancelClick:()->Unit
){

    Column(
        modifier = Modifier.fillMaxWidth()
    ){

        Box(modifier= Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorYellow)
            .clickable {
                onSendClick()
            }
            .padding(12.dp),
            contentAlignment = Alignment.Center

        ){
            Text(text= "Send", color= Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier= Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .clickable {
                onCancelClick()
            }
            .padding(12.dp),
            contentAlignment = Alignment.Center

        ){
            Text(text= "Discard", color= Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

    }

}