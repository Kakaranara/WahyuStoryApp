package com.example.wahyustoryapp

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT, Locale.US
).format(System.currentTimeMillis())



fun makeFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory =
        if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir


    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }
}


fun findMaxQuality(bitmap: Bitmap): Int {
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

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val file = createCustomTempFile(context)
    val inputStream: InputStream = contentResolver.openInputStream(selectedImg)!!
    val outputStream: OutputStream = FileOutputStream(file)
    val byteArray = ByteArray(1024)
    var len: Int
    while (inputStream.read(byteArray).also { len = it } > 0) {
        outputStream.write(byteArray, 0, len)
    }

    outputStream.close()
    inputStream.close()

    return file
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}