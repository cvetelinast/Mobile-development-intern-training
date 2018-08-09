package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository
import android.support.v4.app.ActivityCompat
import android.hardware.camera2.CameraCaptureSession
import android.view.Surface
import android.hardware.camera2.CaptureRequest
import io.reactivex.subjects.PublishSubject
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread
import android.view.TextureView
import android.util.Size
import java.util.*

class CameraPresenter(private val view: CameraContract.View, private val repository: Repository) : CameraContract.Presenter {

    private val CAMERA_REQUEST_CODE: Int = 1888

    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    private val onSurfaceTextureAvailable = PublishSubject.create<SurfaceTexture>()
    private lateinit var previewSize: Size
    private lateinit var stateCallback: CameraDevice.StateCallback
    private var cameraDevice: CameraDevice? = null
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    private var backgroundHandler: Handler? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var cameraId: String = ""


    override fun start() {
        Log.d("tag", "Camera Presenter started")
    }

    override fun initTextureView(textureView: TextureView) {
        this.textureView = textureView
    }

    override fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun initCameraManager(context: Context) {
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun setSurfaceTextureListener() {
        surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
                onSurfaceTextureAvailable.onNext(surfaceTexture)
            }

            override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {

            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {

            }
        }
    }

    override fun initStateCallback() {
        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice) {
                this@CameraPresenter.cameraDevice = cameraDevice
                createPreviewSession()
            }

            override fun onDisconnected(cameraDevice: CameraDevice) {
                cameraDevice.close()
                this@CameraPresenter.cameraDevice = null
            }

            override fun onError(cameraDevice: CameraDevice, error: Int) {
                cameraDevice.close()
                this@CameraPresenter.cameraDevice = null
            }
        }
    }

    override fun onTakePhotoButtonClicked(context: Context) {
        repository.savePhoto(context, textureView.bitmap)
            .subscribe(
                { success -> Log.d("tag", "File saved: $success") },
                { error -> Log.d("tag", "Error: $error") }
            )
    }

    override fun calledInOnResume(context: Context) {
        openBackgroundThread()
        if (textureView.isAvailable) {
            setUpCamera()
            openCamera(context)
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    private fun openBackgroundThread() {
        val backgroundThread = HandlerThread("camera_background_thread")
        backgroundThread.start()
        backgroundHandler = Handler(backgroundThread.looper)
    }

    private fun setUpCamera() {
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    val streamConfigurationMap = cameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    previewSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)[0]
                    this.cameraId = cameraId
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera(context: Context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun createPreviewSession() {
        try {
            val previewSurface = extractPreviewSurface()
            captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            captureRequestBuilder?.addTarget(previewSurface)

            cameraDevice?.createCaptureSession(Collections.singletonList(previewSurface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (cameraDevice == null) {
                            return
                        }
                        val captureRequest = captureRequestBuilder?.build()
                        cameraCaptureSession.setRepeatingRequest(captureRequest, null, backgroundHandler)
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {}
                }, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun extractPreviewSurface(): Surface {
        val surfaceTexture = textureView.surfaceTexture
        surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
        return Surface(surfaceTexture)
    }

}