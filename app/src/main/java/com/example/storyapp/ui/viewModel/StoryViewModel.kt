package com.example.storyapp.ui.story


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    // Fungsi untuk mengambil data stories dengan paging
    fun getStories(token: String): Flow<PagingData<StoryItem>> {
        return storyRepository.getStoriesPaging(token).cachedIn(viewModelScope)
    }
    fun getAllStories(token: String): Flow<PagingData<StoryItem>> {
        return storyRepository.getAllStories(token)
            .cachedIn(viewModelScope)
    }
}
