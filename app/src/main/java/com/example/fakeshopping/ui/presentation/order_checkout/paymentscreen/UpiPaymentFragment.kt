package com.example.fakeshopping.ui.presentation.order_checkout.paymentscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.ui.model.payment_viewmodels.UpiPaymentFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.razorpay.Razorpay
import org.json.JSONObject

private object UpiPayRoutes{

    const val upiMethodsScreen = "fakeshopping_UPIMETHOD_MethodSelectionScreen"
    const val upiByIdScreen = "fakeshopping_UPIMETHOD_UpiByIdScreen"

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpiPaymentFragment(
    razorpay: Razorpay,
    currentUser:Long,
    sendPayRequest:( payload:JSONObject)->Unit,
    amountToPay:Float
){

    val viewModel = hiltViewModel<UpiPaymentFragmentViewModel>()
    val ctx = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true, block = {
        viewModel.initViewModelWithuserandRazorpay(
            razorpay, currentUser, amountToPay
        )
    })

    val upiPayScreenNavController = rememberNavController()
    NavHost(
        navController = upiPayScreenNavController,
        startDestination = UpiPayRoutes.upiMethodsScreen
    ){

        composable(
            route = UpiPayRoutes.upiMethodsScreen
        ){
            UpiPaymentOptionsFragment(
                onByAppsClick = {
                    viewModel.onIntentFlowClick()
                    sendPayRequest(viewModel.payload)
                },
                onByUpiIdClick = {
                    upiPayScreenNavController.navigate(UpiPayRoutes.upiByIdScreen)
                }
            )
        }

        composable(
            route = UpiPayRoutes.upiByIdScreen
        ){
            UpiIdPaymentFragment(
                upiValue = viewModel.upiId, onValueChange = { viewModel.onUpiIdTextChange(it) },
                onContinueClick = {
                    val upiValidationErrors = viewModel.validateUpiFormat()
                    if(upiValidationErrors == null){
                        viewModel.onIntentCollectClick()
                        keyboardController?.hide()
                        sendPayRequest(viewModel.payload)
                    }else{
                        Toast.makeText(ctx,upiValidationErrors, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

    }




}

@Composable
private fun UpiIdPaymentFragment(
    upiValue: State<String>,
    onValueChange:( value:String )->Unit,
    onContinueClick:()->Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {

        Spacer(modifier = Modifier.height(21.dp))
        AppTextField(
            value = upiValue,
            onValuechange = onValueChange,
            backgroundColor = ColorWhiteVariant,
            textColor = Color.Black,
            hintColor = Color.LightGray,
            hintTxt = "UPI ID",
            textType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier= Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorYellow)
            .clickable {
                onContinueClick()
            }
            .padding(12.dp),
            contentAlignment = Alignment.Center

        ){
            Text(text= "Continue To Pay", color= Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }


    }

}

@Composable
private fun UpiPaymentOptionsFragment( onByAppsClick:()->Unit, onByUpiIdClick:()->Unit ){


    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(14.dp)
    ){

        Spacer(modifier = Modifier.height(31.dp))
        Text(
            "Choose a UPI method",
            fontSize = 21.sp,
            fontWeight= FontWeight.Bold,
            modifier=Modifier.padding(start= 8.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))
        Box(modifier= Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorYellow)
            .clickable {
                onByAppsClick()
            }
            .padding(12.dp),
            contentAlignment = Alignment.Center

        ){
            Text(text= "Installed UPI Apps", color= Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier= Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorYellow)
            .clickable {
                onByUpiIdClick()
            }
            .padding(12.dp),
            contentAlignment = Alignment.Center

        ){
            Text(text= "UPI Id", color= Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

    }

}