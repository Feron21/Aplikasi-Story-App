package com.example.storyapp.data.model

import com.google.gson.annotations.SerializedName

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val listStory: List<StoryItem>
): Parcelable

//@Parcelize
//data class StoryItem(
//    @SerializedName("id")
//    val id: String,
//    @SerializedName("name")
//    val name: String,
//    @SerializedName("description")
//    val description: String,
//    @SerializedName("photoUrl")
//    val photoUrl: String,
//    @SerializedName("lat")
//    val lat: Double?,
//    @SerializedName("lon")
//    val lon: Double?
//) : Parcelable
