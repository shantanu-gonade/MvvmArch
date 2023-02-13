package com.example.mvvmarch.ui.login

import com.example.mvvmarch.app.Results
import com.example.mvvmarch.arch.base.repository.BaseRepositoryBoth
import com.example.mvvmarch.arch.base.repository.ILocalDataSource
import com.example.mvvmarch.arch.base.repository.IRemoteDataSource
import com.example.mvvmarch.db.UserDatabase
import com.example.mvvmarch.entity.UserInfo
import com.example.mvvmarch.http.service.ServiceManager
import com.example.mvvmarch.manager.UserManager
import com.example.mvvmarch.repository.UserInfoRepository
import com.example.mvvmarch.utils.processApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginRepository @Inject constructor(
    remoteDataSource: LoginRemoteDataSource,
    localDataSource: LoginLocalDataSource
) : BaseRepositoryBoth<LoginRemoteDataSource, LoginLocalDataSource>(remoteDataSource, localDataSource) {

    suspend fun login(username: String, password: String): Results<UserInfo> {
        // 保存用户登录信息
        localDataSource.savePrefUser(username, password)
        val userInfo = remoteDataSource.login()

        // 如果登录失败，清除登录信息
        when (userInfo) {
            is Results.Failure -> localDataSource.clearPrefsUser()
            is Results.Success -> UserManager.INSTANCE = requireNotNull(userInfo.data)
        }
        return userInfo
    }

    fun fetchAutoLogin(): Flow<AutoLoginEvent> {
        return localDataSource.fetchAutoLogin()
    }
}

class LoginRemoteDataSource @Inject constructor(
    private val serviceManager: ServiceManager
) : IRemoteDataSource {

    suspend fun login(): Results<UserInfo> {
        // auth token move to secure place
        val auth = "token"
        return processApiResponse { serviceManager.userService.fetchUserOwner(auth) }
    }
}

class LoginLocalDataSource @Inject constructor(
    private val db: UserDatabase,
    private val userRepository: UserInfoRepository
) : ILocalDataSource {

    suspend fun savePrefUser(username: String, password: String) {
        userRepository.saveUserInfo(username, password)
    }

    suspend fun clearPrefsUser() {
        userRepository.saveUserInfo("", "")
    }

    fun fetchAutoLogin(): Flow<AutoLoginEvent> {
        return userRepository.fetchUserInfoFlow()
            .map { user ->
                val username = user.username
                val password = user.password
                val isAutoLogin = user.autoLogin
                when (username.isNotEmpty() && password.isNotEmpty() && isAutoLogin) {
                    true -> AutoLoginEvent(true, username, password)
                    false -> AutoLoginEvent(false, "", "")
                }
            }
    }
}

data class AutoLoginEvent(
    val autoLogin: Boolean,
    val username: String,
    val password: String
)
