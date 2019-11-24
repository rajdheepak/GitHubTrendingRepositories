package com.zestworks.githubtrendingrepositories.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.zestworks.githubtrendingrepositories.database.GitHubDataBaseHolder
import com.zestworks.githubtrendingrepositories.repository.GitHubRepositoryImpl

object ViewModelFactory {
    fun getGithubViewModel(activity: AppCompatActivity): GitHubViewModel {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GitHubViewModel(GitHubRepositoryImpl(GitHubDataBaseHolder.getDb(activity.applicationContext)!!)) as T
            }

        })[GitHubViewModel::class.java]
    }
}