package com.example.flashkart.network

import com.example.flashkart.data.InternetItem
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL =
    "https://gist.githubusercontent.com/Chinmay0608/7305386ea8541f2ec857f8429ba41a3f/raw/"

private val json = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    )
    .build()

interface FlashApiService {
    @GET("bd99b6abcdcd485d6d98ceab1dfd89970b1b5404/flash_items.json")
    suspend fun getItems(): List<InternetItem>
}

object FlashApi {
    val retrofitService: FlashApiService by lazy {
        retrofit.create(FlashApiService::class.java)
    }
}
