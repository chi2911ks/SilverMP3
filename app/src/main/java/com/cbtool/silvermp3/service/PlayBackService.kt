package com.cbtool.silvermp3.service


import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource


import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.cbtool.silvermp3.data.local.MediaCacheManager
import com.cbtool.silvermp3.ui.player.PlaybackPersistence


class PlayBackService: MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private lateinit var playerListener: Player.Listener

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        // Tạo DataSource để tải dữ liệu từ Internet
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)

        // Tạo CacheDataSource Factory để điều phối giữa Cache và Internet
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(MediaCacheManager.getCache(this))
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        // Gắn Factory này vào Player
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(this)
                    .setDataSourceFactory(cacheDataSourceFactory)
            )
            .build()
//        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                // Nếu player kết thúc tự nhiên (STATE_ENDED)
                // Hoặc player đang IDLE (sau khi stop() được gọi) VÀ không sẵn sàng phát
                if (playbackState == Player.STATE_ENDED || (playbackState == Player.STATE_IDLE && !player.playWhenReady)) {
                    stopSelf() // Tự dừng service
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                // Nếu player không còn phát nữa và không sẵn sàng phát
                // Đây cũng là một tín hiệu tốt để dừng service, ví dụ khi tạm dừng lâu
                if (!isPlaying && player.playbackState == Player.STATE_IDLE && !player.playWhenReady) {
                    // Nếu bạn muốn dừng service ngay cả khi tạm dừng, hãy cân nhắc thêm delay
                    // để tránh dừng service quá nhanh nếu người dùng chỉ tạm dừng trong thời gian ngắn.
                    // Ví dụ: Handler().postDelayed({ if (!player.isPlaying) stopSelf() }, 5000)
                }
            }
        }
        player.addListener(playerListener)


    }




    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
    override fun onTaskRemoved(rootIntent: Intent?) {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }


    override fun onDestroy() {
        player.removeListener(playerListener)
        player.release()
        mediaSession.release()
        super.onDestroy()
    }
    companion object {
        const val CHANNEL_ID = "silver_mp3"
    }
}
