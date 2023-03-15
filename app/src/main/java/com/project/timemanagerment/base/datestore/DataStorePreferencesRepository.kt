package com.project.timemanagerment.base.datestore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow


interface DataStorePreferencesRepository {

    suspend fun setIntPreferences(
        preferences: Preferences.Key<Int>,
        value: Int
    )

    fun getIntPreferences(preferences: Preferences.Key<Int>, defaultValue: Int = 0): Flow<Int>
    fun getIntPreferencesSync(preferences: Preferences.Key<Int>, defaultValue: Int = 0): Int

    suspend fun setStringPreferences(
        preferences: Preferences.Key<String>,
        value: String
    )

    fun getStringPreferences(
        preferences: Preferences.Key<String>,
        defaultValue: String = ""
    ): Flow<String>

    fun getStringPreferencesSync(
        preferences: Preferences.Key<String>,
        defaultValue: String = ""
    ): String

    suspend fun setBooleanPreferences(
        preferences: Preferences.Key<Boolean>,
        boolean: Boolean
    )

    fun getBooleanPreferences(
        preferences: Preferences.Key<Boolean>,
        defaultValue: Boolean = false
    ): Flow<Boolean>

    fun getBooleanPreferencesSync(
        preferences: Preferences.Key<Boolean>,
        defaultValue: Boolean = false
    ): Boolean
}