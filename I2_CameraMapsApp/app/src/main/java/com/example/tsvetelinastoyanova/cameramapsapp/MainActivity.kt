package com.example.tsvetelinastoyanova.cameramapsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraContract
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraFragment
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.data.PhotosRepository
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryContract
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryFragment
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.GalleryPresenter
import com.example.tsvetelinastoyanova.cameramapsapp.maps.MapsFragment
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GalleryFragment.FragmentsLoader/*, CameraFragment.PreviousFragmentLoader*/ {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadGalleryFragment()
    }

    private fun loadGalleryFragment() {
        //    if (contentFragment != null) {
        Utils.setTranslucent(this, false)
        createGalleryFragment()
        //     }
    }

    /*** Methods from GalleryFragment.FragmentsLoader ***/

    override fun onClickToLoadMap() {
        //   if (contentFragment != null) {
        Utils.setTranslucent(this, true)
        createMapFragment()
        Log.d("tag", "Loading map ...")
        //   }
    }

    override fun onClickToOpenCamera() {
        //    if (contentFragment != null) {
        Utils.setTranslucent(this, false)
        createCameraFragment()
        Log.d("tag", "Loading camera ...")
        //    }
    }

    //   /*** Methods from CameraFragment.PreviousFragmentLoader ***/

    /*override*/private fun loadPreviousFragment() {
        Log.d("tag", "Should load Gallery fragment")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            loadGalleryFragment()
          //  finish()
        }
        //loadGalleryFragment()
    }

    private fun createGalleryFragment() {
    //    var galleryFragment: GalleryFragment? = supportFragmentManager.findFragmentById(R.id.contentFragment) as? GalleryFragment
    //    if (galleryFragment == null) {
            val galleryFragment = GalleryFragment.newInstance()
            Utils.switchFragment(
                supportFragmentManager, galleryFragment, R.id.contentFragment)
        galleryFragment.javaClass.name
     //   }
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()
        val galleryPresenter: GalleryContract.Presenter = GalleryPresenter(galleryFragment, photosRepository)
        galleryFragment.setPresenter(galleryPresenter)
    }

    private fun createMapFragment() {
        var mapsFragment: MapsFragment? = supportFragmentManager.findFragmentById(R.id.contentFragment) as? MapsFragment
        //Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment)
    }

    private fun createCameraFragment() {
        //  var cameraFragment: CameraFragment? = supportFragmentManager.findFragmentById(R.id.contentFragment) as? CameraFragment

        /*if (cameraFragment == null) { // it is not null but is full with the other fragment
            cameraFragment = CameraFragment.newInstance()
            Utils.switchFragment(supportFragmentManager, cameraFragment, R.id.contentFragment)
        }*/


        var cameraFragment = CameraFragment.newInstance()
        Utils.switchFragment(supportFragmentManager, cameraFragment, R.id.contentFragment)


        val photosRepository: PhotosRepository = Utils.providePhotosRepository()  //todo: to see if returns the created repository
        val cameraPresenter: CameraContract.Presenter = CameraPresenter(cameraFragment, photosRepository)
        cameraFragment.setPresenter(cameraPresenter)
    }

    override fun onBackPressed() {
        Log.d("tag", "onBackPressed Called")
        loadPreviousFragment()
        // previousFragmentLoader.loadPreviousFragment()
    }
}
