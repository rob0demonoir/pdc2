package com.duoc.principedecolores.data.api

import com.google.gson.annotations.SerializedName

data class HoroscopoResponse(
    val data: HoroscopoData,
    val status: Int,
    val success: Boolean
)

data class HoroscopoData(
    val date: String,
    @SerializedName("horoscope_data")
    val prediction: String
)