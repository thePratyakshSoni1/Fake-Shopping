package com.example.fakeshopping.data.userdatabase.repository

import androidx.room.*
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    suspend fun addUser(user: Users)

    suspend fun updateUser(user:Users)

    suspend fun getUserByPhone(phoneNumber:Long): Users?

//    suspend fun getUserFavourites(phoneNumber:Long): Flow<List<Int>>
//
//    suspend fun getUserOrders(phoneNumber:Long): Flow<List<UserOrders>>
//
//    suspend fun getUserCartItems(phoneNumber:Long): Flow<Map<Int, Int>>

//    suspend fun removeUser(phoneNumber: Long)



}