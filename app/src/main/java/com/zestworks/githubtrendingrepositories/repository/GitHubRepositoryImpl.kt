package com.zestworks.githubtrendingrepositories.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import com.blankj.utilcode.util.NetworkUtils
import com.zestworks.githubtrendingrepositories.dagger.AppComponentProvider
import com.zestworks.githubtrendingrepositories.database.GithubDb
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        if (NetworkUtils.isConnected()) {
            gitHubRequestMaker.fetchWeatherForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    responseListener.onFailure()
                }
                .subscribe {
                    if (it.isSuccessful) {
                        val body = it.body()
                        if (body != null && body.isNotEmpty()) {
                            GlobalScope.launch {
                                githubDb.clearAllTables()
                                githubDb.githubDao.addGithub(body)
                            }
                            responseListener.onSuccess()
                        } else {
                            responseListener.onFailure()
                        }
                    } else {
                        responseListener.onFailure()
                    }
                }
        } else {
            responseListener.onFailure()
        }

    }

    override fun getGitHubs(): LiveData<List<GitHubApiResponse>> {
        return githubDb.githubDao.getGitHubApis()
    }
}
