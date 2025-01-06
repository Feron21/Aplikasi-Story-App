package com.example.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefs(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "TOKEN"
    }

    /**
     * Get the token from shared preferences.
     * @return The saved token or null if not present.
     */
    fun getToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, null)?.trim() // Trim whitespace
        Log.d("SharedPrefs", "Token retrieved: ${token ?: "null"}") // Debug log to see token
        return token
    }

    /**
     * Save the token to shared preferences.
     * @param token The token to save.
     */
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token.trim()).apply() // Save trimmed token
        Log.d("SharedPrefs", "Token saved: ${token.trim()}") // Debug log to confirm token saved
    }

    /**
     * Clear the token from shared preferences.
     */
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        Log.d("SharedPrefs", "Token cleared") // Debug log for clearing token
    }

    /**
     * Check if the user is logged in by verifying if a token exists.
     * @return True if a token is present, false otherwise.
     */
    fun isLoggedIn(): Boolean {
        val isLoggedIn = getToken() != null
        Log.d("SharedPrefs", "Is user logged in? $isLoggedIn") // Debug log to check login status
        return isLoggedIn
    }
}
