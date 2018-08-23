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
import android.support.v4.app.FragmentActivity
import com.example.tsvetelinastoyanova.cameramapsapp.CameraMapsAppWidget
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.onClickLoadPhotoInGallery
import android.widget.Button
import android.widget.Toast
import com.example.tsvetelinastoyanova.cameramapsapp.gallery.visualization.Photo
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.vibrate

class GalleryFragment : Fragment(), GalleryContract.View {

    private var presenter: GalleryContract.Presenter? = null
    private var photosAdapter: PhotosAdapter? = null
    private var photoSelectedToDelete: Photo? = null
    private lateinit var recyclerView: RecyclerView

    /*** interface  ***/

    private lateinit var fragmentsLoader: FragmentsLoader

    interface FragmentsLoader {

        fun onClickToLoadMap()
        fun onClickToOpenCamera()
        fun onBucketButtonReady(button: Button)
    }

    /*** Methods from Contract ***/
    override fun showSuccessDeletingPhoto() {
        Toast.makeText(requireContext(), R.string.file_deleted_successfully, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorDeletingPhoto() {
        Toast.makeText(requireContext(), R.string.error_deleting_photo, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorLoadingPhotos() {
        Toast.makeText(requireContext(), R.string.error_loading_photos, Toast.LENGTH_SHORT).show()
    }

    override fun showNewPhotoByAddingItToAdapter(photo: Photo) {
        photosAdapter?.addNewPhoto(photo)
    }

    override fun showAllPhotosOnCompleteLoading() {
        photosAdapter?.let {
            it.notifyDataSetChanged()
            updateWidget(it)
        }
    }

    override fun setPresenter(presenter: GalleryContract.Presenter) {
        this.presenter = presenter
    }

    /*** Methods from Fragment  ***/
    companion object {
        fun newInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)
        createToolbar(view)
        createRecyclerView(view)
        loadPhotosInRecyclerView()
        setActionToBucketButton(view)
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
                if ((grantResults.isNotEmpty() && isPermissionGranted(grantResults[0]))) {
                    fragmentsLoader.onClickToLoadMap()
                }
                return
            }
            CAMERA_AND_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && isPermissionGranted(grantResults[0]))) {
                    if ((grantResults.size == 2 && isPermissionGranted(grantResults[1])) || grantResults.size == 1) {
                        fragmentsLoader.onClickToOpenCamera()
                    }
                }
            }
        }
    }

    private fun setActionToBucketButton(view: View) {
        val bucket = view.findViewById<Button>(R.id.bucket)
        fragmentsLoader.onBucketButtonReady(bucket)
        bucket.setOnClickListener {
            photoSelectedToDelete?.deletePhoto()
            changeVisibility(it as Button, View.INVISIBLE)
        }
    }

    private fun Photo.deletePhoto() {
        photosAdapter?.let {
            it.deletePhotoFromRecyclerView(this)
            updateWidget(it)
        }
        presenter?.deletePhotoFromMemory(this)
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
        if (!isPermissionGranted(Manifest.permission.CAMERA) &&
            !isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestNeededPermissions(arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION))
        } else if (!isPermissionGranted(Manifest.permission.CAMERA)) {
            requestNeededPermissions(arrayOf(Manifest.permission.CAMERA))
        } else if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestNeededPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        } else {
            fragmentsLoader.onClickToOpenCamera()
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ActivityCompat
            .checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionGranted(returnedCode: Int): Boolean {
        return returnedCode == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNeededPermissions(array: Array<String>) {
        Utils.requestPermissions(this,
            array, CAMERA_AND_LOCATION_REQUEST_CODE)
    }

    private fun checkPermissionToOpenMap() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Utils.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            fragmentsLoader.onClickToLoadMap()
        }
    }

    private fun createRecyclerView(view: View): PhotosAdapter? {
        this.recyclerView = view.findViewById(R.id.recyclerView)

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
            HEIGHT = height / 3, listener = { onClickLoadPhotoInGallery(it.file, requireActivity()) },
            longListener = { onLongClickListener(it);true }
        )
    }

    private fun onLongClickListener(photo: Photo) {
        vibrate(requireContext())
        photoSelectedToDelete = photo
        changeVisibility(requireActivity().findViewById(R.id.bucket), View.VISIBLE)
    }

    private fun changeVisibility(button: Button, visibility: Int) {
        button.visibility = visibility
    }

    private fun setRecyclerViewAttributes(photosAdapter: PhotosAdapter) {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = photosAdapter
    }

    private fun loadPhotosInRecyclerView() {
        presenter?.getPhotos(requireContext())
    }

    private fun updateWidget(photosAdapter: PhotosAdapter) {
        activity?.let {
            val intent = createIntent(it)
            val ids = getWidgetsIds(it)
            val pathsToPhotos = photosAdapter.getLastTwoPhotosToUpdateWidget()

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            intent.putStringArrayListExtra(PATHS, pathsToPhotos)
            it.sendBroadcast(intent)
        }
    }

    private fun getWidgetsIds(it: FragmentActivity): IntArray? {
        return AppWidgetManager.getInstance(it.application)
            .getAppWidgetIds(ComponentName(it.application, CameraMapsAppWidget::class.java))
    }

    private fun createIntent(context: Context): Intent {
        val intent = Intent(context, CameraMapsAppWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        return intent
    }
}
