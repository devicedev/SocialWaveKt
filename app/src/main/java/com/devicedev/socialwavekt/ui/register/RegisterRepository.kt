package com.devicedev.socialwavekt.ui.register

import com.devicedev.socialwavekt.data.retrofit.requests.bodies.RegisterBody
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.dao.UserDao
import com.devicedev.socialwavekt.ui.BaseRepository

class RegisterRepository(userDao: UserDao, userService: UserService) : BaseRepository(userDao, userService) {


    suspend fun register(name: String, email: String, password: String, gender: String, birthday: String) =
        userService.register(
            RegisterBody(name, email, password, gender, birthday)
        )


}