package com.example.tsvetelinastoyanova.cameramapsapp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
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
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_CAMERA_FRAGMENT
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_GALLERY_FRAGMENT
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_MAPS_FRAGMENT

class MainActivity : AppCompatActivity(), GalleryFragment.FragmentsLoader {

    private var isGalleryVisible: Boolean = true
    private var bucketButton: Button? = null
    private var currentFragmentAction: String = ""

    companion object {
        const val CURRENT_FRAGMENT_ACTION = "CURRENT_FRAGMENT_ACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        if (savedInstanceState != null) with(savedInstanceState) {
            currentFragmentAction = getString(CURRENT_FRAGMENT_ACTION) ?: ""
            intent.action = currentFragmentAction
        } else {
            currentFragmentAction = intent.action ?: ""
        }
        loadParticularFragment()
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

    override fun onBucketButtonReady(button: Button) {
        bucketButton = button
    }

    private fun loadParticularFragment() {
        when (intent.action) {
            OPEN_MAPS_FRAGMENT -> {
                onClickToLoadMap()
            }
            OPEN_CAMERA_FRAGMENT -> {
                onClickToOpenCamera()
            }
            else -> {
                createGalleryFragment(Utils::addFragmentToActivity)
            }
        }
    }

    private fun createGalleryFragment(action: (fragmentManager: FragmentManager, fragment: Fragment, id: Int, str: String) -> Unit) {
        currentFragmentAction = OPEN_GALLERY_FRAGMENT
        Utils.setTranslucent(this, false)
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        var galleryFragment = supportFragmentManager.findFragmentByTag(GALLERY_FRAGMENT_NAME) as GalleryFragment?
        if (galleryFragment == null) {
            galleryFragment = GalleryFragment.newInstance()
            action(supportFragmentManager, galleryFragment, R.id.contentFragment, GALLERY_FRAGMENT_NAME)
        } else {
            Utils.switchFragment(supportFragmentManager, galleryFragment, R.id.contentFragment, GALLERY_FRAGMENT_NAME)
        }

        galleryFragment.setGalleryPresenter(photosRepository)
        isGalleryVisible = true
    }

    private fun GalleryFragment.setGalleryPresenter(photosRepository: PhotosRepository) {
        val galleryPresenter: GalleryContract.Presenter = GalleryPresenter(this, photosRepository)
        this.setPresenter(galleryPresenter)
    }

    private fun createMapFragment() {
        currentFragmentAction = OPEN_MAPS_FRAGMENT
        var mapsFragment = supportFragmentManager.findFragmentByTag(MAPS_FRAGMENT_NAME) as MapsFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        if (mapsFragment == null) {
            mapsFragment = MapsFragment.newInstance()
            Utils.switchFragment(supportFragmentManager, mapsFragment, R.id.contentFragment, MAPS_FRAGMENT_NAME)
        }
        isGalleryVisible = false
        val mapsPresenter: MapsContract.Presenter = MapsPresenter(mapsFragment, photosRepository)
        mapsFragment.setPresenter(mapsPresenter)
    }

    private fun createCameraFragment() {
        currentFragmentAction = OPEN_CAMERA_FRAGMENT
        var cameraFragment = supportFragmentManager.findFragmentByTag(CAMERA_FRAGMENT_NAME) as CameraFragment?
        val photosRepository: PhotosRepository = Utils.providePhotosRepository()

        if (cameraFragment == null) {
            cameraFragment = CameraFragment.newInstance()
            Utils.switchFragment(supportFragmentManager, cameraFragment, R.id.contentFragment, CAMERA_FRAGMENT_NAME)
        }
        isGalleryVisible = false
        val cameraPresenter: CameraContract.Presenter = CameraPresenter(cameraFragment, photosRepository)
        cameraFragment.setPresenter(cameraPresenter)
    }

    override fun onBackPressed() {
        if (bucketButton != null && bucketButton?.visibility == View.VISIBLE) {
            bucketButton?.visibility = View.INVISIBLE
        } else if (isGalleryVisible) {
            super.onBackPressed()
        } else {
            createGalleryFragment(Utils::switchFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString(CURRENT_FRAGMENT_ACTION, currentFragmentAction)
        }
        super.onSaveInstanceState(outState)
    }
}
