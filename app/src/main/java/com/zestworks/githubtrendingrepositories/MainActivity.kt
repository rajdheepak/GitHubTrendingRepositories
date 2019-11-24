package com.zestworks.githubtrendingrepositories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zestworks.githubtrendingrepositories.viewmodel.GitHubViewModel
import com.zestworks.githubtrendingrepositories.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var gitHubViewModel: GitHubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello_world.setOnClickListener {
            gitHubViewModel.fetchGithubRepositories()
        }
    }

    override fun onStart() {
        super.onStart()
        gitHubViewModel = ViewModelProviders.of(this, ViewModelFactory).get(GitHubViewModel::class.java)
    }
}
