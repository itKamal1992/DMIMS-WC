package com.dmims.dmims.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask

import java.io.IOException
import java.io.InputStream
import java.net.URL

class LoadImageTask(private val mListener: Listener) : AsyncTask<String, Void, Bitmap>() {

    interface Listener {
        fun onImageLoaded(bitmap: Bitmap)
        fun onError()
    }

    override fun doInBackground(vararg args: String): Bitmap? {
        try {
            return BitmapFactory.decodeStream(URL(args[0]).content as InputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if (bitmap != null) {
            mListener.onImageLoaded(bitmap)
        } else {
            mListener.onError()
        }
    }
}
