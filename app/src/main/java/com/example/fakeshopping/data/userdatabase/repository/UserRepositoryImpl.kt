package com.example.fakeshopping.data.userdatabase.repository

import com.example.fakeshopping.data.userdatabase.UserOrders
import com.example.fakeshopping.data.userdatabase.Users
import com.example.fakeshopping.data.userdatabase.dao.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl( private val userDao: UserDao): UserRepository {

    override suspend fun addUser(user: Users) {
        userDao.addUser(user)
    }

    override suspend fun updateUser(user: Users) {
        userDao.updateUser(user)
    }

    override suspend fun getUserByPhone(phoneNumber: Long): Users? {
        return userDao.getUserByPhone(phoneNumber)
    }

    override suspend fun getAllUsers(): List<Users> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserFavourites(phoneNumber: Long): List<Int> {
        val user = userDao.getUserByPhone(phoneNumber)!!
        return user.favourites
    }

    override suspend fun getUserAddress(phoneNumber: Long): UserAddress {
        val user = userDao.getUserByPhone(phoneNumber)!!
        return user.userAddress
    }

    override suspend fun getUserOrders(phoneNumber: Long): List<UserOrders> {
        val user = userDao.getUserByPhone(phoneNumber)!!
        return user.userOrders
    }

    override suspend fun getUserCartItems(phoneNumber: Long): Map<Int, Int> {
        val user = userDao.getUserByPhone(phoneNumber)!!
        return user.cartItems
    }

    override suspend fun removeUser(phoneNumber: Long) {
        val target = userDao.getUserByPhone(phoneNumber)!!
        userDao.removeUser(target)
    }

    override suspend fun addItemToFavourites(phone: Long, itemId: Int) {
        val user = userDao.getUserByPhone(phone)!!
        user.favourites.add(itemId)
        userDao.updateUser(user)
    }

    override suspend fun addItemToCart(phone: Long, itemId: Int, quantity: Int) {

        val user = userDao.getUserByPhone(phone)!!
        user.cartItems[itemId] = quantity
        userDao.updateUser(user)

    }

    override suspend fun removeItemFromFavourites(phone: Long, itemId: Int) {

        val user = userDao.getUserByPhone(phone)!!
        user.favourites.remove(itemId)
        userDao.updateUser(user)
    }

    override suspend fun removeItemFromCart(phone: Long, itemId: Int) {
        val user = userDao.getUserByPhone(phone)!!
        user.cartItems.remove(itemId)
        userDao.updateUser(user)

    }

    override suspend fun increseCartItemQuantity(phone: Long, itemId: Int, incrementBy: Int) {

        val user = userDao.getUserByPhone(phone)!!
        user.cartItems[itemId] = user.cartItems[itemId]!! + incrementBy
        userDao.updateUser(user)

    }

    override suspend fun decreaseCartItemQuantity(phone: Long, itemId: Int, decreaseBy: Int) {
        val user = userDao.getUserByPhone(phone)!!
        if(user.cartItems[itemId]!! - decreaseBy >= 1){
            user.cartItems[itemId] = user.cartItems[itemId]!! - decreaseBy
            userDao.updateUser(user)
        }
    }

}