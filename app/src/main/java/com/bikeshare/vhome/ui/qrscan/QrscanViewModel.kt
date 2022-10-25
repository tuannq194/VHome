package com.bikeshare.vhome.ui.qrscan

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikeshare.vhome.data.model.*
import com.bikeshare.vhome.repository.VHomeRepository
import com.bikeshare.vhome.util.Event
import com.bikeshare.vhome.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QrscanViewModel @Inject constructor(
    private val vHomeRepository: VHomeRepository, @ApplicationContext private val context: Context
) : ViewModel() {
    //Attributes
    val _attributesResponseLiveData: MutableLiveData<Event<Resource<AttributesResponse>>> = MutableLiveData()
    val attributesResponseLiveData: LiveData<Event<Resource<AttributesResponse>>>
        get() = _attributesResponseLiveData

    var attributesResponse: AttributesResponse? = null

    fun attributes(authorization: String, post: AttributesPost) = viewModelScope.launch(Dispatchers.IO) {
        safeAttributes(authorization, post)
    }

    private suspend fun safeAttributes(authorization: String, post: AttributesPost) {
        val response = vHomeRepository.attributes(authorization, post)
        _attributesResponseLiveData.postValue(Event(handleAttributesResponse(response)))
    }

    private fun handleAttributesResponse(response: Response<AttributesResponse>): Resource<AttributesResponse> {
        if (response.isSuccessful) {
            Log.d("ATTRIBUTES_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                if (attributesResponse == null) {
                    attributesResponse  = resultResponse
                }
                return Resource.Success(attributesResponse ?: resultResponse)
            }
        } else {
            Log.e("ATTRIBUTES_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((attributesResponse ?: response.message()).toString())
    }

    suspend fun saveCameraInfomation(cameraSerial: String,cameraVerificationCode: String, cameraId: String) {
        vHomeRepository.saveCameraInfomation(cameraSerial,cameraVerificationCode, cameraId)
    }


    //PreCheck
    val preCheckResponseLiveData: MutableLiveData<Resource<PreCheckResponse>> = MutableLiveData()
    var preCheckResponse: PreCheckResponse? = null

    fun preCheck(authorization: String, post: PreCheckPost) = viewModelScope.launch(Dispatchers.IO) {
        safePreCheck(authorization, post)
    }

    private suspend fun safePreCheck(authorization: String, post: PreCheckPost) {
        val response = vHomeRepository.preCheck(authorization, post)
        preCheckResponseLiveData.postValue(handlePreCheckResponse(response))
    }

    private fun handlePreCheckResponse(response: Response<PreCheckResponse>): Resource<PreCheckResponse> {
        if (response.isSuccessful) {
            Log.d("PRECHECK_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                if (preCheckResponse == null) {
                    preCheckResponse = resultResponse
                }
                return Resource.Success(preCheckResponse ?: resultResponse)
            }
        } else {
            Log.e("PRECHECK_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((preCheckResponse ?: response.message()).toString())
    }
}