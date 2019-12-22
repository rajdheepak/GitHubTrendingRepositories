package com.zestworks.githubtrendingrepositories.repository

import androidx.lifecycle.LiveData
import com.zestworks.githubtrendingrepositories.dagger.AppComponentProvider
import com.zestworks.githubtrendingrepositories.database.GithubDb
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
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

    override fun fetchGitHubRepositories(responseListener: ResponseListener) {
        //update state as onFlight
        GlobalScope.launch {
            try {
                val blockingFirst = gitHubRequestMaker.fetchWeatherForecast().blockingFirst()
                if (blockingFirst.isSuccessful) {
                    //update network state as success
                    val body = blockingFirst.body()
                    if (body != null && body.isNotEmpty()) {
                        githubDb.clearAllTables()
                        githubDb.githubDao.addGithub(body)
                        responseListener.onSuccess()
                    } else {
                        responseListener.onFailure()
                    }
                } else {
                    //update network state as error
                    responseListener.onFailure()
                }
            }catch (e: Exception) {
                responseListener.onFailure()
            }
        }
    }

    override fun getGitHubs(): LiveData<List<GitHubApiResponse>> {
        return githubDb.githubDao.getGitHubApis()
    }
}
