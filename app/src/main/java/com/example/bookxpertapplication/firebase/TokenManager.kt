package com.example.bookxpertapplication.firebase


import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "fcm_prefs"
    private const val KEY_TOKEN = "device_fcm_token"

    fun saveToken(context: Context, token: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }
}
