package com.example.fakeshopping.data.userdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users(

    @PrimaryKey val userPhoneNumer:Long?,
    val favourites:List<Int>,
    val cartItems:Map<Int,Int>,
    val userAddress:String?,
    val userFullName:String,
    val userOrders:List<UserOrders>


)