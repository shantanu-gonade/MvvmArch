package com.example.mvvmarch.ui.search

import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mvvmarch.arch.base.repository.BaseRepositoryRemote
import com.example.mvvmarch.arch.base.repository.IRemoteDataSource
import com.example.mvvmarch.arch.ext.globalPagingConfig
import com.example.mvvmarch.entity.Repo
import com.example.mvvmarch.http.service.UserService
import com.example.mvvmarch.utils.PAGING_REMOTE_PAGE_SIZE
import javax.inject.Inject

class SearchRepository @Inject constructor(
        remoteDataSource: SearchRemoteDataSource
) : BaseRepositoryRemote<SearchRemoteDataSource>(remoteDataSource) {

    fun fetchPager(keyWord: String): Pager<Int, Repo> {
        return remoteDataSource.getPager(keyWord)
    }
}

class SearchRemoteDataSource @Inject constructor(
        private val userService: UserService
) : IRemoteDataSource {

    fun getPager(keyWord: String): Pager<Int, Repo> {
        return Pager(
                config = globalPagingConfig,
                pagingSourceFactory = { SearchPagingSource(userService, keyWord) }
        )
    }
}

class SearchPagingSource(
        private val userService: UserService,
        private val keyWord: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        if (params is LoadParams.Prepend) {
            return LoadResult.Page(
                    data = listOf(),
                    prevKey = null,
                    nextKey = null
            )
        }
        return try {
            val key = if (params.key == null) 1 else params.key as Int
            val searchResult = userService.search(keyWord, key, PAGING_REMOTE_PAGE_SIZE)
            LoadResult.Page(
                    data = searchResult.items,
                    prevKey = key - 1,
                    nextKey = key + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        TODO("Not yet implemented")
    }
}
