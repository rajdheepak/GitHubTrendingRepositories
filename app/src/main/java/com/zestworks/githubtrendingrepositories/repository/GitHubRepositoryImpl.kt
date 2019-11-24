package com.zestworks.githubtrendingrepositories.repository

import android.util.Log
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GitHubRepositoryImpl: GitHubRepository {

    private var gitHubRequestMaker: GitHubRequestMaker

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://github-trending-api.now.sh")
            .client(OkHttp.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        gitHubRequestMaker = retrofit.create(GitHubRequestMaker::class.java)
    }

    override fun fetchGitHubRepositories(responseListener: ResponseListener) {
        //update state as onFlight
        GlobalScope.launch {
            val blockingFirst = gitHubRequestMaker.fetchWeatherForecast().blockingFirst()
            if(blockingFirst.isSuccessful) {
                //update network state as success
                responseListener.onSuccess()
            } else {
                //update network state as error
                responseListener.onFailure()
            }
        }
    }
}

object OkHttp { //single instance of okHttp client so that all running requests can be cancelled in onCleared of ViewModel
    val okHttpClient = OkHttpClient()
}
