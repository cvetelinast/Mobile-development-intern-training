package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.tsvetelinastoyanova.cameramapsapp.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.tsvetelinastoyanova.cameramapsapp.camera.CameraFragment
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment(), GalleryContract.View {
    private lateinit var presenter: GalleryContract.Presenter

    /*** interface  ***/

    private lateinit var fragmentsLoader: FragmentsLoader

    interface FragmentsLoader {
        fun onClickToLoadMap()
        fun onClickToOpenCamera()
    }

    /*** Methods from Fragment  ***/

    override fun setPresenter(presenter: GalleryContract.Presenter) {
        this.presenter = presenter
    }

    companion object {
        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

   /* companion object {
        @Volatile
        private var INSTANCE: GalleryFragment? = null

        fun newInstance(): GalleryFragment {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GalleryFragment().also { INSTANCE = it }
            }
        }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        val toolbar = view.findViewById(R.id.toolbar_gallery) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentsLoader = context as? FragmentsLoader ?: throw ClassCastException(context?.toString()
            + " must implement MapLoader")
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
        setFloatingButtonListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_map -> {
                fragmentsLoader.onClickToLoadMap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFloatingButtonListener() {
        camera.setOnClickListener { _ -> fragmentsLoader.onClickToOpenCamera() }
    }
}