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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SilverApplication : Application() {
    // 1. Tạo StateFlow để nắm giữ trạng thái Controller
    private val _controllerFlow = MutableStateFlow<MediaController?>(null)
    val controllerFlow = _controllerFlow.asStateFlow()

    // Giữ biến cũ nếu bạn vẫn muốn truy cập trực tiếp (optional)
    val mediaController get() = _controllerFlow.value

    private suspend fun initMediaController() {
        val sessionToken = SessionToken(this, ComponentName(this, PlayBackService::class.java))
        val future = MediaController.Builder(this, sessionToken).buildAsync()
        val controller = future.await()

        // 2. Cập nhật giá trị vào Flow
        _controllerFlow.value = controller
        Log.d("SilverApp", "MediaController đã sẵn sàng")
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Main).launch { // Nên dùng Main để update Flow UI
            initMediaController()
        }
        startKoin {
            androidContext(this@SilverApplication)
            modules(appModule)
        }
    }
}