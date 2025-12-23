package com.example.finalproject.core.common

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

object ThemePreferences {

    private const val PREFS_NAME = "android_final_project_prefs"
    private const val KEY_DARK_MODE = "dark_mode_enabled"

    fun applySavedTheme(context: Context) {
        val prefs = prefs(context)
        val mode = if (prefs.contains(KEY_DARK_MODE)) {
            if (prefs.getBoolean(KEY_DARK_MODE, false)) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        } else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        val prefs = prefs(context)
        return if (prefs.contains(KEY_DARK_MODE)) {
            prefs.getBoolean(KEY_DARK_MODE, false)
        } else {
            isSystemInDarkMode(context)
        }
    }

    fun updateDarkMode(context: Context, enabled: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun isSystemInDarkMode(context: Context): Boolean {
        val nightModeFlags =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}
