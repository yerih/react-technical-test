package com.faceapp.test.feature.faceapp.ui.datasources

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import com.faceapp.test.core.data.FaceDataSource
import com.faceapp.test.core.data.domain.FaceResultModel
import com.faceapp.test.core.data.domain.loadBitmapFromUri
import com.faceapp.test.feature.faceapp.ui.log
import com.faceapp.test.feature.faceapp.ui.toPercentage
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
import javax.inject.Inject


class FaceDataSourceImpl @Inject constructor(
    private val context: Context,
): FaceDataSource{
    override fun initialize(onFinished: (Boolean, Error?) -> Unit) {
        val license: ByteArray? = RegulaLicense.getLicense(context)
        license?.let{
            Log.i("TGB", "license != null")
            val configuration = InitializationConfiguration.Builder(license).setLicenseUpdate(false).build()
            FaceSDK.Instance().initialize(context, configuration,
                FaceInitializationCompletion { status, exception ->
                    log( "status = ${status}, exception = $exception")
                    onFinished(status, if(status) null else Error(exception.message))
                })
        }?: log( "license null")
    }

    override fun deinitialize() {
        FaceSDK.Instance().deinitialize()
    }

    override fun matchFaces(uri1: Uri, uri2: Uri, onFinished: (FaceResultModel) -> Unit) {
        val bmp1 = loadBitmapFromUri(context, uri1!!)
        val bmp2 = loadBitmapFromUri(context, uri2!!)
        log("TGB", "matchFace: bmp1 = ${bmp1.toString()}")
        log("TGB", "matchFace: bmp2 = ${bmp2.toString()}")
        var faceResult: FaceResultModel?
        val imgA = MatchFacesImage(bmp1, ImageType.PRINTED)
        val imgB = MatchFacesImage(bmp2, ImageType.PRINTED)
        val matchFacesRequest = MatchFacesRequest(arrayListOf(imgA, imgB))
        val crop = OutputImageCrop(OutputImageCropAspectRatio.OUTPUT_IMAGE_CROP_ASPECT_RATIO_3X4)
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
                    log( "similarity = ${similarity.toPercentage()}")
                    faceResult = FaceResultModel(similarity = similarity.toPercentage())
                }
                response.exception != null -> {
                    log( "similarity exception= ${response.exception.message}")
                    faceResult = FaceResultModel(error = Error(response.exception!!.message), similarity = "")
                    onFinished(faceResult!!)
                    return@matchFaces
                }
                else -> {
                    log( "similarity = null")
                }
            }

            val faceBitmaps = arrayListOf<Bitmap>()
            for(matchFaces in response.detections) {
                for (face in matchFaces.faces)
                    face.crop?.let { faceBitmaps.add(it) }
            }

            val l = faceBitmaps.size
            faceResult = FaceResultModel(similarity = similarity?.toPercentage()?:"0%", detections = "$l")
            onFinished(faceResult!!)
        }
    }


}

