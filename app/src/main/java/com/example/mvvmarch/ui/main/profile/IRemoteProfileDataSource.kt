package com.example.mvvmarch.ui.main.profile

import com.example.mvvmarch.arch.base.repository.BaseRepositoryRemote
import com.example.mvvmarch.arch.base.repository.IRemoteDataSource
import com.example.mvvmarch.http.service.ServiceManager
import javax.inject.Inject

interface IRemoteProfileDataSource : IRemoteDataSource

class ProfileRepository @Inject constructor(
        remoteDataSource: ProfileRemoteDataSource
) : BaseRepositoryRemote<IRemoteProfileDataSource>(remoteDataSource)

class ProfileRemoteDataSource @Inject constructor(
        val serviceManager: ServiceManager
) : IRemoteProfileDataSource