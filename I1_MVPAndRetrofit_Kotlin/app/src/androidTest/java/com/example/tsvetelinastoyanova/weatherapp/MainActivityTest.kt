package com.example.tsvetelinastoyanova.weatherapp

import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.uiautomator.UiDevice
import org.hamcrest.Matchers.*

import android.os.SystemClock
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import com.example.tsvetelinastoyanova.weatherapp.citiesList.visualization.CitiesAdapter
import android.support.test.InstrumentationRegistry.getInstrumentation
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import org.junit.*

class MainActivityTest {
    private val VALID_CITY = "Varna"
    private val NOT_ADDED_CITY = "Reconquista"
    private val degreeCelsiusSign: String = String.format(Constants.DEGREES_CELSIUS, "")

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun init() {
        // todo: How to prepare recyclerView for testing?
        addCity(VALID_CITY)
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())
        device.setOrientationNatural()
    }

    /*@After
    fun clear() {
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CitiesAdapter.MyViewHolder>(0, ViewActions.swipeLeft()))
        SystemClock.sleep(3000)
    }
*/
    @Test
    fun testWrongInput() {
        addCity("Not valid city name")
        checkForErrorToastMessage()
    }

    @Test
    fun testValidInput() {
        addCity(NOT_ADDED_CITY)
        checkForSuccessfullyAddedToastMessage()
    }


    @Test
    fun testValidInputWithRepeatingCity() {
        addCity(VALID_CITY)
        checkForAddingAddedCity()
    }

    @Test
    fun addCityClickAndValidate() {
        clickOnFirstRowOfRecyclerView()
        validateWeatherDetails()
    }

    @Test
    fun checkOnClickCityEvent() {
        clickOnFirstRowOfRecyclerView()
        if (!Utils.isTablet(activityRule.activity)) {
            checkActivityStarted()
        }
        Espresso.onView(withId(R.id.cityAndCountry)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.cityAndCountry))
            .perform(swipeLeft())
    }

    @Test
    fun checkOnClickCityAndRotate() {
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())

        clickOnFirstRowOfRecyclerView()
        if (!Utils.isTablet(activityRule.activity)) {
            checkActivityStarted()
        }
        Espresso.onView(withId(R.id.cityAndCountry)).check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.degrees)).check(matches(withText(endsWith(degreeCelsiusSign))))
        Espresso.onView(withId(R.id.cityAndCountry)).perform(swipeLeft())
        Espresso.onView(withId(R.id.combined_chart)).check(matches(isDisplayed()))

        device.setOrientationLeft()
        Espresso.onView(withId(R.id.combined_chart)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.combined_chart)).perform(swipeRight())
        Espresso.onView(withId(R.id.degrees)).check(matches(withText(endsWith(degreeCelsiusSign))))

        device.setOrientationNatural()
        Espresso.onView(withId(R.id.degrees)).check(matches(withText(endsWith(degreeCelsiusSign))))
    }

    @Test
    fun deleteCityOnSwipe() {
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CitiesAdapter.MyViewHolder>(0, ViewActions.swipeLeft()))
        SystemClock.sleep(2000)
        checkForDeletedCityToastMessage()
    }

    @Test
    fun deleteCityAndUndo() {
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CitiesAdapter.MyViewHolder>(0, ViewActions.swipeLeft()))
        Espresso.onView(withId(android.support.design.R.id.snackbar_action))
            .perform(click())
        SystemClock.sleep(3000) // wait to see that it will not be removed
    }

    @Test
    fun rotateCitiesList() {
        val device: UiDevice = UiDevice.getInstance(getInstrumentation())

        device.setOrientationLeft()
        Espresso.onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        device.setOrientationNatural()
        Espresso.onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        device.setOrientationRight()
        Espresso.onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        device.setOrientationNatural()
        Espresso.onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    private fun validateWeatherDetails() {
        Espresso.onView(withId(R.id.degrees)).check(matches(withText(endsWith(degreeCelsiusSign))))

        val tempMinPrefix: String = activityRule.activity.resources.getString(R.string.temperature_min, "")
        Espresso.onView(withId(R.id.tempMin)).check(matches(withText(startsWith(tempMinPrefix))))
        Espresso.onView(withId(R.id.tempMin)).check(matches(withText(endsWith(degreeCelsiusSign))))

        val tempMaxPrefix: String = activityRule.activity.resources.getString(R.string.temperature_max, "")
        Espresso.onView(withId(R.id.tempMax)).check(matches(withText(startsWith(tempMaxPrefix))))
        Espresso.onView(withId(R.id.tempMax)).check(matches(withText(endsWith(degreeCelsiusSign))))

        val pressurePrefix: String = activityRule.activity.resources.getString(R.string.pressure, 0f)
            .substringBefore(":")
        Espresso.onView(withId(R.id.pressure)).check(matches(withText(startsWith(pressurePrefix))))

        val humidityPrefix: String = activityRule.activity.resources.getString(R.string.humidity, 0)
            .substringBefore(":")
        Espresso.onView(withId(R.id.humidity)).check(matches(withText(startsWith(humidityPrefix))))

        val speedOfWindPrefix: String = activityRule.activity.resources.getString(R.string.speed_of_wind, 0f)
            .substringBefore(":")
        Espresso.onView(withId(R.id.windSpeed)).check(matches(withText(startsWith(speedOfWindPrefix))))
    }

    private fun clickOnFirstRowOfRecyclerView() {
        Espresso.onView(withId(R.id.recyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<CitiesAdapter.MyViewHolder>(0, click()))
    }

    private fun addCity(input: String) {
        Espresso.onView(withId(R.id.city))
            .perform(clearText())
            .perform(typeText(input))
        Espresso.onView(withId(R.id.addCityButton)).perform(click())
    }

    private fun checkForErrorToastMessage() {
        checkToastMessage(R.string.problem_not_valid_city_name)
    }

    private fun checkForSuccessfullyAddedToastMessage() {
        checkToastMessage(R.string.added_city)
    }

    private fun checkForAddingAddedCity() {
        checkToastMessage(R.string.existing_city)
    }

    private fun checkForDeletedCityToastMessage() {
        checkToastMessage(R.string.city_deleted_success)
    }

    private fun checkToastMessage(messageId: Int) {
        Espresso.onView(withText(messageId))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView)))).check(matches(isDisplayed()))
    }

    private fun checkActivityStarted() {
        Intents.intended(IntentMatchers.hasComponent(WeatherDetailsActivity::class.java.name))
    }
}