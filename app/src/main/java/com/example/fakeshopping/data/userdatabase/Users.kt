package com.example.fakeshopping.data.userdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fakeshopping.data.userdatabase.repository.UserAddress

@Entity
data class Users(

    @PrimaryKey var userPhoneNumer:Long,
    var password:String,
    val favourites:MutableList<Int>,
    val cartItems:MutableMap<Int,Int>,
    val userAddress:UserAddress,
    var userFirstName:String,
    var userLastName:String,
    val userOrders:MutableList<UserOrders>


)