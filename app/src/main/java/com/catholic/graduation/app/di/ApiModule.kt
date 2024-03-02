package com.catholic.graduation.app.di

import com.catholic.graduation.data.remote.IntroApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object ApiModule {

    @Singleton
    @Provides
    fun provideIntroApi(retrofit: Retrofit): IntroApi = retrofit.create(IntroApi::class.java)

}