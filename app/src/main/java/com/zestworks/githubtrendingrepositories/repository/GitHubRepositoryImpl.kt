package com.zestworks.githubtrendingrepositories.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.zestworks.githubtrendingrepositories.dagger.AppComponentProvider
import com.zestworks.githubtrendingrepositories.database.GithubDb
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GitHubRepositoryImpl : GitHubRepository {

    @Inject
    lateinit var gitHubRequestMaker: GitHubRequestMaker

    @Inject
    lateinit var githubDb: GithubDb

    init {
        AppComponentProvider.appComponent().inject(this)
    }

    @SuppressLint("CheckResult")
    override fun fetchGitHubRepositories(responseListener: ResponseListener) {
        gitHubRequestMaker.fetchWeatherForecast()
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (it.isSuccessful) {
                    val body = it.body()
                    if (body != null && body.isNotEmpty()) {
                        githubDb.clearAllTables()
                        githubDb.githubDao.addGithub(body)
                        responseListener.onSuccess()
                    } else {
                        responseListener.onFailure()
                    }
                } else {
                    responseListener.onFailure()
                }
            }
    }

    override fun getGitHubs(): LiveData<List<GitHubApiResponse>> {
        return githubDb.githubDao.getGitHubApis()
    }
}
