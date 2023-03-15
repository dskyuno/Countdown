package com.project.timemanagerment.base.di


import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.project.timemanagerment.UserPreferences
import com.project.timemanagerment.app.dataStore
import com.project.timemanagerment.app.userPreferencesStore
import com.project.timemanagerment.base.constant.ConstantsPreferencesKey
import com.project.timemanagerment.base.datestore.DataStorePreferencesRepository
import com.project.timemanagerment.base.datestore.proto.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class BaseModule {
    @Singleton
    @Provides
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext


    @Singleton
    @Provides
    fun providesDataStorePreferences(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun providesUserPreferencesStore(@ApplicationContext context: Context): DataStore<UserPreferences> {
        return context.userPreferencesStore
    }

    @Singleton
    @Provides
    fun providesAppInfo(
        dataStorePreferencesRepository: DataStorePreferencesRepository
    ): AppInfo {
        var appUniqueId = dataStorePreferencesRepository.getStringPreferencesSync(
            ConstantsPreferencesKey.appUniqueId,
            ConstantsPreferencesKey.appUniqueId_default_value
        )
        if (appUniqueId.isEmpty()) {
            val randomId = UUID.randomUUID().toString()
            runBlocking {
                dataStorePreferencesRepository.setStringPreferences(
                    ConstantsPreferencesKey.appUniqueId,
                    randomId
                )
            }
            appUniqueId = randomId
        }
        val sysVersion = android.os.Build.VERSION.SDK_INT
        val phoneModel = android.os.Build.MODEL
        val phoneBrand = android.os.Build.BRAND
        return AppInfo(appUniqueId, sysVersion, phoneBrand, phoneModel)
    }

}