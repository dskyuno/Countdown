package com.project.timemanagerment.base.constant

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class ConstantsPreferencesKey {
    companion object {
        val userToken = stringPreferencesKey("USER_TOKEN")
        val userToken_default_value: String = ""

        val appUniqueId = stringPreferencesKey("app_unique_id")
        val appUniqueId_default_value = ""

        val countdownArrangementMode = intPreferencesKey("arrangement_mode")
        val countdownArrangementMode_mode_list = 0
        val countdownArrangementMode_mode_grild = 1

        val consentAgreementAndPolicy = booleanPreferencesKey("consent_agreement_and_policy")
        val consentAgreementAndPolicy_default_value = false
    }
}