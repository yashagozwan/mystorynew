package com.yashagozwan.mystorynew.ui.register

import androidx.lifecycle.ViewModel
import com.yashagozwan.mystorynew.model.Register
import com.yashagozwan.mystorynew.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun register(register: Register) = storyRepository.register(register)
}