package com.tdh.windydemo.home

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseFragment
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentHomeBinding
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.utils.DateTimeUtils
import com.tdh.windydemo.utils.ImageUtils
import com.tdh.windydemo.utils.Utils

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var locationWeatherAdapter: LocationWeatherAdapter

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

    private fun setDataToView(model: ForecastWeatherDataModel) {
        binding.currentTimeTv.text = DateTimeUtils.getTimeStamp()
        binding.cityNameTv.text = "Ha Noi"
        ImageUtils.displayImageFromUrl(
            requireContext(),
            "http://openweathermap.org/img/wn/" + model.weather[0].icon + ".png",
            binding.weatherStatusIv
        )
        binding.weatherStatusTv.text = model.weather[0].main
        binding.temperatureTv.text = "${Utils.kelvinToCelsius(model.main.temp)}°C"
        binding.windStatusTv.text =
            "Wind: ${model.wind.speed}m/s ${Utils.convertDegreeToRotationName(model.wind.deg)}"
        binding.humidityStatusTv.text = "Humidity: ${model.main.humidity}%"
        binding.pressureStatusTv.text = "Pressure: ${model.main.pressure}hPa"
        binding.visibilityStatusTv.text =
            "Visibility: ${Math.round(model.visibility / 100.0) / 10.0}km"

        val windDirectionBmp: Bitmap? =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_wind_direction)
                ?.toBitmap()

        if (windDirectionBmp != null) {
            val matrix = Matrix()
            matrix.postRotate(model.wind.deg - 45f)
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
            binding.directionWindIv.setImageBitmap(rotatedBitmap)
        } else {
            binding.directionWindIv.setImageResource(R.drawable.ic_wind_direction)
        }
    }

    override fun setupView() {
        binding.refreshLayout.setOnRefreshListener {
            homeViewModel.getForecastWeatherOfLocation(21.028511f, 105.804817f)
            homeViewModel.getForecastWeatherOfFavoriteLocations()
        }

        locationWeatherAdapter = LocationWeatherAdapter(mutableListOf()) {
            showMessage("Remove ${it.name}",
                "Do you want remove from favorite list?",
                "Remove",
                { dialog, which -> dialog.dismiss() },
                "Cancel",
                { dialog, which -> dialog.dismiss() })
        }
        binding.locationRv.adapter = locationWeatherAdapter
    }

    override fun initData() {
        homeViewModel.getForecastWeatherOfLocation(21.028511f, 105.804817f)
        homeViewModel.getForecastWeatherOfFavoriteLocations()
    }
}