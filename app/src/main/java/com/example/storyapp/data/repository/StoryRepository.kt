package com.example.storyapp.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.data.model.*
import com.example.storyapp.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

open class StoryRepository private constructor(
    private val apiService: ApiService
) {

    companion object {
        private const val ITEMS_PER_PAGE = 10

        @Volatile
        private var instance: StoryRepository? = null

        /**
         * Singleton instance of StoryRepository.
         * @param apiService ApiService instance for network requests.
         * @return StoryRepository instance.
         */
        fun getInstance(apiService: ApiService): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService).also { instance = it }
            }
        }
    }

    /**
     * Fetch stories with pagination.
     * @param token Authorization token for API requests.
     * @return Flow of PagingData containing StoryItem objects.
     */
    fun getStoriesPaging(token: String): Flow<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = ITEMS_PER_PAGE
            ),
            pagingSourceFactory = { StoryPagingSource(apiService, token) }
        ).flow
    }

    // Fungsi untuk register pengguna
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            apiService.register(name, email, password)
        } catch (e: IOException) {
            throw IOException("Network error: ${e.message ?: "Unknown error"}")
        } catch (e: HttpException) {
            throw Exception("HTTP error: ${e.message ?: "Unknown error"}")
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message ?: "Unknown error"}")
        }
    }

    // Fungsi untuk login pengguna
    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            apiService.login(email, password)
        } catch (e: IOException) {
            throw IOException("Network error: ${e.message ?: "Unknown error"}")
        } catch (e: HttpException) {
            throw Exception("HTTP error: ${e.message ?: "Unknown error"}")
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message ?: "Unknown error"}")
        }
    }

    // Fungsi untuk mengambil cerita dengan lokasi
    suspend fun getStoriesWithLocation(authToken: String): List<StoryItem> {
        try {
            Log.d("StoryRepository", "Authenticating with token: $authToken")

            val response = apiService.getStoriesWithLocation(authToken)

            if (response.isSuccessful && response.body() != null) {
                return response.body()!!.listStory
            } else {
                throw Exception("HTTP error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("StoryRepository", "Error fetching stories with location", e)
            throw e
        }
    }
    fun getAllStories(token: String): Flow<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = ITEMS_PER_PAGE
            ),
            pagingSourceFactory = { StoryPagingSource(apiService, token, false) }
        ).flow
    }
}
