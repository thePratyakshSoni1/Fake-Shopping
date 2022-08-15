package com.example.fakeshopping.ui.presentation.login

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.utils.HomeScreenFragmentRoutes
import com.example.fakeshopping.utils.LoginScreenRoutes
import com.example.fakeshopping.utils.Routes

@Composable
fun LoginScreenNavigation(rootnavController:NavHostController){

    val loginFragsNavController = rememberNavController()
    NavHost(startDestination = LoginScreenRoutes.loginFragment, navController = loginFragsNavController ){

        composable( route = LoginScreenRoutes.loginFragment ){

            LoginFragment(
                loginFragsNavController,
                onLoginSuccess = {
                    rootnavController.popBackStack()
                    rootnavController.navigate(Routes.homeScreen+"?category={category}")
                }
            )

        }

        composable( route = LoginScreenRoutes.signupFragment ){

            SignupFragment(
                loginFragsNavController,
            )

        }

        composable( route = LoginScreenRoutes.numberVerificationFragment ){

            NumberVericationFragment(
                loginFragsNavController,
                onSuccessVerification = {
                    loginFragsNavController.popBackStack()
                    loginFragsNavController.popBackStack()
                    loginFragsNavController.popBackStack()
                    rootnavController.navigate(Routes.homeScreen+"?category={category}")
                }
            )



        }

    }

}