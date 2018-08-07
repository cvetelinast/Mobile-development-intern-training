package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.tsvetelinastoyanova.cameramapsapp.R
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.PhotosAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment(), GalleryContract.View {
    private lateinit var presenter: GalleryContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var photosAdapter: PhotosAdapter

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        val toolbar = view.findViewById(R.id.toolbar_gallery) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        setRecyclerView(view)
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

    private fun setRecyclerView(view: View) {
        this.recyclerView = view.findViewById(R.id.recyclerView)

        context?.let {
            presenter.getListOfPhotosOneByOne(it)
                .subscribe(
                    { photo ->
                        photosAdapter.addNewPhoto(photo)
                        Log.d("tag", "loaded photo: $photo")
                    },
                    { err -> Log.d("tag", "Error loading photos: $err") }
                )
        }
        context?.let {
            photosAdapter = PhotosAdapter(photosList = ArrayList(), context = it)
        }

        val mLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = photosAdapter
    }
}