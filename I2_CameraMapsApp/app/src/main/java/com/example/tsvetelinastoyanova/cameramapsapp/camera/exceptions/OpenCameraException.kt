package com.example.tsvetelinastoyanova.cameramapsapp.camera.exceptions

import android.hardware.camera2.CameraDevice
import android.os.Build
import android.annotation.TargetApi
import android.support.annotation.Nullable


class OpenCameraException(@param:Nullable @field:Nullable
                          @get:Nullable
                          val reason: Reason) : Exception() {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    enum class Reason private constructor(private val mCameraErrorCode: Int) {
        ERROR_CAMERA_IN_USE(CameraDevice.StateCallback.ERROR_CAMERA_IN_USE),
        ERROR_MAX_CAMERAS_IN_USE(CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE),
        ERROR_CAMERA_DISABLED(CameraDevice.StateCallback.ERROR_CAMERA_DISABLED),
        ERROR_CAMERA_DEVICE(CameraDevice.StateCallback.ERROR_CAMERA_DEVICE),
        ERROR_CAMERA_SERVICE(CameraDevice.StateCallback.ERROR_CAMERA_SERVICE);


        companion object {

            @Nullable
            fun getReason(cameraErrorCode: Int): Reason? {
                for (reason in Reason.values()) {
                    if (reason.mCameraErrorCode == cameraErrorCode) {
                        return reason
                    }
                }
                return null
            }
        }
    }
}