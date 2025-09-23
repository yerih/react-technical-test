package com.faceapp.test.core.data.domain

import android.net.Uri
import java.io.File


data class TempImageModel(
    val uri: Uri,
    val file: File,        // keep this to delete later
    val absolutePath: String = file.absolutePath
)



