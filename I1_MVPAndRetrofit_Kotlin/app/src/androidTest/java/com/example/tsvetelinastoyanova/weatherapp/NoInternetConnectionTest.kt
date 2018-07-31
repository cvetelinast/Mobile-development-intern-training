package com.example.tsvetelinastoyanova.weatherapp

import android.content.Context
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.RootMatchers
import android.support.test.espresso.matcher.ViewMatchers
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import android.content.Context.WIFI_SERVICE
import android.net.ConnectivityManager
import android.net.wifi.WifiManager


class NoInternetConnectionTest {
    private val VALID_CITY = "Varna"

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val wifiManager = activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
        val internet = activityRule.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // internet.activeNetwork = false
        // todo: turn off internet connection
    }

    @Test
    fun testNoInternetConnectionWhenLoadingApp() {
        //      if (!Utils.isNetworkAvailable(activityRule.activity)) {
        checkToastMessage(R.string.problem_loading_cities)
        //    }
    }

    @Test
    fun testNoInternetConnectionWhenAddingCity() {
        //      if (!Utils.isNetworkAvailable(activityRule.activity)) {

        Espresso.onView(ViewMatchers.withId(R.id.city))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText(VALID_CITY))

        Espresso.onView(ViewMatchers.withId(R.id.addCityButton))
            .perform(ViewActions.click())
        checkToastMessage(R.string.problem_network_connection)
        //   }
    }

    private fun checkToastMessage(messageId: Int) {
        Espresso.onView(ViewMatchers.withText(messageId))
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}