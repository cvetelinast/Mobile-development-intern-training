package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.app.Activity
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
import android.graphics.Point
import android.widget.Toast

class GalleryFragment : Fragment(), GalleryContract.View {
    private var presenter: GalleryContract.Presenter? = null
    private lateinit var recyclerView: RecyclerView

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
        createRecyclerView(view)?.let { loadPhotosInRecyclerView(it) }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentsLoader = context as? FragmentsLoader ?: throw ClassCastException(context?.toString()
            + " must implement MapLoader")
    }

    override fun onResume() {
        super.onResume()
        presenter!!.start()
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

    private fun createRecyclerView(view: View): PhotosAdapter? {
        this.recyclerView = view.findViewById(R.id.recyclerView)

        var photosAdapter: PhotosAdapter? = null
        activity?.let { photosAdapter = createPhotosAdapter(it) }
        photosAdapter?.let { setRecyclerViewAttributes(it) }
        return photosAdapter
    }

    private fun createPhotosAdapter(activity: Activity): PhotosAdapter {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        return PhotosAdapter(photosList = mutableListOf(), context = activity, WIDTH = width / 3,
            HEIGHT = height / 3)
        { Toast.makeText(activity, "Clicked on ${it.name}", Toast.LENGTH_SHORT).show() }
    }

    private fun setRecyclerViewAttributes(photosAdapter: PhotosAdapter) {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = photosAdapter
    }

    private fun loadPhotosInRecyclerView(photosAdapter: PhotosAdapter) {
        context?.let {
            presenter!!.getListOfPhotosOneByOne(it)
                .subscribe(
                    { photo ->
                        photosAdapter.addNewPhoto(photo)
                        Log.d("photo", "Loaded photo: ${photo.name}")
                    },
                    { err ->
                        Log.d("photo", "Error loading photos: $err")
                    },
                    {
                        photosAdapter.notifyDataSetChanged()
                    }
                )
        }
    }
}
