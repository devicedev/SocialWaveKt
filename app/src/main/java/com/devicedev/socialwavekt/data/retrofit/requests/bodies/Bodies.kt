package com.devicedev.socialwavekt.data.retrofit.requests.bodies

data class LoginBody(

    private val email: String,

    private val password: String
)

data class RegisterBody(
    private val name: String,
    private val email: String,
    private val password: String,
    private val gender: String,
    private val birthday: String
)
data class EditBody(
    private val name: String,
    private val email: String,
    private val password: String,
    private val gender: String
)