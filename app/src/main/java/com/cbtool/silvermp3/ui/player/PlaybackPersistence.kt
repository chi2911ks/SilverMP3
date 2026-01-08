package com.cbtool.silvermp3.ui.player

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

// Class này giúp lưu và lấy lại trạng thái phát nhạc (Playlist nào, bài số mấy)
class PlaybackPersistence(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "silver_mp3_playback_state"
        private const val KEY_LAST_SOURCE = "last_played_source" // Ví dụ: "favourite", "playlist_abc"
        private const val KEY_LAST_INDEX = "last_played_index"   // Ví dụ: 0, 1, 2...
    }

    // Hàm lưu trạng thái: Nguồn phát (source) và Index bài hát
    fun saveState(sourceKey: String, index: Int) {
        prefs.edit().apply {
            putString(KEY_LAST_SOURCE, sourceKey)
            putInt(KEY_LAST_INDEX, index)
            apply() // Lưu bất đồng bộ
        }
    }

    // Lấy nguồn phát cuối cùng (trả về null nếu chưa có)
    fun getLastSource(): String? {
        return prefs.getString(KEY_LAST_SOURCE, null)
    }

    // Lấy index bài hát cuối cùng
    fun getLastIndex(): Int {
        return prefs.getInt(KEY_LAST_INDEX, 0)
    }
    fun setLastIndex(index: Int) {
        prefs.edit { putInt(KEY_LAST_INDEX, index) }
    }
    // Xóa trạng thái (dùng khi logout hoặc muốn reset)
    fun clearState() {
        prefs.edit { clear() }
    }
    fun clearLastSource() {
        prefs.edit { remove(KEY_LAST_SOURCE) }
    }
}
