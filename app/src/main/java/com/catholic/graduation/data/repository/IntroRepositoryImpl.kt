package com.catholic.graduation.data.repository

import com.catholic.graduation.data.remote.IntroApi
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val api: IntroApi
) : IntroRepository{
}