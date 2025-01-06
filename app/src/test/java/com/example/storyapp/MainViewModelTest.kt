package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.ui.viewModel.MainViewModel
import com.example.storyapp.utils.DataDummy
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var storyRepository: StoryRepository
    @Before
    fun setUp() {
        MockKAnnotations.init(this) // Inisialisasi MockK
    }

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        // Generate dummy data
        val dummyStories = DataDummy.generateDummyStoryPagingData()
        val expectedStories = flow { emit(dummyStories) }

        // Mock the repository method
        val storyRepository = mockk<StoryRepository>()
        every { storyRepository.getAllStories("dummy_token") } returns expectedStories

        // Initialize ViewModel with the mocked repository
        val mainViewModel = MainViewModel(storyRepository)
        val actualStories = mainViewModel.getAllStories("dummy_token").first()

        // Use AsyncPagingDataDiffer to test PagingData
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        // Ensure data is loaded
        advanceUntilIdle() // Ensure all coroutines have completed

        // Assertions
        assertNotNull(differ.snapshot())
        assertEquals(10, differ.snapshot().size) // Jumlah data dummy
        assertEquals("story-0", differ.snapshot()[0]?.id) // Validasi ID pertama
    }



    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedStories = flow { emit(data) }

        every { storyRepository.getAllStories("dummy_token") } returns flowOf(data)


        val mainViewModel = MainViewModel(storyRepository)
        val actualStories = mainViewModel.getAllStories("dummy_token").first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        assertEquals(0, differ.snapshot().size)
    }



    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}