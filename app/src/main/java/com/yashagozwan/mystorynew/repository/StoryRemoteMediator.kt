package com.yashagozwan.mystorynew.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.database.StoryDatabase
import com.yashagozwan.mystorynew.model.Story

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiConfig: ApiConfig,
    private val token: String
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        return try {
            val response = apiConfig.dicoding(token).stories(page, state.config.pageSize)
            val endOfPaginationReached = response.listStory.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAllStory()
                }

                database.storyDao().insertAllStory(response.listStory)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }
}