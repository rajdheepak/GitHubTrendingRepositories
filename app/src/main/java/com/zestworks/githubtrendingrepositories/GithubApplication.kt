package com.zestworks.githubtrendingrepositories

import android.app.Application
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.Utils
import com.zestworks.githubtrendingrepositories.dagger.AppComponentProvider
import com.zestworks.githubtrendingrepositories.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class GithubApplication: Application(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        AppComponentProvider.appComponent = appComponent
        Utils.init(this)
    }

}