package com.example.fakeshopping.data.userdatabase.dao

import com.example.fakeshopping.data.userdatabase.Users

interface UserDao {

    suspend fun getUserByPhone(phoneNumber:Long): Users

    suspend fun addUser(user: Users)

    suspend fun removeUser(user: Users)

    suspend fun updateUserProfile(oldPhonenumber:Long, newPhoneNumber:Long, userAddress:String, userName:String)

    suspend fun updateUserFavourites(newFavouriteItems:List<Int>)
    suspend fun addUserFavourites(vararg newItems:Int)
    suspend fun removeUserFavourites(vararg items:Int)

    suspend fun updateUserCart(newCartItems:Map<Int,Int>)
    suspend fun addUserCartItems(vararg newItems:Map<Int,Int>)
    suspend fun updateUserCartItemsQuantity(itemId:Int, newQuantity:Int)
    suspend fun removeUserCartItems(vararg items:Int )

}