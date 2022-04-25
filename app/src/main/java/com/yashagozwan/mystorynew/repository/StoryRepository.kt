package com.yashagozwan.mystorynew.repository

import com.yashagozwan.mystorynew.datastore.UserPreference

class StoryRepository private constructor(private val userPreference: UserPreference) {

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(userPreference: UserPreference) = instance ?: synchronized(this) {
            instance ?: StoryRepository(userPreference)
        }.also { instance = it }
    }
}