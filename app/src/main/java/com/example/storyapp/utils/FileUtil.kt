package com.example.storyapp.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtil {
    fun getFileFromUri(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream: InputStream = contentResolver.openInputStream(uri)!!
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.copyTo(outputStream)
        outputStream.close()
        inputStream.close()

        return tempFile
    }
}
