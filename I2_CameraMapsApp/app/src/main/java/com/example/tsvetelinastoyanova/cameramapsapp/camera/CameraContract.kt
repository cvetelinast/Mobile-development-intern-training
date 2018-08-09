package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.app.Activity
import android.content.Context
import android.view.TextureView
import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface CameraContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

        fun initTextureView(textureView: TextureView)

        fun requestPermissions(activity: Activity)

        fun initCameraManager(context: Context)

        fun setSurfaceTextureListener()

        fun initStateCallback()

        fun calledInOnResume(context: Context)

        fun onTakePhotoButtonClicked(context: Context)
    }
}