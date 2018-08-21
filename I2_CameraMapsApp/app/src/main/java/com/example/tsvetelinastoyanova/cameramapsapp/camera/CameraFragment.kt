package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsvetelinastoyanova.cameramapsapp.R
import android.support.design.widget.FloatingActionButton
import android.view.TextureView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment(), CameraContract.View {

    private var presenter: CameraContract.Presenter? = null

    /*** Methods from Fragment  ***/
    override fun setPresenter(presenter: CameraContract.Presenter) {
        this.presenter = presenter
    }

    override fun showErrorOpeningTheCamera() {
        Toast.makeText(activity, R.string.error_opening_camera, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorMessageForSavingFile() {
        Toast.makeText(activity, R.string.error_saving_file, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    /***/

    companion object {
        fun newInstance(): CameraFragment {
            return CameraFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    override fun onPause() {
        super.onPause()
        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        createCameraView(view)
        val button = view.findViewById(R.id.fab_take_photo) as FloatingActionButton
        button.setOnClickListener { _ -> presenter?.onTakePhotoButtonClicked(requireActivity()) }
        return view
    }

    private fun createCameraView(view: View) {
        val textureView: TextureView = view.findViewById(R.id.texture_view)

        presenter?.apply {
            setTextureView(textureView)
            init(requireContext())
            startCameraIfPossible(requireActivity())
        }
    }

    private fun changeRequestedOrientation(orientation: Int) {
        requireActivity().requestedOrientation = orientation
    }
}