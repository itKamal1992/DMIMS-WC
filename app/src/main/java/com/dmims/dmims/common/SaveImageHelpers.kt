package com.dmims.dmims.common

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import java.lang.ref.WeakReference

class SaveImageHelpers (
    private val context: Context,
    alertDialog: AlertDialog,
    contentResolver: ContentResolver,
    private val name: String,
    private val desc: String
) : Target {
    private val alertDialogWeakReference: WeakReference<AlertDialog>
    private val contentResolverWeakReference: WeakReference<ContentResolver>

    init {
        this.alertDialogWeakReference = WeakReference(alertDialog)
        this.contentResolverWeakReference = WeakReference(contentResolver)
    }

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        val resolver = contentResolverWeakReference.get()
        val dialog = alertDialogWeakReference.get()
        if (resolver != null) {
            MediaStore.Images.Media.insertImage(resolver, bitmap, name, desc)
            dialog!!.dismiss()

            // Code to open Gallary
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            context.startActivity(Intent.createChooser(intent, "VIEW PICTURE"))
        }

    }

    override fun onBitmapFailed(errorDrawable: Drawable) {

    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable) {

    }
}
