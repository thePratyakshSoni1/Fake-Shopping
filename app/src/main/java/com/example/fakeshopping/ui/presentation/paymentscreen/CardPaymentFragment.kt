package com.example.fakeshopping.ui.presentation.paymentscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.payment_viewmodels.CardPaymentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.PasswordTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant


@Composable
fun CardPaymentFragment(){

    val viewModel :CardPaymentViewModel = hiltViewModel()

    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 36.dp, start = 12.dp, end = 12.dp)){

        AppTextField(
            value = viewModel.cardNumber,
            onValuechange = { viewModel.onCardNumberTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "0000 0000 0000 0000",
            textType = KeyboardType.Phone
        )

        AppTextField(
            value = viewModel.cardHolderName,
            onValuechange = { viewModel.onCardHolderNameTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "Card holder name",
            textType = KeyboardType.Text
        )

        AppTextField(
            value = viewModel.cardExpiry,
            onValuechange = { viewModel.onCardExpiryTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "MM/YY",
            textType = KeyboardType.Phone
        )

        PasswordTextField(
            value = viewModel.cardCvv,
            onValuechange = { viewModel.onCardCvvTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "1234",
            textType = KeyboardType.NumberPassword,
            isPasswordVisible = viewModel.isCvvVisible,
            onTogglePassword = { viewModel.toggleCvvVisibility() }
        )

    }

}
