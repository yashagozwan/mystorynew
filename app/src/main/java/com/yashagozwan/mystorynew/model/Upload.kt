package com.yashagozwan.mystorynew.model

import okhttp3.MultipartBody

data class Upload(
    val file: MultipartBody.Part,
    val description: String,
    val lat: Double? = null,
    val lon: Double? = null,
)