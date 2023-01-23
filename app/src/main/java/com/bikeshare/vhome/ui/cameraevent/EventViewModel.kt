package com.bikeshare.vhome.ui.cameraevent

import android.os.Bundle
import android.util.Log
import androidx.hilt.Assisted
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bikeshare.vhome.data.itemmodel.EventItem
import com.bikeshare.vhome.data.model.*
import com.bikeshare.vhome.repository.VHomeRepository
import com.bikeshare.vhome.util.Event
import com.bikeshare.vhome.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val vHomeRepository: VHomeRepository,
    @Assisted state : SavedStateHandle
) : ViewModel() {
    /**Get List Camera*/
    val getListCamResponseLiveData: MutableLiveData<Event<Resource<CommonArrayResponse>>> = MutableLiveData()
    var getListCamResponse: CommonArrayResponse? = null

    fun getListCam(authorization: String) = viewModelScope.launch(Dispatchers.IO) {
        safeGetListCam(authorization)
    }

    private suspend fun safeGetListCam(authorization: String) {
        val response = vHomeRepository.getListCam(authorization)
        getListCamResponseLiveData.postValue(Event(handleListCamResponse(response)))
    }

    private fun handleListCamResponse(response: Response<CommonArrayResponse>): Resource<CommonArrayResponse> {
        if (response.isSuccessful) {
            Log.d("GETLISTCAM_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                return Resource.Success(getListCamResponse ?: resultResponse)
            }
        } else {
            Log.e("GETLISTCA_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((getListCamResponse ?: response.message()).toString())
    }

    /**Get List Devices*/
    val getListDeviceResponseLiveData: MutableLiveData<Event<Resource<DevicesResponse>>> = MutableLiveData()
    var getListDeviceResponse: DevicesResponse? = null

    fun getListDevice(authorization: String) = viewModelScope.launch(Dispatchers.IO) {
        safeGetListDevice(authorization)
    }

    private suspend fun safeGetListDevice(authorization: String) {
        val response = vHomeRepository.getListDevice(authorization)
        getListDeviceResponseLiveData.postValue(Event(handleListDeviceResponse(response)))
    }

    private fun handleListDeviceResponse(response: Response<DevicesResponse>): Resource<DevicesResponse> {
        if (response.isSuccessful) {
            Log.d("GETLISTDEVICE_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                return Resource.Success(getListDeviceResponse ?: resultResponse)
            }
        } else {
            Log.e("GETLISTDEVICE_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((getListDeviceResponse ?: response.message()).toString())
    }

    /**Search Event*/
    val searchEventResponseLiveData: MutableLiveData<Event<Resource<EventSearchResponse>>> = MutableLiveData()
    var searchEventResponse: EventSearchResponse? = null

    fun searchEvent(authorization: String, post: EventSearchPost) = viewModelScope.launch(Dispatchers.IO) {
        safeSearchEvent(authorization, post)
    }

    private suspend fun safeSearchEvent(authorization: String, post: EventSearchPost) {
        val response = vHomeRepository.searchEvent(authorization, post)
        searchEventResponseLiveData.postValue(Event(handleSearchEventResponse(response)))
    }

    private fun handleSearchEventResponse(response: Response<EventSearchResponse>): Resource<EventSearchResponse> {
        if (response.isSuccessful) {
            Log.d("SEARCHEVENT_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                return Resource.Success(searchEventResponse ?: resultResponse)
            }
        } else {
            Log.e("SEARCHEVENT_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((searchEventResponse ?: response.message()).toString())
    }

    /**Search Event Paging*/
    fun getSearchEvent(token: String, post: EventSearchPost): LiveData<PagingData<EventItem>>{
        return vHomeRepository.getSearchEvent(token,post).cachedIn(viewModelScope)
    }

    /** DataStore Save Information*/
    suspend fun saveCameraEvent(cameraEvent: Int) {
        vHomeRepository.saveCameraEvent(cameraEvent)
    }

    suspend fun saveCameraType(cameraType: String) {
        vHomeRepository.saveCameraType(cameraType)
    }

    suspend fun saveCameraState(cameraState: String) {
        vHomeRepository.saveCameraState(cameraState)
    }

    suspend fun saveDateEvent(date: String) {
        vHomeRepository.saveDateEvent(date)
    }
}