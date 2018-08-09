package com.example.tsvetelinastoyanova.cameramapsapp

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraContract
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraFragment
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryContract
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryFragment
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.maps.MapsFragment
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils

class MainActivity : AppCompatActivity(), GalleryFragment.FragmentsLoader/*, CameraFragment.PreviousFragmentLoader*/ {
    private var galleryFragment: GalleryFragment? = null
    private var cameraFragment: CameraFragment? = null
    private var mapsFragment: MapsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        loadGalleryFragment()
    }

    private fun loadGalleryFragment() {
        Utils.setTranslucent(this, false)
        createGalleryFragment()
    }

    /*** Methods from GalleryFragment.FragmentsLoader ***/

    override fun onClickToLoadMap() {
        Utils.setTranslucent(this, true)
        createMapFragment()
    }

    override fun onClickToOpenCamera() {
        Utils.setTranslucent(this, false)
        createCameraFragment()
    }

    //   /*** Methods from CameraFragment.PreviousFragmentLoader ***/

    /*override*/private fun loadPreviousFragment() {
        Utils.popBackStack(supportFragmentManager)
        if (supportFragmentManager.backStackEntryCount > 0) {
            Utils.popBackStack(supportFragmentManager)
            val tempGalleryFragment = galleryFragment
            tempGalleryFragment?.let {
                Utils.switchFragment(supportFragmentManager, it, R.id.contentFragment, "GALLERY")
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun createGalleryFragment() {
        galleryFragment = supportFragmentManager.findFragmentByTag("GALLERY") as GalleryFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()
        if (galleryFragment == null) {
            galleryFragment = GalleryFragment.newInstance()
            galleryFragment?.let {
                Utils.addFragmentToActivity(supportFragmentManager, it, R.id.contentFragment, "GALLERY")
            }
        }
        galleryFragment?.let {
            val galleryPresenter: GalleryContract.Presenter = GalleryPresenter(it, photosRepository)
            it.setPresenter(galleryPresenter)
        }
    }

    private fun createMapFragment() {
        var mapsFragment: MapsFragment? = supportFragmentManager.findFragmentById(R.id.contentFragment) as? MapsFragment
        //Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment)
    }

    private fun createCameraFragment() {
        cameraFragment = supportFragmentManager.findFragmentByTag("CAMERA") as CameraFragment?

        val photosRepository: PhotosRepository = Utils.providePhotosRepository()  //todo: to see if returns the created repository

        if (cameraFragment == null) { // it is not null but is full with the other fragment
            cameraFragment = CameraFragment.newInstance()
            cameraFragment?.let {
                Utils.switchFragment(supportFragmentManager, it, R.id.contentFragment, "CAMERA")
            }
        }

        cameraFragment?.let {
            val cameraPresenter: CameraContract.Presenter = CameraPresenter(it, photosRepository)
            it.setPresenter(cameraPresenter)
        }
    }


    override fun onBackPressed() {
        loadPreviousFragment()
    }
}
