package com.yashagozwan.mystorynew.ui.map

import androidx.lifecycle.ViewModel
import com.yashagozwan.mystorynew.repository.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() = storyRepository.getToken()
    fun storiesAndLocation(token: String) = storyRepository.storyAndLocation(token)
}