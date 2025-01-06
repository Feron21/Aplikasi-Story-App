
package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.ui.story.StoryViewModel
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyViewModel: StoryViewModel

    private val dummyToken = "dummy_token"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        storyViewModel = StoryViewModel(storyRepository)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runBlockingTest {
        val dummyStories = DataDummy.generateDummyStoryPagingData()
        val expectedStories = flowOf(dummyStories)

        `when`(storyRepository.getAllStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = storyViewModel.getAllStories(dummyToken).asLiveData().getOrAwaitValue()
        verify(storyRepository).getAllStories(dummyToken)
        assertNotNull(actualStories)
        assertEquals(dummyStories, actualStories)
    }

    @Test
    fun `when No Stories Should Return Empty Data`() = runBlockingTest {
        val expectedStories = flowOf(PagingData.empty<StoryItem>())

        `when`(storyRepository.getAllStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = storyViewModel.getAllStories(dummyToken).asLiveData().getOrAwaitValue()
        verify(storyRepository).getAllStories(dummyToken)
        assertNotNull(actualStories)
        assertTrue(actualStories == PagingData.empty<StoryItem>())
    }
}
