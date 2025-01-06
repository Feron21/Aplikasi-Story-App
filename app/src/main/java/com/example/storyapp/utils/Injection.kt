package com.example.storyapp.utils


import android.content.Context
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Injection {
    // Convert provideRepository to a suspend function to avoid blocking the main thread
    suspend fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val token = preferences.getUserToken().first() // This is a Flow, so we can collect it asynchronously
        val apiService = ApiConfig.getApiService(token)
        return StoryRepository.getInstance(apiService)
    }
}
