package com.example.fakeshopping.data.userdatabase.repository

import androidx.room.*
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    suspend fun addUser(user: Users)

    suspend fun updateUser(user:Users)

    suspend fun getUserByPhone(phoneNumber:Long): Users?

    suspend fun getUserFavourites(phoneNumber:Long): List<Int>

    suspend fun getUserOrders(phoneNumber:Long): List<UserOrders>

    suspend fun getUserCartItems(phoneNumber:Long): Map<Int, Int>

    suspend fun removeUser(phoneNumber: Long)

    suspend fun addItemToFavourites(phone:Long, itemId:Int)

    suspend fun addItemToCart(phone:Long, itemId:Int, quantity:Int = 1)

    suspend fun removeItemFromFavourites(phone:Long, itemId:Int)

    suspend fun removeItemFromCart(phone:Long, itemId:Int)

    suspend fun increseCartItemQuantity(phone:Long, itemId:Int, incrementBy: Int)

    suspend fun decreaseCartItemQuantity(phone:Long, itemId:Int, decreaseBy: Int)



}