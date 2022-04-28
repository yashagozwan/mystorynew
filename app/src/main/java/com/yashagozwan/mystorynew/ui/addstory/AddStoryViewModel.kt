package com.yashagozwan.mystorynew.ui.addstory

import androidx.lifecycle.ViewModel
import com.yashagozwan.mystorynew.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getToken() = storyRepository.getToken()
    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ) = storyRepository.upload(token, file, description, lat, lon)
}