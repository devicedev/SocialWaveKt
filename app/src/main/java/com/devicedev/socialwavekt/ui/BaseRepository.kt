package com.devicedev.socialwavekt.ui

import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.dao.UserDao
import com.devicedev.socialwavekt.data.room.entities.User

open class BaseRepository(
    protected val userDao: UserDao,
    protected val userService: UserService
) {
    suspend fun store(user: User) = userDao.insert(user)

    suspend fun delete() = userDao.delete()

    suspend fun getCurrent(token: String) = userService.get(token)

    fun get() = userDao.get()

}