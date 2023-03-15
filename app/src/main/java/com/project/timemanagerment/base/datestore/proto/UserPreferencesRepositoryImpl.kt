package com.project.timemanagerment.base.datestore.proto


import android.util.Log
import androidx.datastore.core.DataStore
import com.project.timemanagerment.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(private val userPreferencesStore: DataStore<UserPreferences>) :
    UserPreferencesRepository {
    override suspend fun getUserPreferenceSync(): UserPreferences {
        return userPreferencesStore.data.first()
    }

    override fun getUserPreference(): Flow<UserPreferences> {
        return userPreferencesStore.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e("getUserPreference", "Error reading sort order preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }
    }

    override suspend fun setUserPreference(userPreferences: UserPreferences) {
        userPreferencesStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setAccount(userPreferences.account)
                .setEmail(userPreferences.email)
                .setNickname(userPreferences.nickname)
                .setSex(userPreferences.sex)
                .setExpireTime(userPreferences.expireTime)
                .build()
        }
    }

    override suspend fun clearUserPreference() {
        userPreferencesStore.updateData {
            userPreferencesStore.updateData { currentPreferences ->
                currentPreferences.toBuilder()
                    .clearAccount()
                    .clearEmail()
                    .clearNickname()
                    .clearSex()
                    .clearExpireTime()
                    .build()
            }
        }
    }
}