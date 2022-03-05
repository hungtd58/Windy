package com.tdh.windydemo.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tdh.windydemo.App
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.model.Location
import com.tdh.windydemo.utils.AppSharedPref
import com.tdh.windydemo.utils.NetworkUtils
import com.tdh.windydemo.utils.Utils


class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun getForecastWeatherOfLocation(
        lat: Float,
        lng: Float
    ): BaseResponseDTO<ForecastWeatherDataModel> {
        if (!NetworkUtils.isOnline(App.newInstance)) {
            return BaseResponseDTO.Error(Exception("You're offline. Please check your network connection!"))
        }
        val response = apiService.getForecastWeatherOfLocation(lat, lng)
        return if (response.isSuccessful) {
            BaseResponseDTO.Success(response.body())
        } else {
            BaseResponseDTO.Error(Exception("An error occurred. Please try again later!"))
        }
    }

    override suspend fun getLocationList(): List<Location> {
        val dataJson = Utils.loadJsonFromAsset("city.list.json")
        return Gson().fromJson<List<Location>?>(
            dataJson,
            object : TypeToken<List<Location>>() {}.type
        ).sortedBy { it.name }
    }

    override suspend fun getLocationFavoriteList(): List<Location> {
        return AppSharedPref.getLocationFavoriteList()
    }
}