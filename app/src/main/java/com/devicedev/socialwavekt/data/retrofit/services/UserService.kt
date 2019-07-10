package com.devicedev.socialwavekt.data.retrofit.services

import com.devicedev.socialwavekt.data.retrofit.requests.bodies.EditBody
import com.devicedev.socialwavekt.data.retrofit.requests.bodies.LoginBody
import com.devicedev.socialwavekt.data.retrofit.requests.bodies.RegisterBody
import com.devicedev.socialwavekt.data.retrofit.responses.UserResponse
import com.devicedev.socialwavekt.data.retrofit.responses.UserTokenResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    companion object {
        const val HEADER_TOKEN_KEY = "x-token"
    }

    @GET("api/users")
    suspend fun get(@Header(HEADER_TOKEN_KEY) token: String): Response<UserResponse>

    @POST("api/users/login")
    suspend fun login(@Body loginBody: LoginBody): Response<UserTokenResponse>

    @POST("api/users/register")
    suspend fun register(@Body registerBody: RegisterBody): Response<UserTokenResponse>

    @Multipart
    @PUT("api/users")
    suspend fun edit(
        @Header(HEADER_TOKEN_KEY) token: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<UserResponse>

    @GET("api/users/logout")
    suspend fun logout(@Header(HEADER_TOKEN_KEY) token: String)


}