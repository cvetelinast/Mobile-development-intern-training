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
import android.os.Bundle

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
            isAppDestroyed = false
            firstBitmap = createScaledBitmapFromPath(paths[0])
            if (paths.size >= 2) {
                secondBitmap = createScaledBitmapFromPath(paths[1])
            }
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager,
                                           appWidgetId: Int, newOptions: Bundle) {
        if (!isAppDestroyed) {
            updateAppWidgetOnSizeChanged(appWidgetManager, appWidgetId, context)
        }
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    companion object {
        var isAppDestroyed: Boolean = true
        var firstBitmap: Bitmap? = null
        var secondBitmap: Bitmap? = null
        var layoutId: Int? = null

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            val layoutIdTemp = if (layoutId == null) R.layout.widget_normal else layoutId ?: 0
            val views = RemoteViews(context.packageName, layoutIdTemp)
            setOnClickListeners(views, context)
            loadPhotos(views)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun loadPhotos(views: RemoteViews) {
            firstBitmap?.let { setImage(views, R.id.firstPhoto, it) }
            secondBitmap?.let { setImage(views, R.id.secondPhoto, it) }
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
            bitmap?.let { views.setImageViewBitmap(id, it) }
        }
    }

    private fun updateAppWidgetOnSizeChanged(appWidgetManager: AppWidgetManager, appWidgetId: Int, context: Context) {
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, minWidth, minHeight))
    }

    private fun getRemoteViews(context: Context, minWidth: Int, minHeight: Int): RemoteViews {
        val rows = getCellsForSize(minHeight)
        val columns = getCellsForSize(minWidth)

        val layoutIdTemp = choosePerfectSize(columns, rows)
        layoutId = layoutIdTemp
        val views = RemoteViews(context.packageName, layoutIdTemp)
        loadPhotos(views)
        setOnClickListeners(views, context)
        return views
    }

    private fun choosePerfectSize(columns: Int, rows: Int) = when (columns * rows) {
        1, 2, 3 -> R.layout.widget_small
        4, 5, 6 -> R.layout.widget_normal
        else -> R.layout.widget_large
    }

    private fun getCellsForSize(size: Int): Int {
        var n = 2
        while (70 * n - 30 < size) {
            ++n
        }
        return n - 1
    }

    private fun createScaledBitmapFromPath(path: String): Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        return Bitmap.createScaledBitmap(
            bitmap, bitmap.width / 8, bitmap.height / 8, false)
    }
}