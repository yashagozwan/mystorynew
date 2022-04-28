package com.yashagozwan.mystorynew.di

import android.content.Context
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.database.StoryDatabase
import com.yashagozwan.mystorynew.datastore.UserPreference
import com.yashagozwan.mystorynew.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val storyDatabase = StoryDatabase.getInstance(context)
        val userPreference = UserPreference.getInstance(context)
        val apiConfig = ApiConfig
        return StoryRepository.getInstance(userPreference, apiConfig, storyDatabase)
    }
}