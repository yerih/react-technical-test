package com.faceapp.test.core.data

import android.net.Uri
import com.faceapp.test.core.data.domain.FaceResultModel

interface FaceDataSource {
    fun initialize(onFinished: (Boolean, Error?)->Unit)

    fun matchFaces(uri1: Uri, uri2: Uri, onFinished: (FaceResultModel) -> Unit)

}