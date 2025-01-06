package com.example.storyapp.data.repository


import com.example.storyapp.data.model.LoginResponse
import com.example.storyapp.data.model.RegisterResponse
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.network.ApiConfig

class AuthRepository {

    private val apiService = ApiConfig.getApiService()

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        // Make the API call for registration
        return apiService.register(name, email, password)
    }
}
