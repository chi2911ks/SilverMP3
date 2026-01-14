package com.cbtool.silvermp3

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.AndroidViewModel
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.cbtool.silvermp3.service.PlayBackService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller = _controller.asStateFlow()

    fun createController(){
        if (_controller.value != null) return
        val context = getApplication<Application>()
        val sessionToken =
            SessionToken(context, ComponentName(context, PlayBackService::class.java))
        val controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            _controller.value = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }
    override fun onCleared() {
        super.onCleared()
        _controller.value?.release()
        _controller.value = null
    }
}