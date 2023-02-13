package com.example.mvvmarch.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
import com.example.mvvmarch.repository.UserInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserInfoRepository(userDataStore: DataStore<UserPreferences>): UserInfoRepository {
        return UserInfoRepository.getInstance(userDataStore)
    }

    @Provides
    @Singleton
    fun providePreferencesDataStore(application: Application): DataStore<UserPreferences> {
        return application.createDataStore(
                fileName = "user_prefs.pb",
                serializer = UserPreferencesSerializer
        )
    }
}


data class UserPreferences(val username: String, val password: String, val autoLogin: Boolean)