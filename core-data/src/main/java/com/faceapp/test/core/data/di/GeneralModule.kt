package com.faceapp.test.core.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GeneralModule {

    @Provides
    @Singleton
    fun provideAppContext(@ApplicationContext context: Context): Context = context
}



