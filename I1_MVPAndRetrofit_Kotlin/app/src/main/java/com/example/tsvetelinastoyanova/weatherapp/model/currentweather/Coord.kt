package com.example.tsvetelinastoyanova.weatherapp.model.currentweather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coord(
        @SerializedName("lon") val lon: Double,
        @SerializedName("lat") val lat: Double
) : Parcelable