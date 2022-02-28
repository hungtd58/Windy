package com.tdh.windydemo.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tdh.windydemo.App
import com.tdh.windydemo.model.Location

class AppSharedPref {
    companion object {
        const val KEY_LOCATIONS_SELECTED = "KEY_CITIES_SELECTED"

        private fun getSharePref(): SharedPreferences {
            return App.newInstance.applicationContext
                .getSharedPreferences(
                    KEY_LOCATIONS_SELECTED,
                    Context.MODE_PRIVATE
                )
        }

        private fun saveValueToKey(key: String, value: String) {
            try {
                if (null != App.newInstance.applicationContext) {
                    val editor: SharedPreferences.Editor =
                        getSharePref().edit()
                    editor.putString(key, value)
                    editor.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun addLocationFavorite(location: Location) {
            val locations = getLocationFavoriteList()
            val checkLocations = locations.filter { location.id == it.id }
            locations.removeAll(checkLocations)
            locations.add(location)
            saveValueToKey(KEY_LOCATIONS_SELECTED, Gson().toJson(locations))
        }

        fun removeLocationFavorite(location: Location) {
            val locations = getLocationFavoriteList()
            val checkLocations = locations.filter { location.id == it.id }
            locations.removeAll(checkLocations)
            saveValueToKey(KEY_LOCATIONS_SELECTED, Gson().toJson(locations))
        }

        fun getLocationFavoriteList(): MutableList<Location> {
            val locationsJson = getSharePref().getString(KEY_LOCATIONS_SELECTED, null)
            val locations = mutableListOf<Location>()
            if (locationsJson != null) {
                val gson = Gson()
                locations.addAll(
                    gson.fromJson(
                        locationsJson,
                        object : TypeToken<List<Location>>() {}.type
                    )
                )
            }
            return locations
        }
    }
}