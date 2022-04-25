package com.yashagozwan.mystorynew.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun saveToken(token: String) = viewModelScope.launch {
        storyRepository.saveToken(token)
    }

    fun getToken() = storyRepository.getToken()

    fun login(login: Login) = storyRepository.login(login)
}