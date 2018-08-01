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
import org.junit.After
import java.lang.reflect.AccessibleObject.setAccessible
import android.telephony.SubscriptionManager
import android.os.Build
import java.io.IOException
import java.lang.reflect.AccessibleObject.setAccessible
import android.telephony.TelephonyManager
import java.lang.reflect.AccessibleObject.setAccessible


class NoInternetConnectionTest {
    private val VALID_CITY = "Varna"

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val wifiManager = activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false
        //    changeConnection(false)
        //   setMobileData(false)

        //    val internet = activityRule.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //   internet.activeNetwork = false
        // todo: turn off internet connection
    }

    @After
    fun clean() {
        val wifiManager = activityRule.activity.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true
        //   changeConnection(true)
        //  setMobileData(true)
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

    /* private fun setMobileData(context: Context, enabled: Boolean){
         val conman: ConnectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



     }*/

    /*   @Throws(ClassNotFoundException::class, IllegalAccessException::class, NoSuchMethodException::class)
       private fun setMobileData(enabled: Boolean) {
           val conman = activityRule.activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
           val conmanClass = Class.forName(conman.javaClass.name)
           val iConnectivityManagerField = conmanClass.getDeclaredField("mService")
           iConnectivityManagerField.isAccessible = true
           val iConnectivityManager = iConnectivityManagerField.get(conman)
           val iConnectivityManagerClass = Class.forName(iConnectivityManager.javaClass.name)
           val setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
           setMobileDataEnabledMethod.isAccessible = true
           setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled)
       }*/

    /*   private fun changeConnection(enable: Boolean) {
           try {
               val command = StringBuilder()
               command.append("su -c ")
               command.append("service call phone ")
               command.append(getTransactionCode() + " ")
               if (Build.VERSION.SDK_INT >= 22) {
                   val manager = SubscriptionManager.from(activityRule.activity)
                   var id = 0
                   if (manager.activeSubscriptionInfoCount > 0)
                       id = manager.activeSubscriptionInfoList[0].subscriptionId
                   command.append("i32 ")
                   command.append(id.toString() + " ")
               }
               command.append("i32 ")
               command.append(if (enable) "1" else "0")
               command.append("\n")
               Runtime.getRuntime().exec(command.toString())
           } catch (e: IOException) {
           }

       }

       private fun getTransactionCode(): String {
           try {
               val telephonyManager = activityRule.activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
               val telephonyManagerClass = Class.forName(telephonyManager.javaClass.name)
               val getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony")
               getITelephonyMethod.isAccessible = true
               val ITelephonyStub = getITelephonyMethod.invoke(telephonyManager)
               val ITelephonyClass = Class.forName(ITelephonyStub.javaClass.name)

               val stub = ITelephonyClass.declaringClass
               val field = stub.getDeclaredField("TRANSACTION_setDataEnabled")
               field.isAccessible = true
               return field.getInt(null).toString()
           } catch (e: Exception) {
               if (Build.VERSION.SDK_INT >= 22)
                   return "86"
               else if (Build.VERSION.SDK_INT == 21)
                   return "83"
           }

           return ""
       }*/

    /*   @Throws(Exception::class)
       fun setMobileNetworkfromLollipop(context: Context) {
           var command: String? = null
           var state = 0
           try {
               // Get the current state of the mobile network.
               state = if (isMobileDataEnabledFromLollipop(context)) 0 else 1
               // Get the value of the "TRANSACTION_setDataEnabled" field.
               val transactionCode = getTransactionCode(context)
               // Android 5.1+ (API 22) and later.
               if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                   val mSubscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
                   // Loop through the subscription list i.e. SIM list.
                   for (i in 0 until mSubscriptionManager.activeSubscriptionInfoCountMax) {
                       if (transactionCode != null && transactionCode!!.length > 0) {
                           // Get the active subscription ID for a given SIM card.
                           val subscriptionId = mSubscriptionManager.activeSubscriptionInfoList[i].subscriptionId
                           // Execute the command via `su` to turn off
                           // mobile network for a subscription service.
                           command = "service call phone $transactionCode i32 $subscriptionId i32 $state"
                           executeCommandViaSu(context, "-c", command)
                       }
                   }
               } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                   // Android 5.0 (API 21) only.
                   if (transactionCode != null && transactionCode!!.length > 0) {
                       // Execute the command via `su` to turn off mobile network.
                       command = "service call phone $transactionCode i32 $state"
                       executeCommandViaSu(context, "-c", command)
                   }
               }
           } catch (e: Exception) {
               // Oops! Something went wrong, so we throw the exception here.
               throw e
           }

       }

       private fun isMobileDataEnabledFromLollipop(context: Context): Boolean {
           var state = false
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               state = Settings.Global.getInt(context.contentResolver, "mobile_data", 0) === 1
           }
           return state
       }

       @Throws(Exception::class)
       private fun getTransactionCode(context: Context): String {
           try {
               val mTelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
               val mTelephonyClass = Class.forName(mTelephonyManager.javaClass.name)
               val mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony")
               mTelephonyMethod.isAccessible = true
               val mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager)
               val mTelephonyStubClass = Class.forName(mTelephonyStub.javaClass.name)
               val mClass = mTelephonyStubClass.declaringClass
               val field = mClass.getDeclaredField("TRANSACTION_setDataEnabled")
               field.isAccessible = true
               return String.valueOf(field.getInt(null))
           } catch (e: Exception) {
               // The "TRANSACTION_setDataEnabled" field is not available,
               // or named differently in the current API level, so we throw
               // an exception and inform users that the method is not available.
               throw e
           }

       }

       private fun executeCommandViaSu(context: Context, option: String, command: String) {
           var success = false
           var su = "su"
           for (i in 0..2) {
               // Default "su" command executed successfully, then quit.
               if (success) {
                   break
               }
               // Else, execute other "su" commands.
               if (i == 1) {
                   su = "/system/xbin/su"
               } else if (i == 2) {
                   su = "/system/bin/su"
               }
               try {
                   // Execute command as "su".
                   Runtime.getRuntime().exec(arrayOf(su, option, command))
               } catch (e: IOException) {
                   success = false
                   // Oops! Cannot execute `su` for some reason.
                   // Log error here.
               } finally {
                   success = true
               }
           }
       }*/

}