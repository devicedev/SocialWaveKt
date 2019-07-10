package com.devicedev.socialwavekt.ui.login

import com.devicedev.socialwavekt.data.retrofit.requests.bodies.LoginBody
import com.devicedev.socialwavekt.data.retrofit.responses.UserTokenResponse
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.dao.UserDao
import com.devicedev.socialwavekt.data.room.entities.User
import com.devicedev.socialwavekt.ui.BaseRepository

class LoginRepository(userDao: UserDao,userService: UserService) : BaseRepository(userDao,userService) {

    suspend fun login(email: String, password: String) = userService.login(LoginBody(email, password))


}