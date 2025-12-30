package com.cbtool.silvermp3

import android.app.Application
import android.content.ComponentName
import android.util.Log
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.cbtool.silvermp3.di.appModule
import com.cbtool.silvermp3.service.PlayBackService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SilverApplication : Application() {
    private var _mediaController: MediaController? = null
    val mediaController get() = _mediaController
    private suspend fun initMediaController() {
        val sessionToken = SessionToken(this, ComponentName(this, PlayBackService::class.java))
        val future = MediaController.Builder(this, sessionToken).buildAsync()
        _mediaController = future.await() // 👈 Đợi async hoàn tất
        Log.d("SilverApp", "MediaController đã sẵn sàng: $_mediaController")
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            initMediaController()
        }
        startKoin {
            androidContext(this@SilverApplication)
            modules(appModule)
        }
    }
}