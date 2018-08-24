package com.example.tsvetelinastoyanova.hackernewsapp.model

import com.google.gson.annotations.SerializedName

class StoriesList(
    @SerializedName("stories") val stories: List<Integer>
)