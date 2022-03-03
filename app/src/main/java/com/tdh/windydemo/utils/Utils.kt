package com.tdh.windydemo.utils

import android.content.res.Resources
import android.util.TypedValue
import com.tdh.windydemo.App
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.math.roundToInt


class Utils {
    internal enum class CPoint {
        N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW
    }

    companion object {
        fun fahrenheitToCelsius(fahrenheit: Double): Int {
            return (((fahrenheit - 32) * 5) / 9).roundToInt()
        }

        fun kelvinToCelsius(kelvin: Double): Int {
            return (kelvin - 273.15).roundToInt()
        }

        fun convertDegreeToRotationName(deg: Int): String {
            val divisor = 360 / CPoint.values().size
            val index = deg / divisor
            val remainder = deg % divisor
            return if (remainder <= divisor / 2) {
                CPoint.values()[index % CPoint.values().size].name
            } else {
                CPoint.values()[(index + 1) % CPoint.values().size].name
            }
        }

        fun loadJsonFromAsset(fileName: String): String? {
            val json: String? = try {
                val `is`: InputStream = App.newInstance.assets.open(fileName)
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer, Charset.defaultCharset())
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }
            return json
        }
    }
}

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)