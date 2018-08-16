package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.location.Location
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import com.example.tsvetelinastoyanova.cameramapsapp.camera.surfacetexturelistener.CustomSurfaceTextureListener
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.CAMERA_BACKGROUND_THREAD_NAME
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class CameraPresenter(private val view: CameraContract.View, private val repository: Repository)
    : CameraContract.Presenter {

    private lateinit var cameraManager: CameraManager
    private lateinit var textureView: TextureView
    private lateinit var stateCallback: CameraDevice.StateCallback

    private var backgroundHandler: Handler? = null
    private var previewSize: Size? = null
    private var cameraDevice: CameraDevice? = null

    override fun start() {
        Log.d("tag", "Camera Presenter started")
    }

    override fun init(context: Context) {
        initCameraManager(context)
        initStateCallback()
    }

    override fun setTextureView(textureView: TextureView) {
        this.textureView = textureView
    }

    override fun startCameraIfPossible(activity: Activity) {
        openBackgroundThread()
        textureView.surfaceTextureListener = setSurfaceTextureListener(activity)
    }

    override fun onTakePhotoButtonClicked(activity: Activity) {
        view.showProgressBar()
        initLocation(activity)
    }

    private fun initCameraManager(context: Context) {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private fun setSurfaceTextureListener(activity: Activity): CustomSurfaceTextureListener {
        return object : CustomSurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                getCameraId()?.let { openCamera(activity, it) }
            }
        }
    }

    private fun initStateCallback() {
        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                this@CameraPresenter.cameraDevice = cameraDevice
                createPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                destroyCameraDevice()
                quitBackgroundHandler()
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                destroyCameraDevice()
                quitBackgroundHandler()
            }
        }
    }

    private fun openBackgroundThread() {
        val backgroundThread = HandlerThread(CAMERA_BACKGROUND_THREAD_NAME)
        backgroundThread.start()
        backgroundHandler = Handler(backgroundThread.looper)
    }

    private fun getCameraId(): String? {
        var returnedCameraId: String? = null
        for (cameraId in cameraManager.cameraIdList) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                previewSize = chooseTheBestSizeForCameraPreview(cameraCharacteristics)
                returnedCameraId = cameraId
            }
        }
        return returnedCameraId
    }

    private fun chooseTheBestSizeForCameraPreview(cameraCharacteristics: CameraCharacteristics): Size? {
        val streamConfigurationMap = cameraCharacteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        return streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)?.maxBy { it.height }
    }

    private fun openCamera(activity: Activity, cameraId: String) {
        try {
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            view.showErrorOpeningTheCamera()
            e.printStackTrace()
        }
    }

    private fun initLocation(activity: Activity) {
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            var location: Location? = null
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful && task.result != null) {
                    location = task.result
                }
                savePhoto(activity, location)
            }
        }
    }

    private fun savePhoto(activity: Activity, location: Location?) {
        repository.savePhoto(activity, textureView.bitmap, location)
            .subscribe(
                { _ -> view.hideProgressBar() },
                { _ -> view.showErrorMessageForSavingFile() }
            )
    }

    private fun createPreviewSession() {
        val previewSurface = extractPreviewSurface()
        val captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder?.addTarget(previewSurface)

        createRepeatingCaptureSession(previewSurface, captureRequestBuilder)
    }

    private fun createRepeatingCaptureSession(previewSurface: Surface, captureRequestBuilder: CaptureRequest.Builder?) {
        cameraDevice?.createCaptureSession(Collections.singletonList(previewSurface),
            object : CameraCaptureSession.StateCallback() {

                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    captureRequestBuilder?.build()?.let {
                        cameraCaptureSession.setRepeatingRequest(it, null, backgroundHandler)
                    }
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {}
            }, backgroundHandler)
    }

    private fun extractPreviewSurface(): Surface {
        val surfaceTexture = textureView.surfaceTexture
        previewSize?.let { surfaceTexture.setDefaultBufferSize(it.width, it.height) }
        return Surface(surfaceTexture)
    }

    private fun destroyCameraDevice() {
        cameraDevice = null
    }

    private fun quitBackgroundHandler() {
        backgroundHandler?.looper?.quitSafely()
    }

}