package com.example.tsvetelinastoyanova.cameramapsapp.camera.exceptions

import android.hardware.camera2.CaptureFailure

class CameraCaptureFailedException(val mFailure: CaptureFailure) : Exception()