package com.catholic.graduation.data.repository

import com.catholic.graduation.data.model.request.SignUpRequest
import okhttp3.ResponseBody

interface IntroRepository {

    suspend fun signUp(
        body:SignUpRequest
    ) : Result<ResponseBody>

}