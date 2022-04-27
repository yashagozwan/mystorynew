package com.yashagozwan.mystorynew.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.datastore.UserPreference
import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.model.LoginResponse
import com.yashagozwan.mystorynew.model.Register
import com.yashagozwan.mystorynew.model.Upload
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun register(register: Register) = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.dicoding().register(register)
            emit(Result.Success(response))
        } catch (error: Exception) {
            emit(Result.Error(error.message.toString()))
        }
    }

    fun getStories(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.dicoding(token).stories()
            emit(Result.Success(response.listStory))
        } catch (error: Exception) {
            emit(Result.Error(error.message.toString()))
        }
    }

    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ) = liveData {
        emit(Result.Loading)
        try {
            val response = apiConfig.dicoding(token).upload(file, description, lat, lon)
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