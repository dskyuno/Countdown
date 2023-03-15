package com.project.timemanagerment.base.datestore.proto



import com.project.timemanagerment.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun getUserPreferenceSync(): UserPreferences
    fun getUserPreference(): Flow<UserPreferences>
    suspend fun setUserPreference(userPreferences: UserPreferences)
    suspend fun clearUserPreference()
}