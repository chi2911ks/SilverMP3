package com.cbtool.silvermp3.data.local

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object MediaCacheManager {
    private var cache: SimpleCache? = null

    // Tạo phương thức khởi tạo an toàn
    fun getCache(context: Context): SimpleCache {
        val cacheDir = File(context.cacheDir, "media_cache")

        synchronized(this) {
            if (cache == null) {
                val databaseProvider = StandaloneDatabaseProvider(context)
                cache = SimpleCache(
                    cacheDir,
                    LeastRecentlyUsedCacheEvictor(200 * 1024 * 1024), // 200MB
                    databaseProvider
                )
            }
        }
        return cache!!
    }
}