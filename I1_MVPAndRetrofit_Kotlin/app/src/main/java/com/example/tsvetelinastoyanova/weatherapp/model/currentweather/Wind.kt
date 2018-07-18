package com.example.tsvetelinastoyanova.weatherapp.model.currentweather

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wind(
        @SerializedName("speed") val speed: Double,
        @SerializedName("deg") val deg: Int
) : Parcelable