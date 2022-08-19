package com.example.fakeshopping.ui.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.ui.model.loginscreenViewmodels.LoginScreenViewmodel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.LoginScreenRoutes
import com.example.fakeshopping.utils.LoginSignupStatus

@Composable
fun LoginFragment(loginFragmentNavController: NavHostController, onLoginSuccess:(userId:String)->Unit){

    val viewModel:LoginScreenViewmodel = hiltViewModel()
    val context = LocalContext.current

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
            Modifier
                .fillMaxHeight(0.35f)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {

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


            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                Text(title, fontSize = 43.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Your satisfaction is our duty.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 33.dp, topEnd = 33.dp))
                .background(Color.White),
        ) {
            
            Column(modifier= Modifier
                .fillMaxSize()
                .padding(end = 24.dp, start = 24.dp, top = 64.dp) ){
                Text(modifier=Modifier.fillMaxWidth(),text="Log In", fontFamily = FontFamily.SansSerif, color= Color.Black, fontSize = 23.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(18.dp))
                
                AppTextField(
                    value = viewModel.phoneNumberTxt ,
                    onValuechange = {
                        viewModel.changePhoneText(it)
                    },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.LightGray,
                    hintTxt = "Phone Number",
                    textType = KeyboardType.Number
                )
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    value = viewModel.passwordTxt ,
                    onValuechange = {
                        viewModel.changePasswordText(it)
                    },
                    backgroundColor = ColorWhiteVariant,
                    textColor = Color.Black,
                    hintColor = Color.LightGray,
                    hintTxt = "Password",
                    textType = KeyboardType.Password
                )
                Spacer(Modifier.height(12.dp))

                val bottomText = buildAnnotatedString {

                    append("New here ? ")
                    this.addStyle(
                        SpanStyle(color = Color.LightGray, fontFamily = FontFamily.SansSerif),
                        0,
                        10
                    )
                    append("Sign Up")
                    this.addStyle(
                        SpanStyle(color = Color.Blue, fontFamily = FontFamily.SansSerif),
                        11,
                        18
                    )

                }

                Text(text=bottomText, textAlign=TextAlign.Center, modifier= Modifier
                    .clickable {
                        loginFragmentNavController.navigate(LoginScreenRoutes.signupFragment)
                    }
                    .fillMaxWidth())
                
            }

            Box(modifier= Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp), contentAlignment=Alignment.BottomCenter){
                Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){

                    Button(
                        onClick = {
                                  loginFragmentNavController.navigate(LoginScreenRoutes.signupFragment)
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

                        Text("SignUp", fontSize = 16.sp, modifier=Modifier.padding(start = 6.dp, top=6.dp, bottom=6.dp))

                    }

                    Spacer(Modifier.width(6.dp))

                    Button(

                        onClick = {

                            when(viewModel.verifyLogin()){
                                LoginSignupStatus.STATUS_INVALID_DETAILS -> { Toast.makeText(context, "Please enter a valid phone number",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_LOGIN_WRONG_PASSWORD -> { Toast.makeText(context, "Wrong Password",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_LOGIN_NO_USER -> { Toast.makeText(context, "No user with this number , Please Sign Up Now !",Toast.LENGTH_SHORT).show() }
                                LoginSignupStatus.STATUS_LOGIN_SUCCESS -> {
                                    Toast.makeText(context, "Logged In as ${viewModel.getUser()}",Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(viewModel.phoneNumberTxt.value)
                                }

                                else -> { Toast.makeText(context, "Sorry, Something went wrong !",Toast.LENGTH_SHORT).show() }
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

                        Text("LogIn", fontSize = 16.sp, modifier=Modifier.padding(start = 8.dp, top=8.dp, bottom=8.dp))
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            modifier=Modifier.padding(end=8.dp),
                            imageVector= Icons.Default.KeyboardArrowRight,
                            contentDescription = "Login ?"
                        )

                    }


                }
            }

        }

    }


}