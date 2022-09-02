package com.example.fakeshopping.ui.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
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
import com.example.fakeshopping.ui.model.loginscreenViewmodels.SignupFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.OtpTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.LoginScreenRoutes
import com.example.fakeshopping.utils.LoginSignupStatus
import com.example.fakeshopping.utils.ToolbarProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignupFragment(loginFragmentNavController: NavHostController , onSuccessVerification:(userId:String)->Unit) {

    val viewModel = hiltViewModel<SignupFragmentViewModel>()
    val context = LocalContext.current
    val txtFieldRequester = remember{ FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(viewModel.isOtpStep.value) {

        viewModel.clearCode()
        viewModel.toggleSignupStep(false)

    }

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

        ScreenTitle(isVerificationStep = viewModel.isOtpStep)

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White)
        ) {

            Box(
                modifier = Modifier.weight(1f), contentAlignment= Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        .verticalScroll( rememberScrollState(), true)
                        .fillMaxSize()
                        .padding(end = 24.dp, start = 24.dp, top = 33.dp)
                ) {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = if (viewModel.isOtpStep.value) "Otp is send to ${viewModel.phone.value}" else "Sign Up",
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black,
                        fontSize = if (viewModel.isOtpStep.value) 18.sp else 23.sp,
                        fontWeight = if (viewModel.isOtpStep.value) FontWeight.Medium else FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (viewModel.isOtpStep.value) {

                        Spacer(Modifier.height(12.dp))
                        OtpTextField(viewModel.code) {
                            focusManager.clearFocus()
                            txtFieldRequester.requestFocus()
                        }

                    } else {
                        Spacer(Modifier.height(18.dp))
                        ScreenTextFields(viewModel = viewModel)

                    }

                    Spacer(Modifier.height(12.dp))
                    BottomText(
                        onTextClick = {
                            loginFragmentNavController.popBackStack()
                        },
                        isVerificationStep = viewModel.isOtpStep,
                        onResendCode = {
                            OtpCode.generateNewCode()
                            OtpCodeNotificationService(context).sendOtpCode(OtpCode.code.toString())
                        }
                    )

                }

            }

            BottomButtons(
                viewModel = viewModel,
                onLoginBtnClick = { loginFragmentNavController.popBackStack() },
                onSuccessVerification = {
                    viewModel.addUser()
                    onSuccessVerification(it)
                }
            )

        }
    }

    if(viewModel.isOtpStep.value){
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
}

