package com.example.mvvmarch.repository

import androidx.datastore.core.DataStore
import com.example.mvvmarch.arch.util.SingletonHolderSingleArg
import com.example.mvvmarch.di.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

// TODO: save user preferences? and supress?
@Suppress("PrivatePropertyName")
class UserInfoRepository(private val dataStore: DataStore<UserPreferences>) {

    fun fetchUserInfoFlow(): Flow<UserPreferences> {
        return dataStore.data.catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw it
            }
        }
    }

    suspend fun saveUserInfo(username: String,
                             password: String) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setUsername(username)
                .setPassword(password)
                .setAutoLogin(username.isNotEmpty() && password.isNotEmpty())
                .build()
        }
    }

    companion object :
        SingletonHolderSingleArg<UserInfoRepository, DataStore<UserPreferences>>(::UserInfoRepository)
}