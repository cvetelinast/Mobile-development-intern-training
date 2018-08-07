package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.util.Log
import com.example.tsvetelinastoyanova.cameramapsapp.data.Repository

class CameraPresenter(private val view: CameraContract.View, private val repository: Repository) : CameraContract.Presenter {
    override fun start() {
        Log.d("tag", "Camera Presenter started")
    }
}