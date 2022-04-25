package com.yashagozwan.mystorynew.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashagozwan.mystorynew.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() = storyRepository.getToken()

    fun deleteToken() = viewModelScope.launch {
        storyRepository.deleteToken()
    }

    fun getStories(token: String) = storyRepository.getStories(token)
}