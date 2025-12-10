package com.example.youtubeapp.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "youtube_app_prefs"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_VIDEO_QUALITY = "video_quality"
        private const val KEY_AUTOPLAY = "autoplay"
        private const val KEY_LAST_SYNC = "last_sync"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    var isDarkMode: Boolean
        get() = sharedPreferences.getBoolean(KEY_THEME_MODE, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_THEME_MODE, value).apply()

    var videoQuality: String
        get() = sharedPreferences.getString(KEY_VIDEO_QUALITY, "720p") ?: "720p"
        set(value) = sharedPreferences.edit().putString(KEY_VIDEO_QUALITY, value).apply()

    var isAutoplayEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_AUTOPLAY, true)
        set(value) = sharedPreferences.edit().putBoolean(KEY_AUTOPLAY, value).apply()

    var lastSyncTime: Long
        get() = sharedPreferences.getLong(KEY_LAST_SYNC, 0L)
        set(value) = sharedPreferences.edit().putLong(KEY_LAST_SYNC, value).apply()

    var username: String
        get() = sharedPreferences.getString(KEY_USERNAME, "") ?: ""
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}