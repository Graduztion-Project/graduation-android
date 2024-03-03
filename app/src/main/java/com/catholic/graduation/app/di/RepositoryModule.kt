package com.catholic.graduation.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

//    @Singleton
//    @Binds
//    abstract fun bindIntroRepository(introRepositoryImpl: IntroRepositoryImpl): IntroRepository

}