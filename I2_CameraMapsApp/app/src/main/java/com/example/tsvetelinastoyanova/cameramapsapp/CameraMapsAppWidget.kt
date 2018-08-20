package com.example.tsvetelinastoyanova.cameramapsapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_CAMERA_FRAGMENT
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_GALLERY_FRAGMENT
import com.example.tsvetelinastoyanova.cameramapsapp.utils.Utils.OPEN_MAPS_FRAGMENT
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent.getIntent


class CameraMapsAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val paths = intent?.extras?.getStringArrayList(Utils.PATHS)
        if (paths != null && paths.isNotEmpty()) {

            val first = BitmapFactory.decodeFile(paths[0])
            val second = BitmapFactory.decodeFile(paths[1])

            firstBitmap = Bitmap.createScaledBitmap(
                first, first.width / 8, first.height / 8, false)
            secondBitmap = Bitmap.createScaledBitmap(
                second, second.width / 8, second.height / 8, false)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        var firstBitmap: Bitmap? = null
        var secondBitmap: Bitmap? = null

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.my_widget)
            setOnClickListeners(views, context)
            setImage(views, R.id.firstPhoto, firstBitmap)
            setImage(views, R.id.secondPhoto, secondBitmap)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun setOnClickListeners(views: RemoteViews, context: Context) {
            views.setOnClickPendingIntent(R.id.map, getPendingIntent(context, 0,
                OPEN_MAPS_FRAGMENT))
            views.setOnClickPendingIntent(R.id.camera, getPendingIntent(context, 0,
                OPEN_CAMERA_FRAGMENT))
            views.setOnClickPendingIntent(R.id.more, getPendingIntent(context, 0,
                OPEN_GALLERY_FRAGMENT))
        }

        private fun getPendingIntent(context: Context, value: Int, actionConstant: String)
            : PendingIntent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.action = actionConstant
            return PendingIntent.getActivity(context, value, intent, 0)
        }

        private fun setImage(views: RemoteViews, id: Int, bitmap: Bitmap?) {
            if (bitmap != null) {
                views.setImageViewBitmap(id, bitmap)
            }
        }
    }
}

