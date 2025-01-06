package com.example.storyapp.ui.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.repository.StoryRepository

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val mapsActivity: String? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }
            // Tambahkan ViewModel lainnya di sini jika diperlukan
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application, mapsActivity: String? = null, token: String? = null): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    StoryRepository.getInstance(ApiConfig.getApiService(token)),
                    mapsActivity
                ).also { instance = it }
            }
        }
    }
}
