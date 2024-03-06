package com.catholic.graduation.data.remote

import com.catholic.graduation.data.model.request.SignUpRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IntroApi {

    @POST("/join")
    suspend fun signUp(
        @Body params:SignUpRequest
    ): Response<ResponseBody>

}