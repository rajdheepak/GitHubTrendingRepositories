package com.zestworks.githubtrendingrepositories.dagger

object AppComponentProvider {
    lateinit var appComponent: AppComponent

    fun appComponent(): AppComponent {
        return appComponent
    }
}