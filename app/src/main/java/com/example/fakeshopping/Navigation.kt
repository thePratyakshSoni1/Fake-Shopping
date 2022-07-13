package com.example.fakeshopping

import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakeshopping.ui.presentation.HomeScreen
import com.example.fakeshopping.ui.presentation.ProductDetailScreen
import com.example.fakeshopping.utils.Routes

@Composable
fun Navigation(window: Window) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.homeScreen+"?category={category}") {

        composable(
            route = Routes.homeScreen+"?category={category}",
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = "All"
                }
            )
        ) { backStackEntry ->
            window.statusBarColor = Color.Blue.toArgb()
            HomeScreen(navController, backStackEntry.arguments?.getString("category")!!)
        }

        composable(
            route = Routes.productDetailScreen + "/{productId}",
            arguments = kotlin.collections.listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            window.statusBarColor = Color.White.toArgb()
            ProductDetailScreen(navController, backStackEntry.arguments?.getInt("productId")!!)

        }

    }

}