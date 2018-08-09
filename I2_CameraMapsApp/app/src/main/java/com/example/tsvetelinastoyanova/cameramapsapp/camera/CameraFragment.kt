package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.cameramapsapp.R
import android.support.design.widget.FloatingActionButton
import android.view.TextureView


class CameraFragment : Fragment(), CameraContract.View {
    private lateinit var presenter: CameraContract.Presenter

    /*** interface  ***/
    /*
    private lateinit var previousFragmentLoader: PreviousFragmentLoader

    interface PreviousFragmentLoader {
        fun loadPreviousFragment()
    }*/

    /*** Methods from Fragment  ***/
    override fun setPresenter(presenter: CameraContract.Presenter) {
        Log.d("tag", "Set Camera Presenter in CameraFragment")
        this.presenter = presenter
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
            context?.let {
                presenter.onTakePhotoButtonClicked(it)
            }
        }
        return view
    }

    /* override fun onAttach(context: Context?) {
         super.onAttach(context)
         previousFragmentLoader = context as? PreviousFragmentLoader ?: throw ClassCastException(context?.toString()
             + " must implement PreviousFragmentLoader")
     }*/

    override fun onResume() {
        super.onResume()
        presenter.start()
        context?.let {
            presenter.calledInOnResume(it)
        }
    }

    private fun createCameraView(view: View) {
        val textureView: TextureView = view.findViewById(R.id.texture_view)
        presenter.initTextureView(textureView)

        activity?.let {
            presenter.requestPermissions(it)
            presenter.initCameraManager(it)
            presenter.setSurfaceTextureListener()
            presenter.initStateCallback()
        }
    }
}