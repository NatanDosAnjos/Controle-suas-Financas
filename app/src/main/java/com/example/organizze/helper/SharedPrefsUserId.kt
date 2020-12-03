package com.example.organizze.helper

import android.content.Context

class SharedPrefsUserId (private val context: Context ) {

    private val sharedPrefsName = "Shared_Prefs_UserId"
    private val key = "user_Auth_ID"
    private val prefs = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE)

    fun saveData(userId: String) {
        prefs.edit().clear().apply()
        prefs.edit().putString(key, userId).commit()
    }

    fun getUserId(): String? {
        return prefs.getString(key, null)
    }
}