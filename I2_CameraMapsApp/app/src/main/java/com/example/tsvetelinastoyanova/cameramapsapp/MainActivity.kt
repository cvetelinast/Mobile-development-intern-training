package com.example.tsvetelinastoyanova.cameramapsapp

import android.content.pm.ActivityInfo
import android.os.Bundle
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
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils

class MainActivity : AppCompatActivity(), GalleryFragment.FragmentsLoader {

    private var isGalleryVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        loadGalleryFragment()
    }

    private fun loadGalleryFragment() {
        Utils.setTranslucent(this, false)
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

    private fun createGalleryFragment(action: (sfm: FragmentManager, fragment: Fragment, id: Int, str: String) -> Unit) {
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()
        val galleryFragment = GalleryFragment.newInstance()
        if (!galleryFragment.isAdded) {
            //  showGalleryFragment(photosRepository)
            action(supportFragmentManager, galleryFragment, R.id.contentFragment, "GALLERY")
            galleryFragment.setGalleryPresenter(photosRepository)
        } else {
            Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment, "GALLERY")
        }
        isGalleryVisible = true

        /*val tempGalleryFragment = supportFragmentManager.findFragmentByTag("GALLERY") as GalleryFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()
        if (tempGalleryFragment == null) {
            galleryFragment = GalleryFragment.newInstance()
            showGalleryFragment(photosRepository)
            setGalleryPresenter(photosRepository)
        } else {
            galleryFragment = tempGalleryFragment
        }
        isGalleryVisible = true*/
    }

    fun GalleryFragment.setGalleryPresenter(photosRepository: PhotosRepository) {
        val galleryPresenter: GalleryContract.Presenter = GalleryPresenter(this, photosRepository)
        this.setPresenter(galleryPresenter)
    }

/*
    private fun showGalleryFragment(photosRepository: PhotosRepository) {
        if (supportFragmentManager.fragments.size == 0) {
            Utils.addFragmentToActivity(supportFragmentManager, galleryFragment, R.id.contentFragment, "GALLERY")
        } else {
            Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment, "GALLERY")
        }
    }
*/

    private fun createMapFragment() {
        /* mapsFragment = supportFragmentManager.findFragmentById(R.id.contentFragment) as MapsFragment?
         val photosRepository: PhotosRepository = Utils.providePhotosRepository()
         if (mapsFragment == null) {
             mapsFragment = MapsFragment.newInstance()
             mapsFragment?.let {
                 Utils.switchFragment(supportFragmentManager, it, R.id.contentFragment, "MAPS")
                 isGalleryVisible = false
             }
         }
         mapsFragment?.let {
             val mapsPresenter: MapsContract.Presenter = MapsPresenter(it, photosRepository)
             it.setPresenter(mapsPresenter)
         }*/
    }

    private fun createCameraFragment() {
        var cameraFragment = supportFragmentManager.findFragmentByTag("CAMERA") as CameraFragment?

        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        if (cameraFragment == null) {
            cameraFragment = CameraFragment.newInstance()

            Utils.switchFragment(supportFragmentManager, cameraFragment, R.id.contentFragment, "CAMERA")
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
