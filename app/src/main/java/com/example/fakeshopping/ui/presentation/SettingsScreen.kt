package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.ui.model.SettingScreenViewModel
import com.example.fakeshopping.ui.presentation.components.SimpleAppScreensTopBar
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.ui.theme.ColorYellowVarient
import com.example.fakeshopping.utils.Routes

@Composable
fun SettingsScreen(navController:NavHostController, currentUserId:String) {

    val viewModel = hiltViewModel<SettingScreenViewModel>()

    LaunchedEffect(key1 = true, block = {
        viewModel.setCurrentUserAndInitViewModel(currentUserId)
    })

    Scaffold(
        topBar = {
            SimpleAppScreensTopBar(
                title = "Settings",
                onBackArrowPress = {
                   navController.popBackStack()
                }
            )
        }
    ) {

        Column{

            //General Settings Section
            Spacer(modifier = Modifier.height(12.dp))
            SettingSection(heading = "General") {

                SettingItem(
                    icon = Icons.Default.Email, title = "E-Mail letters",
                    isEnabled = viewModel.isMailLettersEnabled,
                    onCheckChange = {
                        viewModel.onMailLettersSettingSwitchToggle(it)
                    }
                )

                SettingItem(
                    icon = Icons.Default.LocationOn,
                    title = "Change address based on recent order",
                    isEnabled = viewModel.addressChangingSchemeEnabled,
                    onCheckChange = {
                        viewModel.onAddressSchemeSettingSwitchToggle(it)
                    }
                )

                SettingItem(
                    icon = Icons.Default.Phone, title = "Help & Supoport",
                    onClick = { navController.navigate(Routes.supportScreen) }
                )
            }

            //Notifications Settings section
            Spacer(modifier = Modifier.height(8.dp))
            SettingSection(heading = "Notifications") {

                SettingItem(
                    icon = Icons.Default.ShoppingCart, title = "Order Updates",
                    isEnabled = viewModel.isOrderupdatesEnabled,
                    onCheckChange = {
                        viewModel.onOrderUpdatesSettingSwitchToggle(it)
                    },
                    settingItemEnabled = false
                )

                SettingItem(
                    icon = Icons.Default.Email, title = "Otp Codes" ,
                    isEnabled = viewModel.isOtpCodesEnabled,
                    onCheckChange = {
                        viewModel.onOtpCodesSettingSwitchToggle(it)
                    },
                    settingItemEnabled = false
                )

            }

        }


    }

}

@Composable
private fun SettingItem( icon:ImageVector, title:String, onClick:()->Unit ){

    Row (
        modifier= Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Icon(
            imageVector = icon, contentDescription = null,
            modifier=Modifier.padding(start= 18.dp)
        )
        Spacer(modifier=Modifier.width(8.dp))
        Text(
            text= title, fontSize = 16.sp,
            modifier=Modifier.padding(start= 12.dp)
        )

    }

}

@Composable
private fun SettingItem(
    icon:ImageVector,
    title:String, onCheckChange:(setToenabled:Boolean)->Unit,
    isEnabled: State<Boolean>,
    settingItemEnabled:Boolean = true
){

    Row (
        modifier= Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        Icon(
            imageVector = icon, contentDescription = null,
            modifier=Modifier.padding(start= 18.dp),
        )

        Spacer(modifier=Modifier.width(8.dp))
        Text(
            text= title, fontSize = 14.sp,
            modifier= Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(0.75f),

        )

        val x = remember{ mutableStateOf(true) }

        Switch(
            checked = isEnabled.value,
            onCheckedChange = {
               onCheckChange(it)
            },
            enabled = settingItemEnabled,
            colors = SwitchDefaults.colors(
                checkedTrackColor = ColorYellow,
                uncheckedThumbColor = ColorWhiteVariant,
                checkedThumbColor = ColorYellowVarient,
            )

        )

    }

}

@Composable
private fun SettingSection(
    heading:String,
    Content: @Composable ColumnScope.()->Unit,
){

    Column(
        modifier= Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ){

        Text(
            text= heading,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier=Modifier.padding(start= 12.dp)
        )
        Spacer(Modifier.width(8.dp))

        Content()

    }

}