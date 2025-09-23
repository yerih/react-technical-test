package com.faceapp.test.feature.faceapp.ui.datasources

import android.content.Context
import com.faceapp.test.feature.faceapp.R
import java.io.IOException


object RegulaLicense {
    fun getLicense(context: Context?): ByteArray? {
        if (context == null) return null
        val licInput = context.resources.openRawResource(R.raw.regula)
        val available: Int = try {
            licInput.available()
        } catch (e: IOException) {
            return null
        }
        val license = ByteArray(available)
        try {
            licInput.read(license)
        } catch (e: IOException) {
            return null
        }
        return license
    }
}