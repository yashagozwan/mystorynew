package com.yashagozwan.mystorynew.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.model.Story

class StoryPagingSource(
    private val apiConfig: ApiConfig,
    private val token: String
) :
    PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiConfig.dicoding(token).stories(position, params.loadSize)
            val listStory = response.listStory
            LoadResult.Page(
                data = listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position.minus(1),
                nextKey = if (listStory.isNullOrEmpty()) null else position.plus(1),
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}