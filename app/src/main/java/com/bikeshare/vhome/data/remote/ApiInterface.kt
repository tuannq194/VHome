package com.bikeshare.vhome.data.remote

import com.bikeshare.vhome.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    @POST("api/app/login")
    suspend fun userLogin(@Body post: LoginPost): Response<LoginResponse>

    @POST("/api/devices/attributes")
    suspend fun attributes(@Header("Authorization") authorization: String, @Body post: AttributesPost): Response<DevicesResponse>

    @POST("/api/vhome/camera/preCheck")
    suspend fun preCheck(@Header("Authorization") authorization: String, @Body post: PreCheckPost): Response<CommonResponse>

    @POST("/api/vhome/camera/addDevice")
    suspend fun addDevice(@Header("Authorization") authorization: String, @Body post: AddDevicePost): Response<AddDeviceResponse>

    @GET("/api/vhome/camera/list")
    suspend fun getListCam(@Header("Authorization") authorization: String): Response<CommonArrayResponse>

    @GET("/api/devices?expand=true")
    suspend fun getListDevice(@Header("Authorization") authorization: String): Response<DevicesResponse>

    @POST("/api/vhome/camera/event/AI/search")
    suspend fun searchEvent(@Header("Authorization") authorization: String, @Body post: EventSearchPost): Response<EventSearchResponse>

}