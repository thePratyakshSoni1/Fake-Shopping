package com.example.fakeshopping.ui.presentation.paymentscreen

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.ui.presentation.components.AppTextField
import com.example.fakeshopping.utils.PaymentScreenRoutes

@Composable
fun PaymentScreen(stratDestination:String){

    val context = LocalContext.current
    val myWebview = WebView(context)
    AndroidView(factory = { myWebview }, modifier= Modifier.size(0.dp) )

    val paymentScreenNavController = rememberNavController()
    NavHost(navController = paymentScreenNavController, startDestination = stratDestination ){

        composable(route= PaymentScreenRoutes.cardFragment){
            CardPaymentFragment()
        }

        composable(route= PaymentScreenRoutes.upiFragment){
            UpiPaymentFragment()
        }

        composable(route= PaymentScreenRoutes.netBankingFragment){
            NetbankingFragment()
        }

        composable(route= PaymentScreenRoutes.walletFragment){
            WalletPaymentFragment()
        }

    }



}

