package com.devicedev.socialwavekt.ui.main.fragments.profile

import com.devicedev.socialwavekt.data.retrofit.requests.bodies.EditBody
import com.devicedev.socialwavekt.data.retrofit.requests.bodies.RegisterBody
import com.devicedev.socialwavekt.data.retrofit.services.UserService
import com.devicedev.socialwavekt.data.room.dao.UserDao
import com.devicedev.socialwavekt.ui.BaseRepository
import com.devicedev.socialwavekt.utils.IMAGE_MEDIA_TYPE
import com.devicedev.socialwavekt.utils.createRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileRepository(userDao: UserDao, userService: UserService) : BaseRepository(userDao, userService) {


    suspend fun edit(token: String, name: String, email: String, password: String, gender: String, image: File?) =
        userService.edit(
            token,
            createRequestBody(name),
            createRequestBody(email),
            createRequestBody(password),
            createRequestBody(gender),
            image?.let{
                MultipartBody.Part.createFormData("image", it.name, RequestBody.create(
                    MediaType.parse(IMAGE_MEDIA_TYPE), it
                ))

            }

        )

}