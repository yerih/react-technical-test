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

package com.faceapp.test.feature.faceapp.ui.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.faceapp.test.core.data.FaceAppRepository
import com.faceapp.test.core.data.domain.FaceResultModel
import com.faceapp.test.feature.faceapp.ui.log
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
        faceAppRepository.initialize{ _, error ->
            _uiState.update { it.copy(isLoading = false, error = error) }
        }
    }


    fun matchFaces(){
        if(uri1 == null || uri2 == null){
            uri1?:log("bmp1 is null")
            uri2?:log("bmp2 is null")
            return
        }
        faceAppRepository.matchFaces(uri1!!, uri2!!){result ->
            result.error?.let{error ->
                _uiState.update { it.copy(resultModel = null, error = error) }
            }?:let {
                _uiState.update { it.copy(resultModel = result) }
            }
        }
    }


    fun captureBitmap(uri: Uri, isFirst: Boolean = true){
        if(isFirst) uri1 = uri else uri2 = uri
    }

    fun resetState() = _uiState.update { it.copy(resultModel = null, error = null) }

}



data class UiState(
    val isLoading: Boolean = true,
    val isInitialized: Boolean? = null,
    val error: Error? = null,
    val resultModel: FaceResultModel? = null,
)


