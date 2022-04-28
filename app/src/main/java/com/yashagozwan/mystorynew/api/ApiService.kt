package com.yashagozwan.mystorynew.api

import com.yashagozwan.mystorynew.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("/v1/login")
    suspend fun login(@Body login: Login): LoginResponse

    @POST("/v1/register")
    suspend fun register(@Body register: Register): RegisterResponse

    @GET("/v1/stories")
    suspend fun stories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @POST("/v1/stories")
    @Multipart
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double
    ): UploadResponse

    @GET("v1/stories?location=1")
    suspend fun storiesAndLocation(): StoryMapResponse
}