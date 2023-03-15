package com.project.timemanagerment.base.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/*
class ToastUtil {
    private var toast: Toast? = null
    fun showMsg(@ApplicationContext context: Context, msg: String) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast?.let {
                it.cancel()
            }
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toast?.duration = Toast.LENGTH_SHORT
        }
        toast?.show()
    }

}*/

fun deleteImageByUrl(imageUrl: String, context: Context) {
    val fileToDelete = File(imageUrl)
    if (fileToDelete.exists()) {
        if (fileToDelete.delete()) {
            if (fileToDelete.exists()) {
                fileToDelete.canonicalFile.delete()
                if (fileToDelete.exists()) {
                    context.applicationContext.deleteFile(fileToDelete.name)
                }
            }
            Log.e("File Deleted ", imageUrl)
        } else {
            Log.e("File not Deleted ", imageUrl)
        }
    }
}
//public storage
@Throws(IOException::class)
fun saveBitmap(
    context: Context, bitmap: Bitmap, format: Bitmap.CompressFormat,
    mimeType: String, displayName: String
): Uri {

    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
    }

    val resolver = context.contentResolver
    var uri: Uri? = null

    try {
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            ?: throw IOException("Failed to create new MediaStore record.")

        resolver.openOutputStream(uri)?.use {
            if (!bitmap.compress(format, 95, it))
                throw IOException("Failed to save bitmap.")
        } ?: throw IOException("Failed to open output stream.")

        return uri

    } catch (e: IOException) {

        uri?.let { orphanUri ->
            // Don't leave an orphan entry in the MediaStore
            resolver.delete(orphanUri, null, null)
        }

        throw e
    }
}


fun saveImageToCacheByBitmap(bitmap: Bitmap, context: Context): String {
    val dirPath = context.cacheDir.path;
    val imageFolder = "images"
    //create image folder
    var folderFile = File(dirPath, imageFolder)
    if (!folderFile.exists()) {
        folderFile.mkdir()
        folderFile.createNewFile()
    }
    val imageName = Date().time.toString() + ".png"
    var file = File(folderFile, imageName)
    var fileOutputStream = FileOutputStream(file)
    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fileOutputStream)
        fileOutputStream.flush()

    } catch (e: IOException) {
        e.printStackTrace()
    } finally {

        try {
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return file.absolutePath
}


fun saveImageByBitmap(bitmap: Bitmap, context: Context): String {
    val dirPath = context.filesDir.path;
    val imageFolder = "images"
    //create image folder
    var folderFile = File(dirPath, imageFolder)
    if (!folderFile.exists()) {
        folderFile.mkdir()
        folderFile.createNewFile()
    }
    val imageName = Date().time.toString() + ".jpg"
    var file = File(folderFile, imageName)
    var fileOutputStream = FileOutputStream(file)
    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
        fileOutputStream.flush()

    } catch (e: IOException) {
        e.printStackTrace()
    } finally {

        try {
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return file.absolutePath
}