package com.example.tsvetelinastoyanova.cameramapsapp.camera

import android.app.Activity
import android.content.Context
import android.view.TextureView
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BasePresenter
import com.example.tsvetelinastoyanova.cameramapsapp.abstraction.BaseView

interface CameraContract {

    interface View : BaseView<Presenter> {

        fun showErrorOpeningTheCamera()

        fun showErrorMessageForSavingFile()

        fun showProgressBar()

        fun hideProgressBar()
    }

    interface Presenter : BasePresenter {

        fun init(context: Context)

        fun setTextureView(textureView: TextureView)

        fun startCameraIfPossible(activity: Activity)

        fun onTakePhotoButtonClicked(activity: Activity)
    }
}