package com.bikeshare.vhome.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bikeshare.vhome.data.itemmodel.CameraItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val appContext = context.applicationContext

    /**User Information*/
    suspend fun accessTokenString(): String? {
        return appContext.dataStore.data.first()[ACCESS_TOKEN]
    }

    suspend fun accessOrgIdString(): String? {
        return appContext.dataStore.data.first()[ORG_ID]
    }

    suspend fun accessUsernameString(): String? {
        return appContext.dataStore.data.first()[USER_NAME]
    }

    val accessToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    val accessOrgId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ORG_ID]
        }

    val accessUsername: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[USER_NAME]
        }

    suspend fun saveUserInformation(accessToken: String, orgId: String, username: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = "Bearer " + accessToken
            preferences[ORG_ID] = orgId
            preferences[USER_NAME] = username
        }
    }

    /**Camera Information*/
    suspend fun accessCameraManufacturerString(): String? {
        return appContext.dataStore.data.first()[CAMERA_MANUFACTURER]
    }

    suspend fun accessCameraModelString(): String? {
        return appContext.dataStore.data.first()[CAMERA_MODEL]
    }

    suspend fun accessCameraSerialString(): String? {
        return appContext.dataStore.data.first()[CAMERA_SERIAL]
    }

    suspend fun accessCameraVerificationCodeString(): String? {
        return appContext.dataStore.data.first()[CAMERA_VERIFICATION_CODE]
    }

    val accessCameraManufacturer: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_MANUFACTURER]
        }

    val accessCameraModel: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_MODEL]
        }

    val accessCameraSerial: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_SERIAL]
        }

    val accessCameraVerificationCode: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_VERIFICATION_CODE]
        }

    suspend fun saveCameraInformation(cameraManufacturer: String, cameraModel: String, cameraSerial: String,cameraVerificationCode: String){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_MANUFACTURER] = cameraManufacturer
            preferences[CAMERA_MODEL] = cameraModel
            preferences[CAMERA_SERIAL] = cameraSerial
            preferences[CAMERA_VERIFICATION_CODE] = cameraVerificationCode
        }
    }

    /**Camera Id*/
    suspend fun accessCameraIdString(): String? {
        return appContext.dataStore.data.first()[CAMERA_ID]
    }

    val accessCameraId: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_ID]
        }

    suspend fun saveCameraId(cameraId: String){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_ID] = cameraId
        }
    }

    /**Camera Event*/
    suspend fun accessCameraEventInt(): Int {
        return appContext.dataStore.data.first()[CAMERA_EVENT]?:0
    }

    val accessCameraEvent: Flow<Int?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_EVENT]
        }

    suspend fun saveCameraEvent(cameraEvent: Int){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_EVENT] = cameraEvent
        }
    }

    /**Camera Type*/
    suspend fun accessCameraTypeString(): String? {
        return appContext.dataStore.data.first()[CAMERA_TYPE]?:"HC22/HC32"
    }

    val accessCameraType: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_TYPE]
        }

    suspend fun saveCameraType(cameraType: String){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_TYPE] = cameraType
        }
    }

    /**Camera State*/
    suspend fun accessCameraStateString(): String? {
        return appContext.dataStore.data.first()[CAMERA_STATE]
    }
    // ?:"[{\"cameraName\":\"CNME10000454\",\"isChecked\":\"true\"},{\"cameraName\":\"CNME00000305\",\"isChecked\":\"true\"}]"

    val accessCameraState: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[CAMERA_STATE]
        }

    suspend fun saveCameraState(cameraState: String){
        appContext.dataStore.edit { preferences ->
            preferences[CAMERA_STATE] = cameraState
        }
    }

    /**Date*/
    suspend fun accessDateString(): String? {
        return appContext.dataStore.data.first()[DATE_EVENT]
    }

    val accessDate: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[DATE_EVENT]
        }

    suspend fun saveDateEvent(date: String){
        appContext.dataStore.edit { preferences ->
            preferences[DATE_EVENT] = date
        }
    }

    /** Clear Datastore*/
    suspend fun clearData() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val ORG_ID = stringPreferencesKey("org_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val CAMERA_MANUFACTURER = stringPreferencesKey("camera_menufacturer")
        private val CAMERA_MODEL = stringPreferencesKey("camera_model")
        private val CAMERA_SERIAL = stringPreferencesKey("camera_serial")
        private val CAMERA_VERIFICATION_CODE = stringPreferencesKey("camera_verification_code")
        private val CAMERA_ID = stringPreferencesKey("camera_id")
        private val CAMERA_EVENT=  intPreferencesKey("camera_event")
        private val CAMERA_TYPE= stringPreferencesKey("camera_type")
        private val CAMERA_STATE = stringPreferencesKey("camera_state")
        private val DATE_EVENT = stringPreferencesKey("date_event")
    }
}