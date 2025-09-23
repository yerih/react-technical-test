

package com.faceapp.test.core.data

import android.net.Uri
import com.faceapp.test.core.data.domain.FaceResultModel
import javax.inject.Inject

interface FaceAppRepository {

    fun initialize(onFinished: (status: Boolean, error: Error?)->Unit)

    fun matchFaces(uri1: Uri, uri2: Uri, onFinished: (FaceResultModel)->Unit)
}

class FaceAppRepositoryImpl @Inject constructor(
    private val faceDataSource: FaceDataSource
): FaceAppRepository{

    override fun initialize(onFinished: (status: Boolean, error: Error?) -> Unit) = faceDataSource.initialize(onFinished)
    override fun matchFaces(uri1: Uri, uri2: Uri, onFinished: (FaceResultModel) -> Unit) = faceDataSource.matchFaces(
        uri1,
        uri2,
        onFinished
    )


}

