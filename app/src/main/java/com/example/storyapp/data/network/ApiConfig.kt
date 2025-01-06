package com.example.storyapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(token: String? = null): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            // Add Authorization header only if token is provided
            addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder().apply {
                    // Add Authorization header if token is not null or empty
                    if (!token.isNullOrEmpty()) {
                        addHeader("Authorization", "Bearer $token")
                        println("Authorization Header: Bearer $token")
                    } else {
                        println("Authorization Header not added. Token is null or empty.")
                    }
                }
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
