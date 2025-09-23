package com.faceapp.test.core.data.domain


data class FaceResultModel(
    val similarity: String,
    val detections: String = "",
    val error: Error? = null,
)


