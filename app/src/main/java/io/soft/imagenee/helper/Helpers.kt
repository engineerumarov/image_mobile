package io.soft.imagenee.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.InputStream

fun getBitmapFromUri(contentResolver: ContentResolver, uri: Uri): Bitmap? {
    var inputStream: InputStream? = null
    try {
        inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        inputStream?.close()
    }
    return null
}

fun Bitmap.createFile(context: Context, fileName: String = "upload.png"): File {
    return File(context.externalCacheDir, fileName).apply {
        createNewFile()
        outputStream().use {
            this@createFile.compress(
                Bitmap.CompressFormat.PNG,
                80,
                it
            )
        }
    }
}