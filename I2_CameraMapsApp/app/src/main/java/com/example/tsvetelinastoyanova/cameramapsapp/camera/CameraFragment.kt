package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.cameramapsapp.R
import android.support.design.widget.FloatingActionButton
import android.view.TextureView
import android.widget.Toast

class CameraFragment : Fragment(), CameraContract.View {
    private var presenter: CameraContract.Presenter? = null
    private val CAMERA_REQUEST_CODE: Int = 1888
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1889


    /*** Methods from Fragment  ***/
    override fun setPresenter(presenter: CameraContract.Presenter) {
        Log.d("tag", "Set Camera Presenter in CameraFragment")
        this.presenter = presenter
    }

    override fun showFileSavedMessage() {
        Toast.makeText(activity, R.string.file_saved_successfully, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): CameraFragment {
            return CameraFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        createCameraView(view)
        val s = view.findViewById(R.id.fab_take_photo) as FloatingActionButton
        s.setOnClickListener { _ ->
            activity?.let {
                presenter?.onTakePhotoButtonClicked(it)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        checkNotNull(presenter)
        Log.d("tag", "onResume() in CameraPresenter")
        presenter?.start()
        context?.let {
            presenter?.calledInOnResume(it)
        }
    }

    private fun createCameraView(view: View) {
        val textureView: TextureView = view.findViewById(R.id.texture_view)

        checkNotNull(presenter)
        presenter!!.apply {
            this.initTextureView(textureView)

            activity?.let {
                this.requestPermission(it, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                this.requestPermission(it,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
                this.initCameraManager(it)
                this.setSurfaceTextureListener()
                this.initStateCallback()
            }
        }
    }
}