package com.yashagozwan.mystorynew.api

import com.yashagozwan.mystorynew.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/login")
    suspend fun login(@Body login: Login): LoginResponse

    @POST("/v1/register")
    suspend fun register(@Body register: Register): RegisterResponse

    @GET("/v1/stories")
    suspend fun stories(): StoryResponse

}