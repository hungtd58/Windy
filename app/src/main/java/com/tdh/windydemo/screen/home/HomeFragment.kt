package com.tdh.windydemo.screen.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseFragment
import com.tdh.windydemo.App
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentHomeBinding
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.screen.detail.DetailLocationDialogFragment
import com.tdh.windydemo.screen.detail.DetailLocationViewModel
import com.tdh.windydemo.screen.location.AddLocationDialogFragment
import com.tdh.windydemo.utils.DateTimeUtils
import com.tdh.windydemo.utils.GpsUtils
import com.tdh.windydemo.utils.ImageUtils
import com.tdh.windydemo.utils.Utils

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val detailLocationViewModel: DetailLocationViewModel by viewModels(ownerProducer = { requireActivity() })

    lateinit var locationWeatherAdapter: LocationWeatherAdapter
    lateinit var permissionResults: ActivityResultLauncher<Array<String>>

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initViewModels() {
        addViewModel(homeViewModel)
    }

    override fun observeViewModel() {
        homeViewModel.forecastWeatherDataModel.observe(viewLifecycleOwner) {
            setDataToView(it)
        }
        homeViewModel.favoriteLocationList.observe(viewLifecycleOwner) {
            locationWeatherAdapter.updateData(it)
            locationWeatherAdapter.notifyDataSetChanged()
        }
        homeViewModel.isLoading.removeObservers(viewLifecycleOwner)
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
    }

    fun setDataToView(model: ForecastWeatherDataModel?) {
        model?.let {
            binding.currentTimeTv.text = DateTimeUtils.getTimeStamp()
            binding.cityNameTv.text = model.name
            binding.cityNameTv.visibility = View.VISIBLE
            binding.mainWeatherStatusIv.visibility = View.VISIBLE
            ImageUtils.displayImageFromUrl(
                requireContext(),
                "http://openweathermap.org/img/wn/" + model.weather[0].icon + ".png",
                binding.mainWeatherStatusIv
            )
            binding.weatherStatusTv.text = model.weather[0].main
            binding.mainTemperatureTv.text = "${Utils.kelvinToCelsius(model.main.temp)}°C"
            binding.mainWindStatusTv.text =
                "Wind: ${model.wind.speed}m/s ${Utils.convertDegreeToRotationName(model.wind.deg)}"
            binding.humidityStatusTv.text = "Humidity: ${model.main.humidity}%"
            binding.pressureStatusTv.text = "Pressure: ${model.main.pressure}hPa"
            binding.visibilityStatusTv.text =
                "Visibility: ${Math.round(model.visibility / 100.0) / 10.0}km"

            val windDirectionBmp: Bitmap? =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_wind_direction)
                    ?.toBitmap()

            binding.mainDirectionWindIv.visibility = View.VISIBLE
            if (windDirectionBmp != null) {
                val matrix = Matrix()
                matrix.postRotate(180f + model.wind.deg - 45f)
                val scaledBitmap = Bitmap.createScaledBitmap(
                    windDirectionBmp,
                    windDirectionBmp.width,
                    windDirectionBmp.height,
                    true
                )

                val rotatedBitmap = Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.width,
                    scaledBitmap.height,
                    matrix,
                    true
                )
                binding.mainDirectionWindIv.setImageBitmap(rotatedBitmap)
            } else {
                binding.mainDirectionWindIv.setImageResource(R.drawable.ic_wind_direction)
            }
        } ?: run {
            binding.currentTimeTv.text = DateTimeUtils.getTimeStamp()
            binding.cityNameTv.visibility = View.GONE
            binding.mainWeatherStatusIv.visibility = View.GONE
            binding.weatherStatusTv.text = ""
            binding.mainTemperatureTv.text = ""
            binding.mainWindStatusTv.text = ""
            binding.humidityStatusTv.text = ""
            binding.pressureStatusTv.text = ""
            binding.visibilityStatusTv.text = ""
            binding.mainDirectionWindIv.visibility = View.GONE
        }
    }

    override fun setupView() {
        binding.refreshLayout.setOnRefreshListener {
            reloadData()
        }

        locationWeatherAdapter = LocationWeatherAdapter(mutableListOf(), {
            showMessage("Remove ${it.name}",
                "Do you want remove from favorite list?",
                "Remove",
                { dialog, _ ->
                    homeViewModel.removeLocation(it)
                    dialog.dismiss()
                },
                "Cancel",
                { dialog, _ -> dialog.dismiss() })
        }, {
            detailLocationViewModel.forecastWeatherDataModel.postValue(it)
            showDialogFragment(DetailLocationDialogFragment())
        })
        binding.locationRv.adapter = locationWeatherAdapter
        binding.addLocationTv.setOnClickListener {
            showDialogFragment(AddLocationDialogFragment().apply {
                onDismissListener = {
                    reloadData()
                }
            })
        }

        permissionResults = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.none { !it.value }) {
                reloadData()
            } else {
                homeViewModel.getForecastWeatherOfLocation(21.028511f, 105.804817f)
            }
        }
    }

    override fun initData() {
        reloadData()
    }

    private val gpsUtils = GpsUtils()

    private fun reloadData() {
        if (ActivityCompat.checkSelfPermission(
                App.newInstance,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                App.newInstance,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gpsUtils.getBinderLocation(object : GpsUtils.OnBindLocationService {
                override fun onBindLocationService(location: Location?) {
                    location?.let {
                        homeViewModel.getForecastWeatherOfLocation(
                            it.latitude.toFloat(),
                            it.longitude.toFloat()
                        )
                    } ?: run {
                        homeViewModel.getForecastWeatherOfLocation(21.028511f, 105.804817f)
                    }
                }

                override fun onFailure() {
                    homeViewModel.getForecastWeatherOfLocation(21.028511f, 105.804817f)
                }
            })
        } else {
            permissionResults.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
        homeViewModel.getForecastWeatherOfFavoriteLocations()

    }
}