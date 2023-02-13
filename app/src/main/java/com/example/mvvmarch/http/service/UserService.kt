package com.example.mvvmarch.http.service

import com.example.mvvmarch.entity.ReceivedEvent
import com.example.mvvmarch.entity.Repo
import com.example.mvvmarch.entity.SearchResult
import com.example.mvvmarch.entity.UserInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("user")
    suspend fun fetchUserOwner(@Header("Authorization") authorization: String): Response<UserInfo>

    @GET("users/{username}/received_events?")
    suspend fun queryReceivedEvents(@Path("username") username: String,
                                    @Query("page") pageIndex: Int,
                                    @Query("per_page") perPage: Int): List<ReceivedEvent>

    @GET("users/{username}/repos?")
    suspend fun queryRepos(@Path("username") username: String,
                           @Query("page") pageIndex: Int,
                           @Query("per_page") perPage: Int,
                           @Query("sort") sort: String): List<Repo>

    @GET("search/repositories")
    suspend fun search(@Query("q") q: String,
                       @Query("page") pageIndex: Int,
                       @Query("per_page") perPage: Int): SearchResult

}