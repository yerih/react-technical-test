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
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faceapp.test.RegulaLicense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.faceapp.test.core.data.FaceAppRepository
import com.faceapp.test.feature.faceapp.ui.FaceAppUiState.Loading
import com.faceapp.test.feature.faceapp.ui.FaceAppUiState.Success
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.callback.FaceInitializationCompletion
import com.regula.facesdk.configuration.InitializationConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UiState(
    val isLoading: Boolean = true,
    val isInitialized: Boolean? = null,
    val error: Error? = null,
)




@HiltViewModel
class FaceAppViewModel @Inject constructor(
    private val faceAppRepository: FaceAppRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    var uiState = _uiState.asStateFlow()



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


}

sealed interface FaceAppUiState {
    object Loading : FaceAppUiState
//    data class Error(val throwable: Throwable) : FaceAppUiState
    data class Success(val data: List<String>) : FaceAppUiState
}
