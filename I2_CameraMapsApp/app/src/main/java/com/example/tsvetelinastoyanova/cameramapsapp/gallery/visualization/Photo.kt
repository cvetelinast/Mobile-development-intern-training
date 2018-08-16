package com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization

import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.util.*

data class Photo(val name: String, val lastModified: Date, val location: LatLng?, val file: File)
