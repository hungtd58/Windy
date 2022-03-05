package com.tdh.windydemo.api

import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.model.Location

interface ApiRepository {
    suspend fun getForecastWeatherOfLocation(lat: Float, lng: Float): BaseResponseDTO<ForecastWeatherDataModel>
    suspend fun getLocationList(): List<Location>
    suspend fun getLocationFavoriteList(): List<Location>
}