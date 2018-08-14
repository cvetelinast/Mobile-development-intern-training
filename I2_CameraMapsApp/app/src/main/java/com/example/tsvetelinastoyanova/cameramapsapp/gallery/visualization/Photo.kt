package com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization

import java.io.File
import java.util.*

data class Photo(val name: String, val lastModified: Date, val lat: String, val lon: String, val file: File)
