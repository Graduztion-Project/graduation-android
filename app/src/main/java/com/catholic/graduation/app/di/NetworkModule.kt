package com.catholic.graduation.app.di

import com.catholic.graduation.BuildConfig
import com.catholic.graduation.app.DataStoreManager
import com.catholic.graduation.config.AccessTokenInterceptor
import com.catholic.graduation.config.BearerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    var isProd = true

    fun changeVersion(state: Boolean){
        isProd = state
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_PROD_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        bearerInterceptor: BearerInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(accessTokenInterceptor)
        .addInterceptor(bearerInterceptor)
        .build()

    @Provides
    fun provideAccessTokenInterceptor(dataStoreManager: DataStoreManager): AccessTokenInterceptor =
        AccessTokenInterceptor(dataStoreManager)

    @Provides
    fun provideBearerInterceptor(dataStoreManager: DataStoreManager): BearerInterceptor =
        BearerInterceptor(dataStoreManager)

}