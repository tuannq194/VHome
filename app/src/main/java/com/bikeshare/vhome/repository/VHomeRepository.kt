package com.bikeshare.vhome.repository

import com.bikeshare.vhome.data.UserPreferences
import com.bikeshare.vhome.data.model.*
import com.bikeshare.vhome.data.remote.ApiInterface
import okhttp3.RequestBody
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

    suspend fun preCheck(authorization: String, post: PreCheckPost): Response<PreCheckResponse> {
        return vhomeApi.preCheck(authorization ,post)
    }

    suspend fun attributes(authorization: String, post: AttributesPost): Response<AttributesResponse> {
        return vhomeApi.attributes(authorization ,post)
    }

    suspend fun saveAccessTokens(accessToken: String) {
        preferences.saveAccessTokens(accessToken)
    }

    suspend fun saveCameraInfomation(cameraSerial: String,cameraVerificationCode: String, cameraId: String) {
        preferences.saveCameraInfomation(cameraSerial,cameraVerificationCode, cameraId)
    }
}