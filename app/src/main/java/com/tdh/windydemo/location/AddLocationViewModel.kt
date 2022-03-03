package com.tdh.windydemo.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tdh.libase.base.BaseViewModel
import com.tdh.windydemo.api.ApiRepositoryImpl
import com.tdh.windydemo.api.ServiceBuilder
import com.tdh.windydemo.model.Location
import com.tdh.windydemo.utils.AppSharedPref
import isNothing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import stripAccent
import java.util.*

class AddLocationViewModel : BaseViewModel() {
    private val apiImpl = ApiRepositoryImpl(ServiceBuilder().getApiService())

    private var fullLocationList = mutableListOf<Location>()
    private var searchKeyWord = ""
    private var searchJob: Job? = null

    private val _locationList =
        MutableLiveData<MutableList<Location>>(mutableListOf())
    val locationList: LiveData<MutableList<Location>> get() = _locationList

    fun searchLocation(keyWord: String?) {
        searchKeyWord = keyWord.stripAccent().lowercase(Locale.getDefault())
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            if (fullLocationList.isEmpty()) {
                fullLocationList = apiImpl.getLocationList().toMutableList()
            }
            val locationFavorites = apiImpl.getLocationFavoriteList()
            val locationResults: List<Location>
            if (searchKeyWord.isNothing()) {
                locationResults = fullLocationList.toMutableList()
            } else {
                locationResults = fullLocationList.filter {
                    it.name.stripAccent().lowercase(Locale.getDefault()).contains(searchKeyWord)
                }
            }
            locationResults.filter { location ->
                locationFavorites.find { location.id == it.id } != null
            }.map { it.isSelected = true }

            _locationList.postValue(locationResults.toMutableList())
        }
    }

    fun toggleSelectLocation(location: Location, isSelected: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            if (isSelected) {
                AppSharedPref.addLocationFavorite(location)
            } else {
                AppSharedPref.removeLocationFavorite(location)
            }
            locationList.value?.first { it.id == location.id }?.isSelected = isSelected
            _locationList.postValue(locationList.value?.toMutableList())
        }
    }
}