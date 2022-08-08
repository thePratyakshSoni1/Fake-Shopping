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
import com.example.fakeshopping.ui.presentation.*
import com.example.fakeshopping.ui.presentation.homscreen.HomeScreen
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

            window.statusBarColor = Color(0xFFF8F8F8).toArgb()
            HomeScreen(rootNavController = navController, category = backStackEntry.arguments?.getString("category")!!,window=window)
        }

        composable(
            route = Routes.productDetailScreen + "/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            window.statusBarColor = Color.White.toArgb()
            ProductDetailScreen(navController, backStackEntry.arguments?.getInt("productId")!!)

        }

        composable(
            route = Routes.shoppingCartScreen,
        ){
            window.statusBarColor = Color.White.toArgb()
            ShopingCartScreen(navController = navController)
        }

        composable(
            route = Routes.favouritesScreen,
        ){
            window.statusBarColor = Color.White.toArgb()
            FavouritesScreen(navController = navController)
        }

    }

}