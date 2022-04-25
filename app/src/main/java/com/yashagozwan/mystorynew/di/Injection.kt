package com.yashagozwan.mystorynew.di

import android.content.Context
import com.yashagozwan.mystorynew.datastore.UserPreference
import com.yashagozwan.mystorynew.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val userPreference = UserPreference.getInstance(context)
        return StoryRepository.getInstance(userPreference)
    }
}