package com.yashagozwan.mystorynew.api

import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.model.LoginResponse
import com.yashagozwan.mystorynew.model.Register
import com.yashagozwan.mystorynew.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/login")
    suspend fun login(@Body login: Login): LoginResponse

    @POST("/v1/register")
    suspend fun register(@Body register: Register): RegisterResponse
}