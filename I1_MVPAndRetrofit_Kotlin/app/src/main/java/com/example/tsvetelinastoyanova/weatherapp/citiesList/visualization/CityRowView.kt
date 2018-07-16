package com.example.tsvetelinastoyanova.weatherapp.citiesList.visualization


interface CityRowView {
    fun setName(name: String)

    fun setTemperature(temperature: Double)

    fun setIcon(iconId: Int)
}
