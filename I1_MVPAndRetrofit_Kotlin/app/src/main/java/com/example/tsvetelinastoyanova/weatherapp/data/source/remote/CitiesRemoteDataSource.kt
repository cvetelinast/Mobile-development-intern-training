package com.example.tsvetelinastoyanova.weatherapp.data.source.remote

import com.example.tsvetelinastoyanova.weatherapp.Constants
import com.example.tsvetelinastoyanova.weatherapp.data.source.CityDataSource
import com.example.tsvetelinastoyanova.weatherapp.model.currentweather.CurrentWeatherObject
import com.example.tsvetelinastoyanova.weatherapp.model.forecast.WeatherObject
import com.example.tsvetelinastoyanova.weatherapp.util.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CitiesRemoteDataSource {
   /* val INSTANCE: CitiesRemoteDataSource? = null
        get() {
            if (field == null) {
                field = CitiesRemoteDataSource()
                return CitiesRemoteDataSource()
            }
            return INSTANCE
        }
*/

    companion object {

        private var INSTANCE: CitiesRemoteDataSource? = null

        fun getInstance(): CitiesRemoteDataSource? {
            if (INSTANCE == null) {
                synchronized(CitiesRemoteDataSource::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CitiesRemoteDataSource()
                    }
                }
            }
            return INSTANCE
        }
    }

    fun getWeatherObject(cityName: String, callback: CityDataSource.GetWeatherObjectCallback) {
        Utils.checkNotNull(cityName)
        Utils.checkNotNull(callback)
        val retrofit = RetrofitClientInstance.instance
        val service = retrofit.create(GetDataService::class.java)
        val call = service.getWeatherForCity(Constants.API_KEY, cityName)
        call.enqueue(object : Callback<CurrentWeatherObject> {
            override fun onResponse(call: Call<CurrentWeatherObject>, response: Response<CurrentWeatherObject>) {
                val weatherObject: CurrentWeatherObject? = response.body()
                if (weatherObject == null) {
                    callback.onFail()
                } else {
                    callback.onWeatherObjectLoaded(weatherObject)
                }
            }

            override fun onFailure(call: Call<CurrentWeatherObject>, t: Throwable) {
                callback.onFail()
            }
        })
    }
}