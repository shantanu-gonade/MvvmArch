package com.example.mvvmarch.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmarch.entity.ReceivedEvent
import com.example.mvvmarch.entity.Repo

@Database(
    entities = [ReceivedEvent::class, Repo::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userReceivedEventDao(): UserReceivedEventDao

    abstract fun userReposDao(): UserReposDao
}