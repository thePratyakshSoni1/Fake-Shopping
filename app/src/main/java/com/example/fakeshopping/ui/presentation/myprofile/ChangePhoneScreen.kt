package com.example.fakeshopping.ui.presentation.myprofile

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.otp_code_notification.OtpCode
import com.example.fakeshopping.otp_code_notification.OtpCodeNotificationService
import com.example.fakeshopping.otp_code_notification.OtpNotificationReceiver
import com.example.fakeshopping.ui.model.myprofileViewmodels.ChangePhoneNumberViewmodel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.presentation.components.IconButton
import com.example.fakeshopping.ui.presentation.components.OtpTextField
import com.example.fakeshopping.ui.presentation.components.PasswordTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChangePhoneNumberFragment(currentUserId:String, myProfileNavController:NavHostController, onLoggedStateChanged:(String?)->Unit){

    val viewModel :ChangePhoneNumberViewmodel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val otpfieldRequester = remember{ FocusRequester() }

    BackHandler(enabled = viewModel.isVerificationStep.value) {
        viewModel.toggleVerificationState()
    }

    LaunchedEffect( true) {
        viewModel.setUserIdAndDetails(currentUserId)
    }

    Scaffold(
        topBar = {
            ChangePhoneScreenTopAppBar (onBackArrowPress = {
                OtpCode.clearCode()
                myProfileNavController.popBackStack()
            })
        }
    ) {

        Column(Modifier.fillMaxWidth().padding(top= 36.dp, start= 12.dp, end= 12.dp)) {

            if( viewModel.isVerificationStep.value ) {

                TextField(
                    value = viewModel.code.value,
                    onValueChange = {
                        if (it.length <= 4) {
                            viewModel.onCodeValueChange(it)
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
                        .focusRequester(otpfieldRequester)
                        .alpha(0f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )

                OtpTextField(codeText = viewModel.code, onOtpFieldClick = { otpfieldRequester.requestFocus() })
                Text(
                    "Resend OTP",
                    color=Color.Blue,
                    modifier=Modifier.clickable (
                        indication=null,
                        interactionSource= MutableInteractionSource()
                            ){
                        OtpCode.generateNewCode()
                        OtpCodeNotificationService(context).sendOtpCode(OtpCode.code.toString())
                    }
                )

            }else{

                AppTextField(
                    value = viewModel.newPhoneNumber,
                    onValuechange = { viewModel.onPhoneValueChange(it) },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.DarkGray,
                    hintTxt = "New phone",
                    textType = KeyboardType.Phone
                )
                Spacer(Modifier.height(8.dp))

                PasswordTextField(
                    value = viewModel.password,
                    onValuechange = { viewModel.onPasswordValueChange(it) },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.DarkGray,
                    hintTxt = "Password",
                    textType = KeyboardType.Password,
                    isPasswordVisible = viewModel.isPassswordVisible,
                    onTogglePassword = { viewModel.togglePasswordVisibility() }
                )
                Spacer(Modifier.height(12.dp))



            }

            Column(Modifier.fillMaxWidth()) {

                val numberUpdateCoroutine = rememberCoroutineScope()
                ChangePhoneActionButtons(
                    onCancel = { myProfileNavController.popBackStack() },
                    onUpdate = {
                        if(viewModel.onPasswordVerify()){
                            if(viewModel.newPhoneNumber.value.length == 10){
                                numberUpdateCoroutine.launch {
                                    if(viewModel.isValidNumber()){
                                        onLoggedStateChanged(viewModel.newPhoneNumber.value)
                                        viewModel.onUserPhoneUpdate()
                                        Toast.makeText(context, "Number Updated ✔", Toast.LENGTH_SHORT).show()
                                        myProfileNavController.popBackStack()
                                    }else{
                                        Toast.makeText(context, "Number Already registered", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                Toast.makeText(context, "PLease enter a 10 digit phone number", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Wrong Password !", Toast.LENGTH_SHORT).show()
                        }

                    },
                    onContinue = {
                        OtpCode.generateNewCode()
                        OtpCodeNotificationService(context).sendOtpCode(OtpCode.code.toString())
                        viewModel.toggleVerificationState()
                    },
                    isVerificationStep = viewModel.isVerificationStep
                )
            }

        }



    }

}

@Composable
private fun ChangePhoneActionButtons( onCancel:()->Unit, onUpdate:()->Unit, onContinue:()->Unit, isVerificationStep: State<Boolean>){

    Button(
        onClick = {
            if(isVerificationStep.value){
                onUpdate()
            }else{
                onContinue()
            }
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
            if(isVerificationStep.value) "Update" else "Continue",
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


@Composable
private fun ChangePhoneScreenTopAppBar( onBackArrowPress:()->Unit ) {

    Box(Modifier.shadow(elevation = 4.dp)) {
        TopAppBar(
            backgroundColor = Color.White,
            title = {
                Text(
                    "My Profile",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },

            navigationIcon = {

                IconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = { onBackArrowPress() },
                    contentDescription = "Go back"
                )

            }
        )
    }

}
