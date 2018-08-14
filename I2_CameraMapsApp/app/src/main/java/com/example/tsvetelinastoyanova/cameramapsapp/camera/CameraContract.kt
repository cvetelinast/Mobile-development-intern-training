package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.app.Activity
import android.content.Context
import android.view.TextureView
import com.example.tsvetelinastoyanova.weatherapp.BasePresenter
import com.example.tsvetelinastoyanova.weatherapp.BaseView

interface CameraContract {

    interface View : BaseView<Presenter> {

        fun showFileSavedMessage()
    }

    interface Presenter : BasePresenter {

        fun initTextureView(textureView: TextureView)

        fun requestPermission(activity: Activity, array: Array<String>, code: Int)

        fun initCameraManager(context: Context)

        fun setSurfaceTextureListener()

        fun initStateCallback()

        fun calledInOnResume(context: Context)

        fun onTakePhotoButtonClicked(activity: Activity)
    }
}