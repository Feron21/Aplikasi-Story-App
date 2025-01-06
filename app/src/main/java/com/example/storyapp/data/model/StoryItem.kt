package com.example.storyapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("photoUrl")
    val photoUrl: String = "",  // Nilai default untuk photoUrl
    @SerializedName("createdAt")
    val createdAt: String ="",
    @SerializedName("lat")
    val lat: Double? = null,  // Nilai default null untuk lat
    @SerializedName("lon")
    val lon: Double? = null   // Nilai default null untuk lon
) : Parcelable
