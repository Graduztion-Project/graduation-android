package com.catholic.graduation.data.model.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)