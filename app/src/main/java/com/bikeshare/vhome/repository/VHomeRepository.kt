package com.bikeshare.vhome.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.itemmodel.EventPagingSource
import com.bikeshare.vhome.data.model.*
import com.bikeshare.vhome.data.remote.ApiInterface
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VHomeRepository @Inject constructor(
    private val vhomeApi: ApiInterface,
    private val preferences: UserPreferences
) {
    suspend fun login(post: LoginPost): Response<LoginResponse> {
        return vhomeApi.userLogin(post)
    }

    suspend fun attributes(authorization: String, post: AttributesPost): Response<DevicesResponse> {
        return vhomeApi.attributes(authorization, post)
    }

    suspend fun preCheck(authorization: String, post: PreCheckPost): Response<CommonResponse> {
        return vhomeApi.preCheck(authorization, post)
    }

    suspend fun addDevice(authorization: String, post: AddDevicePost): Response<AddDeviceResponse> {
        return vhomeApi.addDevice(authorization, post)
    }

    suspend fun getListCam(authorization: String): Response<CommonArrayResponse> {
        return vhomeApi.getListCam(authorization)
    }

    suspend fun getListDevice(authorization: String): Response<DevicesResponse> {
        return vhomeApi.getListDevice(authorization)
    }

    suspend fun searchEvent(authorization: String, post: EventSearchPost): Response<EventSearchResponse> {
        return vhomeApi.searchEvent(authorization, post)
    }

    fun getSearchEvent(token: String, post: EventSearchPost) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EventPagingSource(vhomeApi, token, post) }
        ).liveData

    /**DataStore*/
    suspend fun saveUserInformation(accessToken: String, orgId: String, username: String) {
        preferences.saveUserInformation(accessToken, orgId, username)
    }

    suspend fun saveCameraInformation(cameraManufacturer: String, cameraModel: String, cameraSerial: String, cameraVerificationCode: String) {
        preferences.saveCameraInformation(cameraManufacturer, cameraModel, cameraSerial, cameraVerificationCode)
    }

    suspend fun saveCameraId(cameraId: String) {
        preferences.saveCameraId(cameraId)
    }

    suspend fun saveCameraEvent(cameraEvent: Int) {
        preferences.saveCameraEvent(cameraEvent)
    }

    suspend fun saveCameraType(cameraType: String) {
        preferences.saveCameraType(cameraType)
    }

    suspend fun saveCameraState(cameraState: String) {
        preferences.saveCameraState(cameraState)
    }

    suspend fun saveDateEvent(date: String) {
        preferences.saveDateEvent(date)
    }

    suspend fun clearData() {
        preferences.clearData()
    }
}