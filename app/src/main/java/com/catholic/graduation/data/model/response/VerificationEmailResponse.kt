package com.catholic.graduation.data.model.response

data class VerificationEmailResponse(
    val verificationCode: String,
    val verificationToken: String
)
