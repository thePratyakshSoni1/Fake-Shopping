package com.example.fakeshopping.ui.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.fakeshopping.ui.model.loginscreenViewmodels.LoginScreenViewmodel
import com.example.fakeshopping.ui.model.loginscreenViewmodels.SignupFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.LoginScreenRoutes
import com.example.fakeshopping.utils.LoginSignupStatus
import com.example.fakeshopping.utils.ToolbarProperties

@Composable
fun SignupFragment(loginFragmentNavController: NavHostController ){

    val viewModel = hiltViewModel<SignupFragmentViewModel>()

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
    ) {


        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White),
        ) {

            Column(modifier= Modifier
                .fillMaxSize()
                .padding(end = 24.dp, start = 24.dp, top = 33.dp) ){
                Text(modifier=Modifier.fillMaxWidth(),text="Sign Up", fontFamily = FontFamily.SansSerif, color= Color.Black, fontSize = 23.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(18.dp))

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
                Spacer(Modifier.height(12.dp))

                val bottomText = buildAnnotatedString {

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

                Text(text=bottomText, textAlign=TextAlign.Center, modifier= Modifier
                    .clickable {
                        loginFragmentNavController.popBackStack()
                    }
                    .fillMaxWidth())

            }

            Box(modifier= Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp), contentAlignment=Alignment.BottomCenter){
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){

                    Button(
                        onClick = {
                                  loginFragmentNavController.popBackStack()
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
                            when(viewModel.verifySignUpDetails()){

                                LoginSignupStatus.STATUS_SIGNUP_INVALID_NAME -> { Toast.makeText(context, "Please enter a valid name without number or symbols", Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_SIGNUP_PASSWORD_UNMATCHED -> { Toast.makeText(context, "Password didn't matched",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_SIGNUP_INVALID_NUMBER -> { Toast.makeText(context, "Enter a valid 10 digi number",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_SIGNUP_FAILED -> { Toast.makeText(context, "Something went wrong !",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_SIGNUP_SUCCESS -> {
                                    loginFragmentNavController.navigate(LoginScreenRoutes.numberVerificationFragment)
                                }

                                else -> { Toast.makeText(context, "Something went wrong !",Toast.LENGTH_SHORT).show() }
                            }
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

        }

    }


}