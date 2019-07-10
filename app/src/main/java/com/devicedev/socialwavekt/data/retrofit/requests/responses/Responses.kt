package com.devicedev.socialwavekt.data.retrofit.responses

import com.devicedev.socialwavekt.data.room.entities.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("user")
    val user: User
)

data class UserTokenResponse(
    @SerializedName("user")
    val user: User,
    val token: String
)

data class ErrorMessage(val message: String)


/*

class ApiError constructor(error: Throwable) {
    var message = "An error occurred"

    init {
        if (error is HttpException) {
            val errorJsonString = error.response()!!
                .errorBody()?.string()
            this.message = JsonParser().parse(errorJsonString)
                .asJsonObject["message"]
                .asString
        } else {
            this.message = error.message ?: this.message
        }
    }
}*/