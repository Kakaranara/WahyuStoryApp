package com.example.wahyustoryapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.example.wahyustoryapp.R

/**
 * Implementation of App Widget functionality.
 */
class StoryWidget : AppWidgetProvider() {

    companion object {
        //        private const val TOAST_ACTION = "com.dicoding.picodiploma.TOAST_ACTION"
        private const val TOAST_ACTION = "TOAST_ACTION"
        const val EXTRA_ITEM = "EXTRAS"


        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.story_widget)
            views.setRemoteAdapter(R.id.stackView, intent)
            views.setEmptyView(R.id.stackView, R.id.empty_view)

            val toastIntent = Intent(context, StoryWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            val flags = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                }
                else -> 0
            }

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, flags)
            views.setPendingIntentTemplate(R.id.stackView, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Klik $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
//
//internal fun updateAppWidget(
//    context: Context,
//    appWidgetManager: AppWidgetManager,
//    appWidgetId: Int
//) {
////    val widgetText = context.getString(R.string.appwidget_text)
////    // Construct the RemoteViews object
////    val views = RemoteViews(context.packageName, R.layout.story_widget)
////    views.setTextViewText(R.id.appwidget_text, widgetText)
////
////    // Instruct the widget manager to update the widget
////    appWidgetManager.updateAppWidget(appWidgetId, views)
//}