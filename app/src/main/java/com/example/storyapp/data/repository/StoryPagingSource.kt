package com.example.storyapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.model.StoryItem
import com.example.storyapp.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource(
    private val apiService: ApiService,
    private val token: String,
    private val includeLocationFilter: Boolean = true
) : PagingSource<Int, StoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            // Memanggil API untuk mendapatkan cerita dengan filter lokasi
            val response = apiService.getStories(
                token = "Bearer $token",
                page = page,
                size = params.loadSize,
                location = if (includeLocationFilter) 1 else 0
            )

            // Memeriksa jika respons sukses
            if (response.isSuccessful) {
                // Mengambil data listStory dari body respons
                val data = response.body()?.listStory ?: emptyList()

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            } else {
                // Jika tidak berhasil, kembalikan error
                LoadResult.Error(Exception("Error: ${response.message()}"))
            }
        } catch (e: IOException) {
            // Penanganan kesalahan jaringan
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Penanganan kesalahan HTTP
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Penanganan kesalahan umum lainnya
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        // Menentukan halaman mana yang perlu di-refresh
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1  // Halaman pertama dimulai dari 1
    }
}