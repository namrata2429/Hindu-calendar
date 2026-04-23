package com.hindupanchang.calendar.utils

import android.content.Context
import android.content.SharedPreferences

enum class AppLanguage(val code: String, val displayName: String, val localName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी"),
    ODIA("or", "Odia", "ଓଡ଼ିଆ")
}

object LanguageManager {
    private const val PREFS_NAME = "hindu_calendar_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun saveLanguage(context: Context, language: AppLanguage) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }

    fun getLanguage(context: Context): AppLanguage {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return when (prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code)) {
            AppLanguage.HINDI.code -> AppLanguage.HINDI
            AppLanguage.ODIA.code -> AppLanguage.ODIA
            else -> AppLanguage.ENGLISH
        }
    }

    fun isFirstLaunch(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return !prefs.contains(KEY_LANGUAGE)
    }

    fun setNotFirstLaunch(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("launched", true).apply()
    }
}
