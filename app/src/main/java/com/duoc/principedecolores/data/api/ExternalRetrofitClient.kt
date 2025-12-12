package com.duoc.principedecolores.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface HoroscopeApiService {
    @GET("/api/v1/get-horoscope/daily")
    suspend fun getHoroscopo(
        @Query("sign") sign: String,
        @Query("day") day: String = "today"
    ): HoroscopoResponse
}

object ExternalRetrofitClient {
    private const val BASE_URL = "https://horoscope-app-api.vercel.app/"

    val apiService: HoroscopeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HoroscopeApiService::class.java)
    }
}