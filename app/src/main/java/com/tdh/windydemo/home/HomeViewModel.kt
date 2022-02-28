package com.tdh.windydemo.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tdh.libase.base.BaseViewModel
import com.tdh.windydemo.api.ApiRepositoryImpl
import com.tdh.windydemo.api.ServiceBuilder
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.model.Location
import kotlinx.coroutines.*

class HomeViewModel : BaseViewModel() {

    private val apiImpl = ApiRepositoryImpl(ServiceBuilder().getApiService())
    private val _forecastWeatherDataModel = MutableLiveData<ForecastWeatherDataModel>()
    val forecastWeatherDataModel: LiveData<ForecastWeatherDataModel> get() = _forecastWeatherDataModel
    private val _favoriteLocationList =
        MutableLiveData<MutableList<ForecastWeatherDataModel>>(mutableListOf())
    val favoriteLocationList: LiveData<MutableList<ForecastWeatherDataModel>> get() = _favoriteLocationList

    fun getForecastWeatherOfLocation(lat: Float, lng: Float) {
        viewModelScope.launch {
            showProgress()
            withContext(Dispatchers.IO) {
                _forecastWeatherDataModel.postValue(loadForecastWeatherOfLocation(lat, lng))
            }
            hideProgress()
        }
    }

    private suspend fun loadForecastWeatherOfLocation(
        lat: Float,
        lng: Float
    ): ForecastWeatherDataModel {
        return apiImpl.getForecastWeatherOfLocation(lat, lng)
    }

    fun getForecastWeatherOfFavoriteLocations() {
        viewModelScope.launch {
            showProgress()
            _favoriteLocationList.value?.clear()
            val locations = mutableListOf<Location>()
            withContext(Dispatchers.Default) {
                locations.addAll(apiImpl.getLocationFavoriteList())
            }
            withContext(Dispatchers.IO) {
                if (locations.isEmpty()) {
                    _favoriteLocationList.postValue(mutableListOf())
                    return@withContext
                }
                val forecastWeatherDataModels = mutableListOf<Deferred<ForecastWeatherDataModel>>()
                for (location in locations) {
                    val forecastWeatherDataModel = async {
                        loadForecastWeatherOfLocation(
                            location.coord.lat.toFloat(),
                            location.coord.lon.toFloat()
                        )
                    }
                    forecastWeatherDataModels.add(forecastWeatherDataModel)
                }
                _favoriteLocationList.postValue(
                    forecastWeatherDataModels.awaitAll().toMutableList()
                )
            }
            hideProgress()
        }
    }
}