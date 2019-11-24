package com.zestworks.githubtrendingrepositories.database

import android.content.Context
import androidx.room.Room

class GitHubDataBaseHolder {
    companion object {
        private var githubDb: GithubDb? = null
        fun getDb(context: Context): GithubDb? {
            if(githubDb == null) {
                synchronized(this) {
                    githubDb = Room.databaseBuilder(context,GithubDb::class.java,"todo").build()
                    return githubDb
                }
            }
            return githubDb
        }
    }
}