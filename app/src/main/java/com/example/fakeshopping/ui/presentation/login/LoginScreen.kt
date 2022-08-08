package com.example.fakeshopping.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.utils.LoginScreenRoutes

@Composable
fun LoginScreenNavigation(){

    val loginFragsNavController = rememberNavController()
    NavHost(startDestination = LoginScreenRoutes.loginFragment, navController = loginFragsNavController ){

        composable( route = LoginScreenRoutes.loginFragment ){

        }

        composable( route = LoginScreenRoutes.signupFragment ){

        }

        composable( route = LoginScreenRoutes.numberVerificationFragment ){

        }

    }

}