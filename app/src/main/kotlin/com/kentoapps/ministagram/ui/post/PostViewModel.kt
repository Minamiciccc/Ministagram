package com.kentoapps.ministagram.ui.post

import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.kentoapps.ministagram.data.model.PostRequest
import com.kentoapps.ministagram.data.source.post.PostRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class PostViewModel @Inject constructor(private val repository: PostRepository): ViewModel()  {

    private val disposables = CompositeDisposable()

    fun savePost(uri: Uri, caption: String) {
        repository.savePost(PostRequest(uri, caption))
                .subscribeBy(
                        onComplete = {},
                        onError = {}
                ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
