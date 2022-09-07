package com.example.fakeshopping.ui.presentation.myprofile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fakeshopping.ui.model.myprofileViewmodels.MyProfileScreenViewmodel
import com.example.fakeshopping.ui.presentation.components.IconButton
import com.example.fakeshopping.utils.MyProfileScreenRoutes
import com.example.fakeshopping.utils.Routes

@Composable
fun MyProfileScreen(rootNavController:NavHostController, currentUser:String, onLoggedStateChanged:(String?)->Unit){

    val myProfilefragmentsNavController = rememberNavController()


    Scaffold(
        modifier=Modifier.fillMaxSize(),
    ) {

        NavHost(navController = myProfilefragmentsNavController, startDestination = MyProfileScreenRoutes.mainFragment ) {

            composable(route = MyProfileScreenRoutes.mainFragment) {
                MyProfileScreenMainFragment(
                    currentUser = currentUser,
                    myprofileNavController = myProfilefragmentsNavController,
                    onLogout = {
                        onLoggedStateChanged(null)
                        rootNavController.navigate(Routes.loginSignupScreen)
                        rootNavController.backQueue.clear()
                    },
                    onProfileBackPress = {
                        rootNavController.popBackStack()
                    }
                )
            }

            composable(route = MyProfileScreenRoutes.editingFragment) {
                UpdateUserDetailsScreen(
                    myprofileNavController = myProfilefragmentsNavController,
                    currentUserId = currentUser,
                    onBackPress = {

                    })
            }

        }

    }

}

