/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.faceapp.test.feature.faceapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.faceapp.test.RegulaLicense
import dagger.hilt.android.lifecycle.HiltViewModel
import com.faceapp.test.core.data.FaceAppRepository
import com.faceapp.test.toPercentage
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.callback.FaceInitializationCompletion
import com.regula.facesdk.configuration.InitializationConfiguration
import com.regula.facesdk.detection.request.OutputImageCrop
import com.regula.facesdk.detection.request.OutputImageParams
import com.regula.facesdk.enums.ImageType
import com.regula.facesdk.enums.OutputImageCropAspectRatio
import com.regula.facesdk.model.MatchFacesImage
import com.regula.facesdk.model.results.matchfaces.MatchFacesSimilarityThresholdSplit
import com.regula.facesdk.request.MatchFacesRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject




@HiltViewModel
class FaceAppViewModel @Inject constructor(
    private val faceAppRepository: FaceAppRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    var uiState = _uiState.asStateFlow()
    var uri1: Uri? = null
    var uri2: Uri? = null



    init {
        _uiState.update { it.copy(isLoading = true) }
        val license: ByteArray? = RegulaLicense.getLicense(context)
        license?.let{
            Log.i("TGB", "license != null")
            val configuration = InitializationConfiguration.Builder(license).setLicenseUpdate(false).build()
            FaceSDK.Instance().initialize(context, configuration,
                FaceInitializationCompletion { status, exception ->
                    _uiState.update {
                        if(status) it.copy(isLoading = false, error = null)
                        else it.copy(isLoading = true, error = Error(exception.message))
                    }
                    Log.i("TGB", "status = ${status}, exception = $exception")
            })
        }?:let{
            Log.i("TGB", "license null")
        }
    }


    fun matchFaces(){
        if(uri1 == null || uri2 == null){
            uri1?:run{ Log.i("TGB", "bmp1 is null")}
            uri2?:run{ Log.i("TGB", "bmp2 is null")}
            return
        }
        val bmp1 = loadBitmapFromUri(context, uri1!!)
//        Log.i("TGB", "matchFace: bmp1 = ${bmp1.toString()}")
        val bmp2 = loadBitmapFromUri(context, uri2!!)
//        Log.i("TGB", "matchFace: bmp2 = ${bmp2.toString()}")

        val imgA = MatchFacesImage(bmp1, ImageType.PRINTED)
        val imgB = MatchFacesImage(bmp2, ImageType.PRINTED)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(imgA, imgB))

        val crop = OutputImageCrop(
            OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_3X4
        )
        matchFacesRequest.outputImageParams = OutputImageParams(crop, Color.WHITE)

        FaceSDK.Instance().matchFaces(context, matchFacesRequest){response ->
            val split = MatchFacesSimilarityThresholdSplit(response.results, 0.75)
            val similarity = when {
                split.matchedFaces.size > 0   -> split.matchedFaces[0].similarity
                split.unmatchedFaces.size > 0 -> split.unmatchedFaces[0].similarity
                else -> null
            }

            when{
                similarity != null -> {
                    Log.i("TGB", "similarity = ${similarity.toPercentage()}")
                    _uiState.update { it.copy(resultModel = FaceResultModel(similarity = similarity.toPercentage())) }
                }
                response.exception != null -> {
                    Log.i("TGB", "similarity exception= ${response.exception.message}")
                    _uiState.update { state -> state.copy(resultModel = null, error = Error(response.exception.message)) }
                    return@matchFaces
                }
                else -> {
                    Log.i("TGB", "similarity = null")
                }
            }


            val faceBitmaps = arrayListOf<Bitmap>()

            for(matchFaces in response.detections) {
                for (face in matchFaces.faces)
                    face.crop?.let { faceBitmaps.add(it) }
            }

            val l = faceBitmaps.size
            _uiState.update { it.copy(resultModel = FaceResultModel(similarity = similarity?.toPercentage()?:"", detections = "$l")) }

        }
    }


    fun captureBitmap(uri: Uri, isFirst: Boolean = true){
//        Log.i("TGB","captureBitmap: number = ${if(isFirst)1 else 2}")
        if(isFirst) uri1 = uri else uri2 = uri
    }

    fun resetState() = _uiState.update { it.copy(resultModel = null) }

}



data class UiState(
    val isLoading: Boolean = true,
    val isInitialized: Boolean? = null,
    val error: Error? = null,
    val resultModel: FaceResultModel? = null,
)


