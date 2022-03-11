package com.tdh.windydemo.screen.detail

import androidx.lifecycle.MutableLiveData
import com.tdh.libase.base.BaseViewModel
import com.tdh.windydemo.model.ForecastWeatherDataModel

class DetailLocationViewModel : BaseViewModel() {
    val forecastWeatherDataModel = MutableLiveData<ForecastWeatherDataModel>(null)
}