package com.zestworks.githubtrendingrepositories

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.zestworks.githubtrendingrepositories.viewmodel.GitHubViewModel
import com.zestworks.githubtrendingrepositories.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var gitHubViewModel: GitHubViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello_world.setOnClickListener {
            gitHubViewModel.fetchGithubRepositories()
        }
    }

    override fun onStart() {
        super.onStart()
        gitHubViewModel = ViewModelFactory.getGithubViewModel(this)
        compositeDisposable = CompositeDisposable()
        gitHubViewModel.githubState.observeOn(AndroidSchedulers.mainThread()).subscribe {
            Toast.makeText(this,""+"heHa",Toast.LENGTH_SHORT).show()
        }
        gitHubViewModel.getGitHubs().observe(this, Observer {
            Toast.makeText(this,""+it.size,Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable?.dispose()
    }
}
