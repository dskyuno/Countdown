package com.project.timemanagerment.base.di

import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepositoryImpl
import com.project.timemanagerment.base.datestore.proto.UserPreferencesRepository
import com.project.timemanagerment.base.datestore.proto.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BaseModuleAbstract {
    @Binds
    @Singleton
    abstract fun providesDataStorePreferencesRepository(dataStorePreferencesRepositoryImpl: DataStorePreferencesRepositoryImpl): DataStorePreferencesRepository

    @Binds
    @Singleton
    abstract fun provideUserPreferenceRepository(userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl): UserPreferencesRepository
}