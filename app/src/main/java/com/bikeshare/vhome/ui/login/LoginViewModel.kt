package com.bikeshare.vhome.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikeshare.vhome.data.model.LoginPost
import com.bikeshare.vhome.data.model.LoginResponse
import com.bikeshare.vhome.repository.VHomeRepository
import com.bikeshare.vhome.util.Event

import com.bikeshare.vhome.util.Resource
import com.bikeshare.vhome.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val vHomeRepository: VHomeRepository, @ApplicationContext private val context: Context
) : ViewModel() {
    val _loginResponseLiveData: MutableLiveData<Event<Resource<LoginResponse>>> = MutableLiveData()
    val loginResponseLiveData: LiveData<Event<Resource<LoginResponse>>>
        get() = _loginResponseLiveData

    var loginResponse: LoginResponse? = null

    fun login(post: LoginPost) = viewModelScope.launch(Dispatchers.IO) {
        safeLogin(post)
    }

    private suspend fun safeLogin(post: LoginPost) {
        val response = vHomeRepository.login(post)
        _loginResponseLiveData.postValue(Event(handleLoginResponse(response)))
    }

    private fun handleLoginResponse(response: Response<LoginResponse>): Resource<LoginResponse> {
        if (response.isSuccessful) {
            Log.d("LOGIN_RETROFIT_SUCCESS", response.body()?.org_name.toString())
            response.body()?.let { resultResponse ->
                //Fix it
                /*if (loginResponse == null){
                    loginResponse = resultResponse
                }*/
                return Resource.Success(loginResponse ?: resultResponse)
            }
        } else {
            Log.e("LOGIN_RETROFIT_ERROR", response.toString())
        }
        return Resource.Error((loginResponse ?: response.message()).toString())
    }

    suspend fun saveUserInformation(accessToken: String, orgId: String, username: String) {
        vHomeRepository.saveUserInformation(accessToken, orgId, username)
    }
}