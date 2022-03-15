package com.tdh.windydemo.screen.detail

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import com.tdh.libase.base.BaseDialogFragment
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.FragmentDialogDetailLocationBinding
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.utils.ImageUtils
import com.tdh.windydemo.utils.Utils

class DetailLocationDialogFragment : BaseDialogFragment<FragmentDialogDetailLocationBinding>() {
    private val detailLocationViewModel: DetailLocationViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun getLayoutId() = R.layout.fragment_dialog_detail_location

    override fun getWidth() = 1f

    override fun getHeight() = 1f

    override fun initViewModels() {
        addViewModel(detailLocationViewModel)
    }

    override fun observeViewModel() {
        detailLocationViewModel.forecastWeatherDataModel.observe(viewLifecycleOwner) {
            setDataToView(it)
        }
    }

    override fun setupView() {

    }

    override fun initData() {

    }

    private fun setDataToView(model: ForecastWeatherDataModel?) {
        model?.let {
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
}