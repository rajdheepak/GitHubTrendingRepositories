package com.zestworks.githubtrendingrepositories.repository

import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface GitHubRequestMaker {
    @GET("/repositories")
    fun fetchWeatherForecast(): Observable<Response<List<GitHubApiResponse>>>
}