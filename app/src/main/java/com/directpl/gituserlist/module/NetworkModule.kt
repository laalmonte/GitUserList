package com.directpl.gituserlist.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.directpl.gituserlist.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.directpl.gituserlist.BuildConfig

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideItunesService(retrofit: Retrofit) =
        retrofit.create(UserService::class.java)


}