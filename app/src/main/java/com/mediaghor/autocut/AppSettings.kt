package com.mediaghor.autocut

import android.content.Context

object AppSettings {
    const val MIN_INTERVAL_SECONDS = 1
    const val MAX_INTERVAL_SECONDS = 3600
    const val DEFAULT_INTERVAL_SECONDS = 10

    private const val FILE = "app_settings"
    private const val KEY_INTERVAL_SECONDS = "cut_interval_seconds"
    private const val KEY_VIDEO_FPS = "video_frame_rate"

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun cutIntervalSeconds(context: Context): Int =
        prefs(context).getInt(KEY_INTERVAL_SECONDS, DEFAULT_INTERVAL_SECONDS)
            .coerceIn(MIN_INTERVAL_SECONDS, MAX_INTERVAL_SECONDS)

    fun setCutIntervalSeconds(context: Context, value: Int) {
        prefs(context).edit()
            .putInt(KEY_INTERVAL_SECONDS, value.coerceIn(MIN_INTERVAL_SECONDS, MAX_INTERVAL_SECONDS))
            .apply()
    }

    fun lastVideoFps(context: Context): Int = prefs(context).getInt(KEY_VIDEO_FPS, 0)

    fun setLastVideoFps(context: Context, fps: Int) {
        prefs(context).edit().putInt(KEY_VIDEO_FPS, fps).apply()
    }
}
