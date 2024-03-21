package com.catholic.graduation.data.model.request

data class RenwalRequest(
    val email : String,
    val password: String,
    val verificationToken: String
)
