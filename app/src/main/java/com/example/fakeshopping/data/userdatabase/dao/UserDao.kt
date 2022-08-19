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

    @Update()
    suspend fun updateUser(user:Users)

    @Query("SELECT * FROM Users WHERE userPhoneNumer = :phoneNumber")
    suspend fun getUserByPhone(phoneNumber:Long): Users?

    @Delete()
    suspend fun removeUser(user:Users)



}