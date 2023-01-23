package com.bikeshare.vhome.ui.home

import androidx.lifecycle.ViewModel
import com.bikeshare.vhome.repository.VHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vHomeRepository: VHomeRepository
) : ViewModel(){
    suspend fun clearData() {
        vHomeRepository.clearData()
    }
}