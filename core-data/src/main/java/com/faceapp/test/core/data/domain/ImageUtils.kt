package com.faceapp.test.core.data.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


fun saveTempBitmap(
    context: Context,
    bitmap: Bitmap,
    prefixName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 90
): TempImageModel {
    val tempFile = File.createTempFile(prefixName, ".jpg", context.cacheDir).apply {
        outputStream().use { os -> bitmap.compress(format, quality, os) }
    }
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
    return TempImageModel(uri = uri, file = tempFile)
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return context.contentResolver.openInputStream(uri)?.use { ins ->
        BitmapFactory.decodeStream(ins)
    }
}

fun clearTemp(file: File?): Boolean {
    if (file == null) return false
    return runCatching {
        if (file.exists()) file.delete() else true
    }.getOrDefault(false)
}

fun clearTemp(path: String?): Boolean {
    if (path.isNullOrEmpty()) return false
    return clearTemp(File(path))
}