package com.example.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreference private constructor(private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>) {

    fun getUserToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private var instance: UserPreference? = null

        fun getInstance(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): UserPreference {
            return instance ?: synchronized(this) {
                instance ?: UserPreference(dataStore).also { instance = it }
            }
        }
    }
}
