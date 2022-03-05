package com.tdh.windydemo.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tdh.libase.base.BaseViewModel
import com.tdh.libase.base.model.MessageDialogState
import com.tdh.windydemo.api.ApiRepositoryImpl
import com.tdh.windydemo.api.BaseResponseDTO
import com.tdh.windydemo.api.ServiceBuilder
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.model.Location
import com.tdh.windydemo.utils.AppSharedPref
import kotlinx.coroutines.*

class HomeViewModel : BaseViewModel() {

    private val apiImpl = ApiRepositoryImpl(ServiceBuilder().getApiService())

    private val _forecastWeatherDataModel = MutableLiveData<ForecastWeatherDataModel?>()
    val forecastWeatherDataModel: LiveData<ForecastWeatherDataModel?> get() = _forecastWeatherDataModel

    private val _favoriteLocationList =
        MutableLiveData<MutableList<ForecastWeatherDataModel>>(mutableListOf())
    val favoriteLocationList: LiveData<MutableList<ForecastWeatherDataModel>> get() = _favoriteLocationList

    fun getForecastWeatherOfLocation(lat: Float, lng: Float) {
        viewModelScope.launch {
            showProgress()
            withContext(Dispatchers.IO) {
                val response = loadForecastWeatherOfLocation(lat, lng)
                if (response is BaseResponseDTO.Success) {
                    _forecastWeatherDataModel.postValue(response.successData)
                } else {
                    messageDialog.postValue(MessageDialogState(message = (response as BaseResponseDTO.Error).exception?.message))
                    _forecastWeatherDataModel.postValue(null)
                }
            }
            hideProgress()
        }
    }

    private suspend fun loadForecastWeatherOfLocation(
        lat: Float,
        lng: Float
    ): BaseResponseDTO<ForecastWeatherDataModel> {
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
                val forecastWeatherDataModels =
                    mutableListOf<Deferred<BaseResponseDTO<ForecastWeatherDataModel>>>()
                for (location in locations) {
                    val forecastWeatherDataModel = async {
                        loadForecastWeatherOfLocation(
                            location.coord.lat.toFloat(),
                            location.coord.lon.toFloat()
                        ).apply {
                            if (this is BaseResponseDTO.Success) {
                                this.successData?.location = location
                            }
                        }
                    }
                    forecastWeatherDataModels.add(forecastWeatherDataModel)
                }

                val responseList = forecastWeatherDataModels.awaitAll().toMutableList()
                    .filterIsInstance<BaseResponseDTO.Success<ForecastWeatherDataModel>>()
                    .map { it.successData!! }.toMutableList()

                _favoriteLocationList.postValue(responseList)
            }
            hideProgress()
        }
    }

    fun removeLocation(forecastWeatherDataModel: ForecastWeatherDataModel) {
        viewModelScope.launch(Dispatchers.Default) {
            forecastWeatherDataModel.location?.let {
                AppSharedPref.removeLocationFavorite(it)
            }
            getForecastWeatherOfFavoriteLocations()
        }
    }
}