package com.zestworks.githubtrendingrepositories.dagger

import android.app.Application
import com.zestworks.githubtrendingrepositories.GithubApplication
import com.zestworks.githubtrendingrepositories.repository.GitHubRepositoryImpl
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class])
@Singleton
interface AppComponent {

    fun inject(githubApplication: GithubApplication)

    fun inject(githubApplication: GitHubRepositoryImpl)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}