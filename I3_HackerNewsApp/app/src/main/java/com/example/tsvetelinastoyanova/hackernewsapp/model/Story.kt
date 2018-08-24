package com.example.tsvetelinastoyanova.hackernewsapp.model
import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("deleted") val deleted: Boolean,
    @SerializedName("text") val text: String,
    @SerializedName("dead") val dead: Boolean,
    @SerializedName("parent") val parent: Int,
    @SerializedName("poll") val poll: String,
    @SerializedName("parts") val parts: List<Int>,
    @SerializedName("by") val by: String,
    @SerializedName("descendants") val descendants: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("kids") val kids: List<Int>,
    @SerializedName("score") val score: Int,
    @SerializedName("time") val time: Int,
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String
)
