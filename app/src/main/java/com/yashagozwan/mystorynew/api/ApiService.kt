package com.yashagozwan.mystorynew.api

import com.yashagozwan.mystorynew.model.Login
import com.yashagozwan.mystorynew.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/v1/login")
    fun login(@Body login: Login): LoginResponse
}