package com.tdh.windydemo.utils

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tdh.windydemo.App

class GpsUtils {
    fun getBinderLocation(onBindLocationService: OnBindLocationService?) {
        if (onBindLocationService == null) return
        if (ActivityCompat.checkSelfPermission(
                App.newInstance,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                App.newInstance,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getFusedLocationClient()?.let {
                it.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val location: Location? = task.result
                            onBindLocationService.onBindLocationService(location)
                        }
                    }
            } ?: run {
                onBindLocationService.onFailure()
            }
        }
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private fun getFusedLocationClient(): FusedLocationProviderClient? {
        if (fusedLocationClient == null) {
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(App.newInstance)
        }
        return fusedLocationClient
    }

    interface OnBindLocationService {
        fun onBindLocationService(location: Location?)
        fun onFailure()
    }
}