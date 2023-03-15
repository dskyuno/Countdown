package com.project.timemanagerment.base.datestore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DataStorePreferencesRepositoryImpl @Inject constructor(val dataStore: DataStore<Preferences>) :
    DataStorePreferencesRepository {
    override suspend fun setIntPreferences(preferencesKey: Preferences.Key<Int>, value: Int) {
        dataStore.edit { setting ->
            setting[preferencesKey] = value
        }
    }

    override fun getIntPreferences(
        preferencesKey: Preferences.Key<Int>,
        defaultValue: Int
    ): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: defaultValue
        }
    }

    override fun getIntPreferencesSync(
        preferencesKey: Preferences.Key<Int>,
        defaultValue: Int
    ): Int {
        val result = runBlocking {
            dataStore.data.map { preferences ->
                preferences[preferencesKey] ?: defaultValue
            }.first()
        }
        return result
    }

    override suspend fun setStringPreferences(
        preferencesKey: Preferences.Key<String>,
        value: String
    ) {
        dataStore.edit { setting ->
            setting[preferencesKey] = value
        }
    }

    override fun getStringPreferences(
        preferencesKey: Preferences.Key<String>,
        defaultValue: String
    ): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: defaultValue
        }
    }


    override fun getStringPreferencesSync(
        preferencesKey: Preferences.Key<String>,
        defaultValue: String
    ): String {
        val result = runBlocking {
            dataStore.data.map { preferences ->
                preferences[preferencesKey] ?: defaultValue
            }.first()
        }
        return result
    }

    override suspend fun setBooleanPreferences(
        preferencesKey: Preferences.Key<Boolean>,
        value: Boolean
    ) {
        dataStore.edit { setting ->
            setting[preferencesKey] = value
        }
    }

    override fun getBooleanPreferences(
        preferencesKey: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: defaultValue
        }
    }

    override fun getBooleanPreferencesSync(
        preferencesKey: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Boolean {
        val result = runBlocking {
            dataStore.data.map { preferences ->
                preferences[preferencesKey] ?: defaultValue
            }.first()
        }
        return result
    }
}