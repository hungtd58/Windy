package com.tdh.windydemo.screen.home

import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.tdh.windydemo.R
import com.tdh.windydemo.databinding.ItemLocationWeatherViewBinding
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.utils.ImageUtils
import com.tdh.windydemo.utils.Utils

class LocationWeatherAdapter(
    private val weatherDataList: MutableList<ForecastWeatherDataModel>,
    private val actionRemoveListener: (location: ForecastWeatherDataModel) -> Unit
) :
    RecyclerView.Adapter<LocationWeatherAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemLocationWeatherViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = weatherDataList[position]
        holder.binding.locationNameTv.text = model.location?.name
        holder.binding.temperatureTv.text = "${Utils.kelvinToCelsius(model.main.temp)}°C"
        ImageUtils.displayImageFromUrl(
            holder.itemView.context,
            "http://openweathermap.org/img/wn/" + model.weather[0].icon + ".png",
            holder.binding.weatherStatusIv
        )
        holder.binding.windStatusTv.text =
            "${model.wind.speed}m/s ${Utils.convertDegreeToRotationName(model.wind.deg)}"
        val windDirectionBmp: Bitmap? =
            AppCompatResources.getDrawable(holder.itemView.context, R.drawable.ic_wind_direction)
                ?.toBitmap()

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
            holder.binding.directionWindIv.setImageBitmap(rotatedBitmap)
        } else {
            holder.binding.directionWindIv.setImageResource(R.drawable.ic_wind_direction)
        }
        holder.binding.removeBtn.setOnClickListener {
            actionRemoveListener.invoke(model)
            holder.binding.swipeLayout.close(true)
        }
    }

    override fun getItemCount(): Int {
        return weatherDataList.size
    }

    fun updateData(weatherDataList: MutableList<ForecastWeatherDataModel>) {
        this.weatherDataList.clear()
        this.weatherDataList.addAll(weatherDataList)
    }

    class VH(val binding: ItemLocationWeatherViewBinding) : RecyclerView.ViewHolder(binding.root)
}