package com.yashagozwan.mystorynew.ui.start

import androidx.lifecycle.ViewModel
import com.yashagozwan.mystorynew.repository.StoryRepository

class StartViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() = storyRepository.getToken()
}