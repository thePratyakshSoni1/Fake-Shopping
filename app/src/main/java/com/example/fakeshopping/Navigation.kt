package com.example.fakeshopping

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.ui.presentation.CategoriesSection
import com.example.fakeshopping.ui.presentation.HomeScreen
import com.example.fakeshopping.utils.Routes

@Composable
fun Navigation(){

    val navController = rememberNavController()

    NavHost(navController= navController, startDestination = Routes.homeScreen){

        composable(Routes.homeScreen){
            HomeScreen(navController)
        }

        composable(Routes.categoryProductsScreen){
            CategoriesSection()
        }

    }

}