package com.tdh.windydemo

import com.tdh.windydemo.utils.Utils
import isNothing
import org.junit.Assert.assertEquals
import org.junit.Test
import stripAccent

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsFunctionUnitTest {
    @Test
    fun fahrenheitToCelsius_isCorrect() {
        assertEquals(0, Utils.fahrenheitToCelsius(32.0))
        assertEquals(10, Utils.fahrenheitToCelsius(50.0))
        assertEquals(-10, Utils.fahrenheitToCelsius(14.0))
        assertEquals(100, Utils.fahrenheitToCelsius(212.0))
    }

    @Test
    fun kelvinToCelsius_isCorrect() {
        assertEquals(0, Utils.kelvinToCelsius(273.15))
        assertEquals(10, Utils.kelvinToCelsius(283.15))
        assertEquals(-10, Utils.kelvinToCelsius(263.15))
        assertEquals(100, Utils.kelvinToCelsius(373.15))
    }

    @Test
    fun convertDegreeToRotationName_isCorrect() {
        assertEquals(Utils.CPoint.N.name, Utils.convertDegreeToRotationName(0))
        assertEquals(Utils.CPoint.E.name, Utils.convertDegreeToRotationName(90))
        assertEquals(Utils.CPoint.S.name, Utils.convertDegreeToRotationName(180))
        assertEquals(Utils.CPoint.W.name, Utils.convertDegreeToRotationName(270))
        assertEquals(Utils.CPoint.NNE.name, Utils.convertDegreeToRotationName(30))
        assertEquals(Utils.CPoint.NNW.name, Utils.convertDegreeToRotationName(-30))
        assertEquals(Utils.CPoint.NNW.name, Utils.convertDegreeToRotationName(330))
        assertEquals(Utils.CPoint.NNW.name, Utils.convertDegreeToRotationName(690))
    }

    @Test
    fun isNothing_isCorrect() {
        assertEquals(true, "".isNothing())
        assertEquals(false, "Hello".isNothing())
    }

    @Test
    fun stripAccent_isCorrect() {
        assertEquals("Ha Dong", "Hà Đông".stripAccent())
        assertEquals("Ha Noi", "Hà Nội".stripAccent())
        assertEquals("Quang Binh", "Quảng Bình".stripAccent())
    }
}