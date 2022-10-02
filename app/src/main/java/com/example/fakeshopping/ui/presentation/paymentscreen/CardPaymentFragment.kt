package com.example.fakeshopping.ui.presentation.paymentscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.payment_viewmodels.CardPaymentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.PasswordTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.squareup.moshi.Json
import org.json.JSONObject


@Composable
fun CardPaymentFragment(
    sendRequest:(payload:JSONObject) -> Unit,
    amountToBePaid:Float,
    currentUserId:Long
) {

    val viewModel: CardPaymentViewModel = hiltViewModel()

    LaunchedEffect(key1 = true, block = {
        viewModel.initRazorpayPayloadAndAmout(amountToBePaid, currentUserId = currentUserId)
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 36.dp, start = 12.dp, end = 12.dp)
    ) {

        AppTextField(
            value = viewModel.cardNumber,
            onValuechange = { viewModel.onCardNumberTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "0000 0000 0000 0000",
            textType = KeyboardType.Phone
        )
        Spacer(Modifier.height(8.dp))

        AppTextField(
            value = viewModel.cardHolderName,
            onValuechange = { viewModel.onCardHolderNameTextChange(it) },
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.DarkGray,
            hintTxt = "Card holder name",
            textType = KeyboardType.Text
        )
        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth()) {

            Box(Modifier.weight(1f)) {
                AppTextField(
                    value = viewModel.cardExpiry,
                    onValuechange = { viewModel.onCardExpiryTextChange(it) },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.DarkGray,
                    hintTxt = "MM/YY",
                    textType = KeyboardType.Phone
                )
            }
            Spacer(Modifier.width(8.dp))

            Box(Modifier.weight(1f)) {
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
        Spacer(Modifier.height(22.dp))
        CardPaymentActionButtons(
            onContinue = {
                try {
                    viewModel.payload.value?.put("method", "card")
                    viewModel.payload.value?.put("card[name]", viewModel.cardHolderName.value)
                    viewModel.payload.value?.put("card[number]", viewModel.cardNumber.value)
                    viewModel.payload.value?.put("card[expiry_month]", viewModel.cardExpiry.value.split("/")[0])
                    viewModel.payload.value?.put("card[expiry_year]",  viewModel.cardExpiry.value.split("/")[1])
                    viewModel.payload.value?.put("card[cvv]",  viewModel.cardCvv.value)
                    sendRequest(viewModel.payload.value!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            onCancel = { }
        )

    }


}

@Composable
private fun CardPaymentActionButtons(onContinue:()->Unit, onCancel: () -> Unit){


    Button(
        onClick = {
            onContinue()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF9500FF)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(pressedElevation = 0.4.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
    ) {
        Text(
           "Continue",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 4.dp),
            color= Color.White
        )
    }

    Spacer(modifier=Modifier.height(8.dp))

    Button(
        onClick = { onCancel() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, Color.Transparent),
        elevation = ButtonDefaults.elevation(pressedElevation = 0.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
    ) {
        Text(
            "Cancel",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(vertical = 4.dp),
            color=Color.Red
        )
    }



}
