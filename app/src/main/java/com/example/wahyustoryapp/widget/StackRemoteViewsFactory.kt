package com.example.wahyustoryapp.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.wahyustoryapp.R
import com.example.wahyustoryapp.data.database.StoryDao
import com.example.wahyustoryapp.data.database.StoryRoomDatabase

class StackRemoteViewsFactory(private val context: Context, private val app: Application) :
    RemoteViewsService.RemoteViewsFactory {

    private var dao: StoryDao
    private var mBitmap = mutableListOf<Bitmap>()

    init {
        val db = StoryRoomDatabase.getInstance(app)
        dao = db.storyDao()
    }

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val data = dao.getAllStoriesValues() ?: listOf()
        for (mStory in data) {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(mStory.photoUrl)
                .submit(512, 512)
                .get()

            mBitmap.add(bitmap)
        }
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return mBitmap.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.image_widget, mBitmap[position])

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.image_widget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}