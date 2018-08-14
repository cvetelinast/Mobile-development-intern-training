package com.example.tsvetelinastoyanova.cameramapsapp.maps

import android.support.v4.app.Fragment
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraFragment

class MapsFragment : Fragment(), MapsContract.View {

    companion object {
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }

    override fun setPresenter(presenter: MapsContract.Presenter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}