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

package com.faceapp.test.core.data.di

import android.net.Uri
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.faceapp.test.core.data.FaceAppRepository
import com.faceapp.test.core.data.FaceAppRepositoryImpl
import com.faceapp.test.core.data.FaceDataSource
import com.faceapp.test.core.data.domain.FaceResultModel
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsFaceAppRepository(
        faceAppRepository: FaceAppRepositoryImpl
    ): FaceAppRepository

}

class FakeFaceAppRepository @Inject constructor(
    private val faceDataSource: FaceDataSource
) : FaceAppRepository {



    override fun initialize(onFinished: (status: Boolean, error: Error?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun matchFaces(uri1: Uri, uri2: Uri, onFinished: (FaceResultModel) -> Unit) {
        TODO("Not yet implemented")
    }


}

val fakeFaceApps = listOf("One", "Two", "Three")
