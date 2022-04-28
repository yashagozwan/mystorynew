package com.yashagozwan.mystorynew.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.yashagozwan.mystorynew.api.ApiConfig
import com.yashagozwan.mystorynew.database.StoryDatabase
import com.yashagozwan.mystorynew.datastore.UserPreference
import com.yashagozwan.mystorynew.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiConfig: ApiConfig,
    private val storyDatabase: StoryDatabase
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

    fun getStories(token: String): LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiConfig,
                token
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
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

    fun storyAndLocation(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.dicoding(token).storiesAndLocation()
            emit(Result.Success(response.listStory))
        } catch (error: Exception) {
            emit(Result.Error(error.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiConfig: ApiConfig,
            storyDatabase: StoryDatabase
        ) = instance ?: synchronized(this) {
            instance ?: StoryRepository(userPreference, apiConfig, storyDatabase)
        }.also { instance = it }
    }
}