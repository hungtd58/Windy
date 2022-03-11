package com.tdh.windydemo

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tdh.windydemo.model.ForecastWeatherDataModel
import com.tdh.windydemo.model.Main
import com.tdh.windydemo.model.Weather
import com.tdh.windydemo.model.Wind
import com.tdh.windydemo.recyclerview.RecyclerViewItemCountAssertion
import com.tdh.windydemo.recyclerview.TestUtils.withRecyclerView
import com.tdh.windydemo.screen.home.HomeFragment
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkBindViewHaveData() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            fragment.setDataToView(
                ForecastWeatherDataModel(
                    "",
                    null,
                    0,
                    null,
                    0,
                    0,
                    Main(283.0, 10, 10, 10, 10, 283.0, 283.0, 283.0),
                    "City Name Test",
                    null,
                    0,
                    10000,
                    arrayListOf(Weather("overcast clouds", "04d", 0, "Clouds")),
                    Wind(180, 2.01, 0.65),
                    null
                )
            )
        }
        onView(withId(R.id.city_name_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.city_name_tv)).check(matches(withText("City Name Test")))
        onView(withId(R.id.main_weather_status_iv)).check(matches(isDisplayed()))
        onView(withId(R.id.main_direction_wind_iv)).check(matches(isDisplayed()))
        onView(withId(R.id.main_temperature_tv)).check(matches(withText("10°C")))
        onView(withId(R.id.main_wind_status_tv)).check(matches(withText("Wind: 0.65m/s S")))
    }

    @Test
    fun checkBindViewNoData() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            fragment.setDataToView(null)
        }

        onView(withId(R.id.city_name_tv)).check(matches(not(isDisplayed())))
        onView(withId(R.id.main_weather_status_iv)).check(matches(not(isDisplayed())))
        onView(withId(R.id.main_direction_wind_iv)).check(matches(not(isDisplayed())))
    }

    @Test
    fun checkFavoriteLocations() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            fragment.locationWeatherAdapter.updateData(
                arrayListOf(
                    ForecastWeatherDataModel(
                        "",
                        null,
                        0,
                        null,
                        0,
                        0,
                        Main(283.0, 10, 10, 10, 10, 283.0, 283.0, 283.0),
                        "City Name Test 1",
                        null,
                        0,
                        10000,
                        arrayListOf(Weather("overcast clouds", "04d", 0, "Clouds")),
                        Wind(180, 2.01, 0.65),
                        null
                    ),
                    ForecastWeatherDataModel(
                        "",
                        null,
                        0,
                        null,
                        0,
                        0,
                        Main(283.0, 10, 10, 10, 10, 283.0, 283.0, 283.0),
                        "City Name Test 2",
                        null,
                        0,
                        10000,
                        arrayListOf(Weather("overcast clouds", "04d", 0, "Clouds")),
                        Wind(180, 2.01, 0.65),
                        null
                    ),
                )
            )
            fragment.locationWeatherAdapter.notifyDataSetChanged()
        }
        onView(withId(R.id.location_rv)).check(RecyclerViewItemCountAssertion(2))
        onView(withRecyclerView(R.id.location_rv).atPosition(0)).perform(
            repeatedlyUntil(
                swipeLeft(),
                hasDescendant(withText("Remove")),
                10
            )
        )
    }

    @Test
    fun checkOpenAddLocationDialog() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.add_location_tv)).perform(click())
        onView(withId(R.id.search_city_edt)).perform(typeTextIntoFocusedView("Bac Giang"))
        onView(withId(R.id.search_location_rv)).check(RecyclerViewItemCountAssertion(2))
        onView(withId(R.id.clear_ic)).perform(click())
        onView(withId(R.id.search_city_edt)).check(matches(withText("")))
    }
}