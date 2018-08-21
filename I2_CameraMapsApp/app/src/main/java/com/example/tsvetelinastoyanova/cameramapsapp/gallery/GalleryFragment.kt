package com.example.tsvetelinastoyanova.cameramapsapp.gallery

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import android.support.v4.app.ActivityCompat
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.CAMERA_AND_LOCATION_REQUEST_CODE
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.LOCATION_REQUEST_CODE
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.PATHS
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import com.example.tsvetelinastoyanova.cameramapsapp.CameraMapsAppWidget
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.onClickLoadPhotoInGallery

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
        createToolbar(view)
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_map -> {
                checkPermissionToOpenMap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fragmentsLoader.onClickToLoadMap()
                }
                return
            }
            CAMERA_AND_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if ((grantResults.size == 2 && grantResults[1] == PackageManager.PERMISSION_GRANTED) || grantResults.size == 1) {
                        fragmentsLoader.onClickToOpenCamera()
                    }
                }
            }
        }
    }

    private fun createToolbar(view: View) {
        val toolbar = view.findViewById(R.id.toolbar_gallery) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
    }

    private fun setFloatingButtonListener() {
        camera.setOnClickListener { _ ->
            checkPermissionsToOpenCamera()
        }
    }

    private fun checkPermissionsToOpenCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), CAMERA_AND_LOCATION_REQUEST_CODE)
        } else if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Utils.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), CAMERA_AND_LOCATION_REQUEST_CODE)
        } else if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), CAMERA_AND_LOCATION_REQUEST_CODE)
        } else {
            fragmentsLoader.onClickToOpenCamera()
        }
    }

    private fun checkPermissionToOpenMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            fragmentsLoader.onClickToLoadMap()
        }
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
        {
            onClickLoadPhotoInGallery(it.file, requireActivity())
        }
    }

    private fun setRecyclerViewAttributes(photosAdapter: PhotosAdapter) {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = photosAdapter
    }

    private fun loadPhotosInRecyclerView(photosAdapter: PhotosAdapter) {
        presenter?.let {
            it.getPhotos(requireContext())
                .subscribe(
                    { photo ->
                        photosAdapter.addNewPhoto(photo)
                        Log.d("photo1", "Loaded photo: ${photo.name}")
                    },
                    { err ->
                        Log.d("photo1", "Error loading photos: $err")
                    },
                    {
                        Log.d("photo1", "Finished loading photos")
                        photosAdapter.notifyDataSetChanged()
                        updateWidget(photosAdapter)
                    }
                )
        }
    }

    private fun updateWidget(photosAdapter: PhotosAdapter) {
        activity?.let {
            val intent = Intent(requireContext(), CameraMapsAppWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val ids = AppWidgetManager.getInstance(it.application)
                .getAppWidgetIds(ComponentName(it.application, CameraMapsAppWidget::class.java))

            val paths = photosAdapter.getLastTwoPhotosToUpdateWidget()
            paths?.apply {
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                intent.putStringArrayListExtra(PATHS, this)
                it.sendBroadcast(intent)
            }
        }
    }
}
