package com.example.tsvetelinastoyanova.cameramapsapp.camera.surfacetexturelistener

import android.graphics.SurfaceTexture
import android.view.TextureView

interface CustomSurfaceTextureListener : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        return false
    }
}