package com.catholic.graduation.data.repository

import com.catholic.graduation.data.model.request.EmailRequest
import com.catholic.graduation.data.model.request.LoginRequest
import com.catholic.graduation.data.model.request.RenwalRequest
import com.catholic.graduation.data.model.request.SignUpRequest
import com.catholic.graduation.data.model.response.LoginResponse
import com.catholic.graduation.data.model.response.VerificationEmailResponse
import okhttp3.ResponseBody

interface IntroRepository {

    suspend fun signUp(
        body: SignUpRequest
    ): Result<ResponseBody>

    suspend fun login(
        body: LoginRequest
    ): Result<LoginResponse>

    suspend fun duplicate(
        body: EmailRequest
    ): Result<ResponseBody>

    suspend fun verification(
        body: EmailRequest
    ): Result<String>

    suspend fun verificationEmail(
        body: EmailRequest
    ): Result<VerificationEmailResponse>

    suspend fun renwal(
        body: RenwalRequest
    ): Result<ResponseBody>

}