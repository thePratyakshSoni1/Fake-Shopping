package com.example.fakeshopping.ui.presentation.myorders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.UserOrdersViewModel

@Composable
fun UserOrdersScreen(currentUserId:String){

    val viewModel = hiltViewModel<UserOrdersViewModel>()
    LaunchedEffect(key1 = true){
        viewModel.setCurrentUser(currentUserId.toLong())
    }

    Box(){
        LazyColumn(
            modifier= Modifier
                .fillMaxSize()
                .padding(12.dp)
        ){
            items(viewModel.userOrders){ it ->
                Card(modifier= Modifier
                    .fillMaxWidth(0.9f)
                    .padding(14.dp)){
                    Text(text= "${it.orderId}\n${it.orderDateTime}\n${it.orderDeliveryAddress}")
                }
            }
        }
    }

}