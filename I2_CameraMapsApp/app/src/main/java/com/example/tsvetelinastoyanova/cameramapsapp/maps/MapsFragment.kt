package com.example.tsvetelinastoyanova.cameramapsapp.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.cameramapsapp.R
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.onClickLoadPhotoInGallery
import java.io.File

class MapsFragment : Fragment(), MapsContract.View, GoogleMap.OnInfoWindowClickListener {

    private var presenter: MapsContract.Presenter? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var infoWindowAdapter: GoogleMap.InfoWindowAdapter

    companion object {
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }

    override fun addMarker(photo: Photo) {
        val location = photo.location
        location?.let {
            googleMap.setInfoWindowAdapter(infoWindowAdapter)
            googleMap.setOnInfoWindowClickListener(this)

            val marker = googleMap.addMarker(MarkerOptions().position(location).title(photo.name)
                .snippet(photo.lastModified.toString()))
            marker.tag = Pair<String, String>(photo.file.absolutePath, photo.lastModified.toString())
        }
    }

    override fun setPresenter(presenter: MapsContract.Presenter) {
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        handleMap()
        createInfoWindowAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter!!.start()
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val pair = marker?.tag as? Pair<String, String>
        pair?.first?.apply {
            onClickLoadPhotoInGallery(File(this), requireActivity())
        }
    }

    private fun handleMap() {
        mapView.getMapAsync { mMap ->
            googleMap = mMap
            requestPermissionIfNeeded()

            context?.let { presenter?.getPhotos(it) }
        }
    }

    private fun requestPermissionIfNeeded() {
        activity?.let {
            if (ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                googleMap.isMyLocationEnabled = true
            } else {
                Utils.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Utils.LOCATION_REQUEST_CODE)
            }
        }
    }

    private fun createInfoWindowAdapter() {
        infoWindowAdapter = object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View? {
                val pair = marker.tag as? Pair<String, String>

                val view = requireActivity().layoutInflater.inflate(R.layout.photo_in_geomarker, null)
                val photoContainer = view.findViewById<ImageView>(R.id.photo)
                val drawable = Drawable.createFromPath(pair?.first)
                photoContainer.setImageDrawable(drawable)
                val dateTime = view.findViewById<TextView>(R.id.dateTime)
                dateTime.text = pair?.second

                return view
            }
        }
    }
}