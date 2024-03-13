package com.catholic.graduation.data.repository

import com.catholic.graduation.data.model.request.DuplicateRequest
import com.catholic.graduation.data.model.request.LoginRequest
import com.catholic.graduation.data.model.request.SignUpRequest
import com.catholic.graduation.data.model.response.LoginResponse
import com.catholic.graduation.data.model.runRemote
import com.catholic.graduation.data.remote.IntroApi
import okhttp3.ResponseBody
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository{
    override suspend fun signUp(body: SignUpRequest): Result<ResponseBody> = runRemote { api.signUp(body) }
    override suspend fun login(body: LoginRequest): Result<LoginResponse> = runRemote { api.login(body) }
    override suspend fun duplicate(body: DuplicateRequest): Result<ResponseBody> = runRemote { api.duplicate(body) }
}