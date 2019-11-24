package com.zestworks.githubtrendingrepositories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zestworks.githubtrendingrepositories.repository.GitHubRepositoryImpl

object ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GitHubViewModel(GitHubRepositoryImpl()) as T
    }
}