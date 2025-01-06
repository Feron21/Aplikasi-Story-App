package com.example.storyapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getAllStories(token: String): Flow<PagingData<StoryItem>> {
        return storyRepository.getAllStories(token)
            .cachedIn(viewModelScope)  // Cache data di dalam ViewModel scope
    }
}


