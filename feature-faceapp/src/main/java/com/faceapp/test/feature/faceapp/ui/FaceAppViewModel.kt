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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.faceapp.test.core.data.FaceAppRepository
import com.faceapp.test.feature.faceapp.ui.FaceAppUiState.Error
import com.faceapp.test.feature.faceapp.ui.FaceAppUiState.Loading
import com.faceapp.test.feature.faceapp.ui.FaceAppUiState.Success
import javax.inject.Inject

@HiltViewModel
class FaceAppViewModel @Inject constructor(
    private val faceAppRepository: FaceAppRepository
) : ViewModel() {

    val uiState: StateFlow<FaceAppUiState> = faceAppRepository
        .faceApps.map<List<String>, FaceAppUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun launchPermissionRequest() {
        viewModelScope.launch {
//            faceAppRepository.add()
        }
    }
}

sealed interface FaceAppUiState {
    object Loading : FaceAppUiState
    data class Error(val throwable: Throwable) : FaceAppUiState
    data class Success(val data: List<String>) : FaceAppUiState
}
