package com.example.storyapp.data.network


import com.example.storyapp.data.model.AddStoryResponse
import com.example.storyapp.data.model.LoginResponse
import com.example.storyapp.data.model.RegisterResponse
import com.example.storyapp.data.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int? = 10,
        @Query("location") location: Int = 0
    ): Response<StoryResponse> // Use Call here for enqueue to work

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") authToken: String,
        @Query("location") location : Int = 1,
    ): Response<StoryResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int? = 10
    ): Response<StoryResponse>





}
