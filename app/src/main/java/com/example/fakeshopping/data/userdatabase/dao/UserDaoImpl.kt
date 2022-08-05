package com.example.fakeshopping.data.userdatabase.dao

import com.example.fakeshopping.data.userdatabase.Users

class UserDaoImpl: UserDao {

    override suspend fun getUserByPhone(phoneNumber: Long): Users {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: Users) {
        TODO("Not yet implemented")
    }

    override suspend fun removeUser(user: Users) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(
        oldPhonenumber: Long,
        newPhoneNumber: Long,
        userAddress: String,
        userName: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserFavourites(newFavouriteItems: List<Int>) {
        TODO("Not yet implemented")
    }

    override suspend fun addUserFavourites(vararg newItems: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserFavourites(vararg items: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserCart(newCartItems: Map<Int, Int>) {
        TODO("Not yet implemented")
    }

    override suspend fun addUserCartItems(vararg newItems: Map<Int, Int>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserCartItemsQuantity(itemId: Int, newQuantity: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserCartItems(vararg items: Int) {
        TODO("Not yet implemented")
    }

}