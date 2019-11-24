package com.zestworks.githubtrendingrepositories.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse

@Database(entities = [GitHubApiResponse::class], version = 1)
abstract class GithubDb: RoomDatabase() {
    abstract val githubDao: GithubDao
}