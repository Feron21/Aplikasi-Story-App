package com.example.storyapp.ui.viewModel


import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.RegisterResponse
import com.example.storyapp.data.repository.StoryRepository


class RegisterViewModel(private val authRepository: StoryRepository) : ViewModel() {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        // Call the repository's register function
        return authRepository.register(name, email, password)
    }
}