@Composable
fun ScreenTitle(isVerificationStep:State<Boolean>) {

    Box(
        Modifier
            .fillMaxHeight(if (isVerificationStep.value) 0.3f else 0.17f)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        if (isVerificationStep.value) {
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
}

@Composable
fun BottomButtons(viewModel:SignupFragmentViewModel, onLoginBtnClick:()->Unit, onSuccessVerification: (String) -> Unit){

    val context = LocalContext.current
    if(viewModel.isOtpStep.value){

        OtpStepBottomActionButtons(
            onVerifyClick = {
                if(viewModel.code.value == OtpCode.code){
                    onSuccessVerification(viewModel.phone.value)
                }else{
                    Log.d("NUMBER VERIFICATION","Code didn't matched")
                }
            },
            onLoginClick = {
                onLoginBtnClick()
            }
        )
    }else{
        SignUpDetailStepBottomActionButtons(
            onSignUpBtnClick = {
                when(viewModel.verifySignUpDetails()){

                    LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME -> { Toast.makeText(context, "Please enter a valid name without numbers or symbols", Toast.LENGTH_SHORT).show() }
                    LoginSignupStatus.STATUS_SIGNUP_PASSWORD_UNMATCHED -> { Toast.makeText(context, "Password didn't matched",Toast.LENGTH_SHORT).show() }
                    LoginSignupStatus.STATUS_SIGNUP_INVALID_PASSWORD_PATTERN -> { Toast.makeText(context, "Password must be 8 digit containing a character, symbol and number",Toast.LENGTH_SHORT).show() }
                    LoginSignupStatus.STATUS_SIGNUP_INVALID_NUMBER -> { Toast.makeText(context, "Enter a valid 10 digi number",Toast.LENGTH_SHORT).show() }
                    LoginSignupStatus.STATUS_SIGNUP_FAILED -> { Toast.makeText(context, "Something went wrong !",Toast.LENGTH_SHORT).show() }
                    LoginSignupStatus.STATUS_SIGNUP_SUCCESS -> {
                        viewModel.toggleSignupStep(true)
                        OtpCode.generateNewCode()
                        OtpCodeNotificationService(context).sendOtpCode(OtpCode.code.toString())
                    }

                    else -> { Toast.makeText(context, "Something went wrong !",Toast.LENGTH_SHORT).show() }
                }
            },
            onLogInBtnClick = { onLoginBtnClick() }
        )
    }


}


@Composable
private fun OtpStepBottomActionButtons(onVerifyClick: () -> Unit, onLoginClick:()->Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 18.dp, vertical = 8.dp),
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
                contentDescription = "go back ?"
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Back",
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
private fun SignUpDetailStepBottomActionButtons( onSignUpBtnClick:()->Unit, onLogInBtnClick:()->Unit ){

    Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceAround){

        Button(
            onClick = {
                onLogInBtnClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = Color(0xFF9300FC),
                disabledBackgroundColor = Color(0x009300FC)
            ),
            enabled = true,
            modifier= Modifier
                .weight(1f)
                .border(2.5.dp, Color(0xFF9500FF), shape = RoundedCornerShape(100.dp)),
            shape = RoundedCornerShape(100.dp),
            elevation= ButtonDefaults.elevation(
                defaultElevation= 0.dp,
                pressedElevation= 0.dp
            )
        ) {


            Icon(
                modifier=Modifier.padding(start=8.dp),
                imageVector= Icons.Default.KeyboardArrowLeft,
                contentDescription = "Log in ?"
            )
            Spacer(Modifier.width(6.dp))
            Text("Log In", fontSize = 16.sp, modifier=Modifier.padding(end = 6.dp, top=6.dp, bottom=6.dp))

        }

        Spacer(Modifier.width(6.dp))
        val context = LocalContext.current

        Button(
            onClick = {
                onSignUpBtnClick()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF9500FF),
                contentColor = Color.White,
                disabledBackgroundColor = Color(0x7A9300FC)
            ),
            enabled = true,
            shape= RoundedCornerShape(100.dp),
            modifier=Modifier.weight(1f),

            ) {

            Text("Sign Up", fontSize = 16.sp, modifier=Modifier.padding(start = 8.dp, top=8.dp, bottom=8.dp))
            Spacer(Modifier.width(6.dp))
            Icon(
                modifier=Modifier.padding(end=8.dp),
                imageVector= Icons.Default.KeyboardArrowRight,
                contentDescription = "Sign Up ?"
            )

        }


    }

}

@Composable
private fun ScreenTextFields(viewModel:SignupFragmentViewModel){

    AppTextField(
        value = viewModel.firstName,
        onValuechange = {
            viewModel.onFirstNameTxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "first Name",
        textType = KeyboardType.Text
    )
    Spacer(Modifier.height(12.dp))
    AppTextField(
        value = viewModel.lastName,
        onValuechange = {
            viewModel.onLastNameTxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "Last Name",
        textType = KeyboardType.Text
    )
    Spacer(Modifier.height(12.dp))

    AppTextField(
        value = viewModel.phone,
        onValuechange = {
            viewModel.onPhoneNumberTxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "Phone Number",
        textType = KeyboardType.Number
    )
    Spacer(Modifier.height(12.dp))

    AppTextField(
        value = viewModel.dob,
        onValuechange = {
            viewModel.onDobTxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "Date Of Birth",
        textType = KeyboardType.Text
    )
    Spacer(Modifier.height(12.dp))

    AppTextField(
        value = viewModel.password,
        onValuechange = {
            viewModel.onPassWordTxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "Password",
        textType = KeyboardType.Password
    )
    Spacer(Modifier.height(12.dp))

    AppTextField(
        value = viewModel.confirmPassword,
        onValuechange = {
            viewModel.onConfirmPasswordtxtChange(it)
        },
        backgroundColor = ColorWhiteVariant,
        textColor = Color.Black,
        hintColor = Color.LightGray,
        hintTxt = "Confirm password",
        textType = KeyboardType.Password
    )

}

@Composable
private fun BottomText(onTextClick:()->Unit, isVerificationStep: State<Boolean>, onResendCode:()->Unit){

    val bottomText = buildAnnotatedString {

        if (isVerificationStep.value){
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
        }else{
            append("Already a member ? ")
            this.addStyle(
                SpanStyle(color = Color.LightGray, fontFamily = FontFamily.SansSerif),
                0,
                18
            )
            append("Log In")
            this.addStyle(
                SpanStyle(color = Color.Blue, fontFamily = FontFamily.SansSerif),
                19,
                25
            )
        }
    }

    Text(text=bottomText, textAlign=TextAlign.Center, modifier= Modifier
        .clickable(
            indication = null,
            interactionSource = MutableInteractionSource()
        ) {
            if (isVerificationStep.value) {
                onResendCode()
            } else {
                onTextClick()
            }
        }
        .fillMaxWidth())

}