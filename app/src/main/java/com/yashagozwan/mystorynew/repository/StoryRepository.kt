package com.yashagozwan.mystorynew.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.datastore.UserPreference
import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.model.LoginResponse

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiConfig: ApiConfig
) {

    suspend fun saveToken(token: String) = userPreference.save(token)
    fun getToken() = userPreference.token().asLiveData()
    suspend fun deleteToken() = userPreference.delete()

    fun login(login: Login): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.dicoding().login(login)
            emit(Result.Success(response))
        } catch (error: Exception) {
            emit(Result.Error(error.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(userPreference: UserPreference, apiConfig: ApiConfig) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiConfig)
            }.also { instance = it }
    }
}