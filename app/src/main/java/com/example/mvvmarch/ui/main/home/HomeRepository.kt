package com.example.mvvmarch.ui.main.home

import android.annotation.SuppressLint
import androidx.paging.*
import androidx.room.withTransaction
import com.example.mvvmarch.arch.base.repository.BaseRepositoryBoth
import com.example.mvvmarch.arch.base.repository.ILocalDataSource
import com.example.mvvmarch.arch.base.repository.IRemoteDataSource
import com.example.mvvmarch.arch.ext.globalPagingConfig
import com.example.mvvmarch.db.UserDatabase
import com.example.mvvmarch.entity.ReceivedEvent
import com.example.mvvmarch.http.service.ServiceManager
import com.example.mvvmarch.manager.UserManager
import com.example.mvvmarch.utils.PAGING_REMOTE_PAGE_SIZE
import com.example.mvvmarch.utils.toast
import javax.inject.Inject

@SuppressLint("CheckResult")
class HomeRepository @Inject constructor(
        remoteDataSource: HomeRemoteDataSource,
        localDataSource: HomeLocalDataSource
) : BaseRepositoryBoth<HomeRemoteDataSource, HomeLocalDataSource>(remoteDataSource, localDataSource) {

    @OptIn(ExperimentalPagingApi::class)
    fun fetchPager(): Pager<Int, ReceivedEvent> {
        val username: String = UserManager.INSTANCE.login
        val remoteMediator = HomeRemoteMediator(username, remoteDataSource, localDataSource)
        return Pager(
                config = globalPagingConfig,
                remoteMediator = remoteMediator,
                pagingSourceFactory = { localDataSource.fetchPagedListFromLocal() }
        )
    }
}

class HomeRemoteDataSource @Inject constructor(private val serviceManager: ServiceManager) :
    IRemoteDataSource {

    suspend fun queryReceivedEvents(username: String,
                                    pageIndex: Int,
                                    perPage: Int): List<ReceivedEvent> {
        return serviceManager.userService.queryReceivedEvents(username, pageIndex, perPage)
    }
}

@SuppressLint("CheckResult")
class HomeLocalDataSource @Inject constructor(private val db: UserDatabase) : ILocalDataSource {

    fun fetchPagedListFromLocal(): PagingSource<Int, ReceivedEvent> {
        return db.userReceivedEventDao().queryEvents()
    }

    suspend fun clearAndInsertNewData(data: List<ReceivedEvent>) {
        db.withTransaction {
            db.userReceivedEventDao().clearReceivedEvents()
            insertDataInternal(data)
        }
    }

    suspend fun insertNewPagedEventData(newPage: List<ReceivedEvent>) {
        db.withTransaction { insertDataInternal(newPage) }
    }

    suspend fun fetchNextIndex(): Int {
        return db.withTransaction {
            db.userReceivedEventDao().getNextIndexInReceivedEvents() ?: 0
        }
    }

    private suspend fun insertDataInternal(newPage: List<ReceivedEvent>) {
        val start = db.userReceivedEventDao().getNextIndexInReceivedEvents() ?: 0
        val items = newPage.mapIndexed { index, child ->
            child.indexInResponse = start + index
            child
        }
        db.userReceivedEventDao().insert(items)
    }
}

@OptIn(ExperimentalPagingApi::class)
class HomeRemoteMediator(
        private val username: String,
        private val remoteDataSource: HomeRemoteDataSource,
        private val localDataSource: HomeLocalDataSource
) : RemoteMediator<Int, ReceivedEvent>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ReceivedEvent>): MediatorResult {
        return try {
            val pageIndex = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val nextIndex = localDataSource.fetchNextIndex()
                    if (nextIndex % PAGING_REMOTE_PAGE_SIZE != 0) {
                        return MediatorResult.Success(true)
                    }
                    nextIndex / PAGING_REMOTE_PAGE_SIZE + 1
                }
            }
            val data = remoteDataSource.queryReceivedEvents(username, pageIndex, PAGING_REMOTE_PAGE_SIZE)
            if (loadType == LoadType.REFRESH) {
                localDataSource.clearAndInsertNewData(data)
            } else {
                localDataSource.insertNewPagedEventData(data)
            }
            MediatorResult.Success(data.isEmpty())
        } catch (exception: Exception) {
            toast (exception.toString())
            MediatorResult.Error(exception)
        }
    }

}
