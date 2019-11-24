package com.zestworks.githubtrendingrepositories

import com.zestworks.githubtrendingrepositories.model.*
import com.zestworks.githubtrendingrepositories.repository.GitHubRepository
import com.zestworks.githubtrendingrepositories.repository.ResponseListener
import com.zestworks.githubtrendingrepositories.viewmodel.GitHubViewModel
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GitHubStateTests {

    internal val gitHubStateTestObserver = TestObserver<GithubState>()
    @Mock
    lateinit var repository: GitHubRepository
    lateinit var weatherViewModel: GitHubViewModel

    @Captor
    private lateinit var gitHubApiResponseListener: ArgumentCaptor<ResponseListener>

    @Before
    fun setup() {
        weatherViewModel = GitHubViewModel(repository)
        weatherViewModel.githubState.subscribe(gitHubStateTestObserver)
    }

    @Test
    fun `state should be request on going if request is in flight`(){
        val last = gitHubStateTestObserver.values().last()
        (last.gitHubApiResponseState is YetToStart) shouldBe true
        weatherViewModel.fetchGithubRepositories()
        val stateAfterNetworkRequest = gitHubStateTestObserver.values().last()
        (stateAfterNetworkRequest.gitHubApiResponseState is RequestOnGoing) shouldBe true
    }

    @Test
    fun `fetch request with success response state`(){
        weatherViewModel.fetchGithubRepositories()
        verify(repository).fetchGitHubRepositories(capture(gitHubApiResponseListener))
        val stateAfterNetworkRequest = gitHubStateTestObserver.values().last()
        (stateAfterNetworkRequest.gitHubApiResponseState is RequestOnGoing) shouldBe true
        gitHubApiResponseListener.value.onSuccess()
        val stateAfterNetworkSuccess = gitHubStateTestObserver.values().last()
        (stateAfterNetworkSuccess.gitHubApiResponseState is Success) shouldBe true

    }

    @Test
    fun `fetch request with error response state`(){
        weatherViewModel.fetchGithubRepositories()
        verify(repository).fetchGitHubRepositories(capture(gitHubApiResponseListener))
        val stateAfterNetworkRequest = gitHubStateTestObserver.values().last()
        (stateAfterNetworkRequest.gitHubApiResponseState is RequestOnGoing) shouldBe true
        gitHubApiResponseListener.value.onFailure()
        val stateAfterNetworkSuccess = gitHubStateTestObserver.values().last()
        (stateAfterNetworkSuccess.gitHubApiResponseState is RequestFailed) shouldBe true

    }
}

infix fun <T> T.shouldBe(any: Any?) {
    assert(any == this)
}

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()