package com.faceapp.test.feature.faceapp.ui.di

import android.content.Context
import com.faceapp.test.core.data.FaceDataSource
import com.faceapp.test.feature.faceapp.ui.datasources.FaceDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {

    @Provides
    @Singleton
    fun provideFaceDataSource(@ApplicationContext context: Context): FaceDataSource = FaceDataSourceImpl(context)

}



