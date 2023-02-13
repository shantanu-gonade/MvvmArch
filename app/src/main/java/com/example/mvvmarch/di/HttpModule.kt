package com.example.mvvmarch.di

import androidx.viewbinding.BuildConfig
import com.example.mvvmarch.repository.UserInfoRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
object HttpModule {
    const val TIME_OUT_SECONDS = 10
    const val BASE_URL = "https://api.github.com/"

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(userRepository: UserInfoRepository): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(
                        TIME_OUT_SECONDS.toLong(),
                        TimeUnit.SECONDS)
                .readTimeout(
                        TIME_OUT_SECONDS.toLong(),
                        TimeUnit.SECONDS)
                .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = when (BuildConfig.DEBUG) {
                                true -> HttpLoggingInterceptor.Level.BODY
                                false -> HttpLoggingInterceptor.Level.NONE
                            }
                        }
                )
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}
