package com.yashagozwan.mystorynew.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import com.yashagozwan.mystorynew.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private fun timeStamp(): String {
        val fileDateFormat = "dd-MMM-yyyy"
        return SimpleDateFormat(fileDateFormat, Locale.US).format(System.currentTimeMillis())
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "${timeStamp()}.jpg")
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // flip gambar
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}