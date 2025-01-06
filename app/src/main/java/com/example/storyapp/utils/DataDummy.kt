
package com.example.storyapp.utils

import androidx.paging.PagingData
import com.example.storyapp.data.model.StoryItem

object DataDummy {
    fun generateDummyStoryPagingData(): PagingData<StoryItem> {
        val stories = List(10) {
            StoryItem(
                id = "story-$it",
                name = "Story $it",
                description = "Description $it",
                photoUrl = "https://dummyimage.com/600x400/000/fff&text=Story+$it",
                createdAt = "2023-01-01T00:00:00Z"
            )
        }
        return PagingData.from(stories)
    }
}
