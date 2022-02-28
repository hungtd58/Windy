package com.tdh.windydemo.api

import com.tdh.windydemo.model.ForecastWeatherDataModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getForecastWeatherOfLocation(
        @Query("lat") lat: Float,
        @Query("lon") lng: Float
    ): ForecastWeatherDataModel
}