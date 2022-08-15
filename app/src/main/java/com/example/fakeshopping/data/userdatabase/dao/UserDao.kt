package com.example.fakeshopping.data.userdatabase.dao

import androidx.room.*
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: Users)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user:Users)

    @Query("SELECT * FROM Users WHERE userPhoneNumer = :phoneNumber")
    suspend fun getUserByPhone(phoneNumber:Long): Users?

//    @Query("SELECT favourites FROM Users WHERE userPhoneNumer = :phoneNumber ")
//    suspend fun getUserFavourites(phoneNumber:Long): List<Int>
//
//    @Query("SELECT userOrders FROM Users WHERE userPhoneNumer = :phoneNumber ")
//    suspend fun getUserOrders(phoneNumber:Long): List<UserOrders>
//
//    @Query("SELECT cartItems FROM Users WHERE userPhoneNumer = :phoneNumber ")
//    suspend fun getUserCartItems(phoneNumber:Long): Map<Int,Int>
//
//    @Delete()
//    suspend fun removeUser(phoneNumber: Long)

}