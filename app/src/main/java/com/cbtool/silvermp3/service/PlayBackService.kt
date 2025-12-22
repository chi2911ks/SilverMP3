package com.cbtool.silvermp3.service


import android.content.Intent
import androidx.media3.common.Player

import androidx.media3.exoplayer.ExoPlayer

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class PlayBackService: MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()

        player.addListener(object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {

            }
        })

    }




    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession
    override fun onTaskRemoved(rootIntent: Intent?) {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }


    override fun onDestroy() {
        mediaSession.run {
            player.release()
            release()
            mediaSession.release()
        }
        super.onDestroy()
    }
    companion object {
        const val CHANNEL_ID = "silver_mp3"
    }
}
