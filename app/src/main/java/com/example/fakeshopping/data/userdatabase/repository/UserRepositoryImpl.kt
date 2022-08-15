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

//    override suspend fun getUserFavourites(phoneNumber: Long): Flow<List<Int>> {
//        return userDao.getUserFavourites(phoneNumber)
//    }
//
//    override suspend fun getUserOrders(phoneNumber: Long): Flow<List<UserOrders>> {
//        return userDao.getUserOrders(phoneNumber)
//    }
//
//    override suspend fun getUserCartItems(phoneNumber: Long): Flow<Map<Int, Int>> {
//        return userDao.getUserCartItems(phoneNumber)
//    }

//    override suspend fun removeUser(phoneNumber: Long) {
//        return userDao.removeUser(phoneNumber)
//    }

}