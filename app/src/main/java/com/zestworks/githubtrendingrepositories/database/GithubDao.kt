package com.zestworks.githubtrendingrepositories.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zestworks.githubtrendingrepositories.model.GitHubApiResponse

@Dao
interface GithubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGithub(gitHubApiResponse: List<GitHubApiResponse>)

    @Query("SELECT * from githubapi")
    fun getGitHubApis(): LiveData<List<GitHubApiResponse>>

    @Query("SELECT * from githubapi ORDER BY stars desc")
    fun getGithubApisByStars(): LiveData<List<GitHubApiResponse>>

    @Query("SELECT * FROM GITHUBAPI ORDER BY name asc")
    fun getGithubApisByName(): LiveData<List<GitHubApiResponse>>
}