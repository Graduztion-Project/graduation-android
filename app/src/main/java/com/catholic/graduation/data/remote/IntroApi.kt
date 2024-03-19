package com.catholic.graduation.data.remote

import com.catholic.graduation.data.model.request.EmailRequest
import com.catholic.graduation.data.model.request.LoginRequest
import com.catholic.graduation.data.model.request.SignUpRequest
import com.catholic.graduation.data.model.response.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IntroApi {

    @POST("/account/join")
    suspend fun signUp(
        @Body params: SignUpRequest
    ): Response<ResponseBody>

    @POST("/account/login")
    suspend fun login(
        @Body params: LoginRequest
    ): Response<LoginResponse>

    @POST("/account/email/duplicate")
    suspend fun duplicate(
        @Body params: EmailRequest
    ): Response<ResponseBody>

    @POST("/account/email/verification")
    suspend fun verification(
        @Body params: EmailRequest
    ): Response<String>

}