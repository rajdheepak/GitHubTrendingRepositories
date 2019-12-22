package com.zestworks.githubtrendingrepositories.dagger

import android.app.Application
import androidx.room.Room
import com.zestworks.githubtrendingrepositories.database.GithubDb
import com.zestworks.githubtrendingrepositories.repository.GitHubRequestMaker
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGithubDb(application: Application): GithubDb {
       return Room.databaseBuilder(application,GithubDb::class.java,"todo").build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("https://github-trending-api.now.sh")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGithubRequestMaker(retrofit: Retrofit): GitHubRequestMaker {
        return retrofit.create(GitHubRequestMaker::class.java)
    }

}