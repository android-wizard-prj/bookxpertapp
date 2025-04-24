package com.example.bookxpertapplication.repositories

import com.example.bookxpertapplication.interfaces.UserDao
import com.example.bookxpertapplication.roomdatabase.UserEntity


class UserRepository(private val userDao: UserDao) {
    suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)
    suspend fun getUser(): UserEntity? = userDao.getLoggedInUser()
}