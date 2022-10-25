package com.bikeshare.vhome.data.remote

import com.bikeshare.vhome.data.model.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @POST("api/app/login")
    suspend fun userLogin(@Body post: LoginPost): Response<LoginResponse>

    @POST("/api/devices/attributes")
    suspend fun attributes(@Header("Authorization") authorization: String, @Body post: AttributesPost): Response<AttributesResponse>

    @POST("/api/vhome/camera/preCheck")
    suspend fun preCheck(@Header("Authorization") authorization: String, @Body post: PreCheckPost): Response<PreCheckResponse>

}