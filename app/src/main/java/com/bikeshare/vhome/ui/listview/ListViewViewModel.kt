package com.bikeshare.vhome.ui.listview

import android.content.Context
import androidx.lifecycle.ViewModel
import com.bikeshare.vhome.repository.VHomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ListViewViewModel @Inject constructor(
    private val vHomeRepository: VHomeRepository, @ApplicationContext private val context: Context
) : ViewModel() {

}