package com.example.wahyustoryapp.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.wahyustoryapp.makeFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object FileUtilityProcessing {
    private fun findMaxQuality(bitmap: Bitmap): Int {
        var quality = 100
        var streamLength: Int
        do {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val byteArray = baos.toByteArray()
            streamLength = byteArray.size
            quality -= 5
        } while (streamLength > 1000000)
        return quality
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val quality = findMaxQuality(bitmap)

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, FileOutputStream(file))
        return file
    }

    fun reduceFileImage(bitmap: Bitmap, application: Application): File {
        val quality = findMaxQuality(bitmap)
        val file = makeFile(application)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        val byteArray = baos.toByteArray()
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.flush()
        fos.close()

        return file
    }
}