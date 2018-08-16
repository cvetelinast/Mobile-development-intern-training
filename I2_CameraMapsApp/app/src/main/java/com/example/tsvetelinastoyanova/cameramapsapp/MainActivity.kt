package com.example.tsvetelinastoyanova.cameramapsapp

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraContract
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraFragment
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryContract
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryFragment
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.maps.MapsContract
import com.example.tsvetelinastoyanova.cameramapsapp.maps.MapsFragment
import com.example.tsvetelinastoyanova.cameramapsapp.maps.MapsPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.CAMERA_FRAGMENT_NAME
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.GALLERY_FRAGMENT_NAME
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.MAPS_FRAGMENT_NAME

class MainActivity : AppCompatActivity(), GalleryFragment.FragmentsLoader {

    private var isGalleryVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        createGalleryFragment(Utils::addFragmentToActivity)
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

    private fun createGalleryFragment(action: (fragmentManager: FragmentManager, fragment: Fragment, id: Int, str: String) -> Unit) {
        Utils.setTranslucent(this, false)
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()
        val galleryFragment = GalleryFragment.newInstance()
        if (!galleryFragment.isAdded) {
            action(supportFragmentManager, galleryFragment, R.id.contentFragment, GALLERY_FRAGMENT_NAME)
            galleryFragment.setGalleryPresenter(photosRepository)
        } else {
            Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment, GALLERY_FRAGMENT_NAME)
        }
        isGalleryVisible = true
    }

    private fun GalleryFragment.setGalleryPresenter(photosRepository: PhotosRepository) {
        val galleryPresenter: GalleryContract.Presenter = GalleryPresenter(this, photosRepository)
        this.setPresenter(galleryPresenter)
    }

    private fun createMapFragment() {
        var mapsFragment = supportFragmentManager.findFragmentByTag(MAPS_FRAGMENT_NAME) as MapsFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        if (mapsFragment == null) {
            mapsFragment = MapsFragment.newInstance()

            Utils.switchFragment(supportFragmentManager, mapsFragment, R.id.contentFragment, MAPS_FRAGMENT_NAME)
            isGalleryVisible = false
            val mapsPresenter: MapsContract.Presenter = MapsPresenter(mapsFragment, photosRepository)
            mapsFragment.setPresenter(mapsPresenter)
        }
    }

    private fun createCameraFragment() {
        var cameraFragment = supportFragmentManager.findFragmentByTag(CAMERA_FRAGMENT_NAME) as CameraFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        if (cameraFragment == null) {
            cameraFragment = CameraFragment.newInstance()

            Utils.switchFragment(supportFragmentManager, cameraFragment, R.id.contentFragment, CAMERA_FRAGMENT_NAME)
            isGalleryVisible = false
            val cameraPresenter: CameraContract.Presenter = CameraPresenter(cameraFragment, photosRepository)
            cameraFragment.setPresenter(cameraPresenter)
        }
    }

    override fun onBackPressed() {
        if (isGalleryVisible) {
            super.onBackPressed()
        } else {
            createGalleryFragment(Utils::switchFragment)
        }
    }
}
