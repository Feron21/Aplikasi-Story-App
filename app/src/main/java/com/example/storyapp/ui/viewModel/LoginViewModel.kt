package com.example.storyapp.ui.viewModel


import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.LoginResponse
import com.example.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            try {
                storyRepository.login(email, password)
            } catch (e: Exception) {
                throw Exception("Login error: ${e.message}")
            }
        }
    }
}
