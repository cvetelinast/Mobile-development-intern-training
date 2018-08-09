package com.example.tsvetelinastoyanova.cameramapsapp.camera.exceptions

import android.hardware.camera2.CameraCaptureSession

class CreateCaptureSessionException(val session: CameraCaptureSession) : Exception()