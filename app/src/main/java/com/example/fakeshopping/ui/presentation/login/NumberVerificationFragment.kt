package com.example.fakeshopping.ui.presentation.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.otp_code_notification.OtpCode
import com.example.fakeshopping.otp_code_notification.OtpCodeNotificationService
import com.example.fakeshopping.ui.model.loginscreenViewmodels.LoginScreenViewmodel
import com.example.fakeshopping.ui.model.loginscreenViewmodels.NumberVericationFragmentViewmodel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.OtpTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.ToolbarProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NumberVericationFragment(loginFragmentNavController: NavHostController,onSuccessVerification:()->Unit) {

    val viewModel: NumberVericationFragmentViewmodel = hiltViewModel()
    val txtFieldRequester = remember{ FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    Column(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color.Blue, Color(0xFF9500FF)),
                    startX = -600f,
                    endX = 600f
                )
            )
            .imePadding()
    ) {

        ScreenTitle()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White)
                .padding(end = 18.dp, start = 18.dp, top = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.padding(end = 18.dp, start = 18.dp, top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Otp is send to ${viewModel.phone}",
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(12.dp))
                    OtpTextField( viewModel.code ){
                        focusManager.clearFocus()
                        txtFieldRequester.requestFocus()
                    }

                    Spacer(Modifier.height(12.dp))
                    BottomText()

                }

            }

            BottomActionButtons(
                onVerifyClick = { onSuccessVerification() },
                onLoginClick = { loginFragmentNavController.popBackStack() }
            )
            Spacer(Modifier.height(8.dp))
        }

    }

    TextField(
        value = viewModel.code.value,
        onValueChange = {
            if (it.length <= 4) {
                viewModel.onCodechange(it)
                if(it.length == 4){
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            } else {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
            Log.d("CODE", "Now: ${viewModel.code.value}")
        },
        modifier = Modifier
            .size(0.dp)
            .focusRequester(txtFieldRequester)
            .alpha(0f),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        )
    )

}


@Composable
private fun ColumnScope.BottomActionButtons(onVerifyClick: () -> Unit, onLoginClick:()->Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .weight(1f),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {

        Button(
            onClick = {
                      onLoginClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xFF9300FC),
                disabledBackgroundColor = Color(0x009300FC)
            ),
            enabled = true,
            modifier = Modifier
                .weight(1f)
                .border(2.5.dp, Color(0xFF9500FF), shape = RoundedCornerShape(100.dp)),
            shape = RoundedCornerShape(100.dp),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Login ?"
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Login",
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 6.dp, top = 6.dp, bottom = 6.dp)
            )

        }

        Spacer(Modifier.width(6.dp))

        Button(
            onClick = {
                onVerifyClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF9500FF),
                contentColor = Color.White,
                disabledBackgroundColor = Color(0x7A9300FC)
            ),
            enabled = true,
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier.weight(1f),

            ) {

            Text(
                "Verify",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            )

        }


    }


}

@Composable
private fun BottomText(){

    val bottomText = buildAnnotatedString {

        append("Didn't get code ? ")
        this.addStyle(
            SpanStyle(color = Color.LightGray, fontFamily = FontFamily.SansSerif),
            0,
            17
        )
        append("Resend Otp")
        this.addStyle(
            SpanStyle(color = Color.Blue, fontFamily = FontFamily.SansSerif),
            18,
            28
        )

    }

    val context = LocalContext.current
    Text(
        text = bottomText, textAlign = TextAlign.Center, modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null
            ) {
                OtpCodeNotificationService(context).sendOtpCode(OtpCode.generateNewCode())
            }
            .fillMaxWidth()
    )

}

@Composable
private fun ScreenTitle(){

    Box(
        Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        val title = buildAnnotatedString {

            append("Fake")
            this.addStyle(
                SpanStyle(color = Color.White, fontFamily = FontFamily.SansSerif),
                0,
                4
            )
            append("Shop")
            this.addStyle(
                SpanStyle(color = ColorYellow, fontFamily = FontFamily.SansSerif),
                4,
                8
            )

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(
                "Your satisfaction is our duty.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

        }
    }
}

