package com.tdh.windydemo.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tdh.windydemo.model.Location
import com.tdh.windydemo.utils.AppSharedPref
import com.tdh.windydemo.utils.Utils


class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun getForecastWeatherOfLocation(
        lat: Float,
        lng: Float
    ) = apiService.getForecastWeatherOfLocation(lat, lng)

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