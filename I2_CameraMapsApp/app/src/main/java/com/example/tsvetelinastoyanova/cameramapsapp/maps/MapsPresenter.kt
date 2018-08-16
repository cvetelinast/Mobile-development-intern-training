package com.example.tsvetelinastoyanova.cameramapsapp.maps

import android.content.Context
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository
import com.google.android.gms.maps.model.LatLng

class MapsPresenter(private val view: MapsContract.View, private val repository: Repository) : MapsContract.Presenter {
    private lateinit var location: LatLng

    override fun start() {
        Log.d("tag", "Maps presenter started")
    }

    override fun getPhotos(context: Context) {
        repository.getPhotos(context)
            .filter { photo -> photo.location != null }
            .subscribe(
                { photo ->
                    view.addMarker(photo)
                    Log.d("map&location", "Loaded photo: lat ${photo.location?.latitude}, lon ${photo.location?.longitude}")
                },
                { err ->
                    Log.d("map&location", "Error loading photos: $err")
                },
                {
                    Log.d("map&location", "Finish loading photos")
                }
            )
    }
}