package com.bikeshare.vhome.ui.camerahome.qrscan

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
    val _attributesResponseLiveData: MutableLiveData<Event<Resource<DevicesResponse>>> =
        MutableLiveData()
    val attributesResponseLiveData: LiveData<Event<Resource<DevicesResponse>>>
        get() = _attributesResponseLiveData

    var attributesResponse: DevicesResponse? = null

    fun attributes(authorization: String, post: AttributesPost) =
        viewModelScope.launch(Dispatchers.IO) {
            safeAttributes(authorization, post)
        }

    private suspend fun safeAttributes(authorization: String, post: AttributesPost) {
        val response = vHomeRepository.attributes(authorization, post)
        _attributesResponseLiveData.postValue(Event(handleAttributesResponse(response)))
    }

    private fun handleAttributesResponse(response: Response<DevicesResponse>): Resource<DevicesResponse> {
        if (response.isSuccessful) {
            Log.d("ATTRIBUTES_RETROFIT_SUCCESS", response.body().toString())
            response.body()?.let { resultResponse ->
                /*if (attributesResponse == null) {
                    attributesResponse  = resultResponse
                }*/
                return Resource.Success(attributesResponse ?: resultResponse)
            }
        } else {
            Log.e("ATTRIBUTES_RETROFIT_ERROR", response.body().toString())
        }
        return Resource.Error((attributesResponse ?: response.message()).toString())
    }

    fun saveCameraInformation(cameraManufacturer: String, cameraModel: String,cameraSerial: String, cameraVerificationCode: String) =
        viewModelScope.launch(Dispatchers.IO) {
            safeSaveCameraInformation(cameraManufacturer, cameraModel,cameraSerial, cameraVerificationCode)
        }

    suspend fun safeSaveCameraInformation(cameraManufacturer: String, cameraModel: String, cameraSerial: String, cameraVerificationCode: String) {
        vHomeRepository.saveCameraInformation(cameraManufacturer, cameraModel, cameraSerial, cameraVerificationCode)
    }


}