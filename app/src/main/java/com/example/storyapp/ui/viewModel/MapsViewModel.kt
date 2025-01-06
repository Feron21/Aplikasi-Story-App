package com.example.storyapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.StoryItem
import kotlinx.coroutines.launch
import android.util.Log
import com.example.storyapp.data.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<StoryItem>>()
    val stories: LiveData<List<StoryItem>> = _stories

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Fetches stories with location data from the repository.
     * Filters the stories to include only those with valid latitude and longitude.
     */
    fun fetchStoriesWithLocation(authToken: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // Pastikan token diteruskan
                Log.d("MapsViewModel", "Fetching stories with token: $authToken")
                val storiesWithLocation = repository.getStoriesWithLocation(authToken)
                    .filter { it.lat != null && it.lon != null }
                _stories.value = storiesWithLocation
                if (storiesWithLocation.isEmpty()) {
                    _error.value = "No stories available with valid location data."
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch stories: ${e.message}"
                Log.e("MapsViewModel", "Error fetching stories", e)
            } finally {
                _loading.value = false
            }
        }
    }

}
