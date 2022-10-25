package com.bikeshare.vhome.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val appContext = context.applicationContext

    val accessToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    val cameraSerial: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_SERIAL]
        }

    val cameraVerificationCode: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_VERIFICATION_CODE]
        }

    suspend fun saveAccessTokens(accessToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = "Bearer " + accessToken
        }
    }

    suspend fun saveCameraInfomation(cameraSerial: String,cameraVerificationCode: String, cameraId: String){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_SERIAL] = cameraSerial
            preferences[CAMERA_VERIFICATION_CODE] = cameraVerificationCode
            preferences[CAMERA_ID] = cameraId
        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val CAMERA_SERIAL = stringPreferencesKey("camera_serial")
        private val CAMERA_VERIFICATION_CODE = stringPreferencesKey("camera_verification_code")
        private val CAMERA_ID = stringPreferencesKey("camera_id")
    }
}