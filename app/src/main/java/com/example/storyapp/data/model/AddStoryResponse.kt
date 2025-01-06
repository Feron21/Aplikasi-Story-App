package com.example.storyapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
data class AddStoryResponse(
    val error: Boolean,
    val message: String,
    val story: StoryResponse? // Pastikan model Story sudah dibuat
)


