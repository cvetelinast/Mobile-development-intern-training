package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.cameramapsapp.R

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
    }
}