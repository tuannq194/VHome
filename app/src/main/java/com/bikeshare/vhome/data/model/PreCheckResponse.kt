package com.bikeshare.vhome.data.model

import com.google.gson.Gson

data class PreCheckResponse(
    val data: String,
    val error: Boolean,
    val errorCode: Int,
    val errorName: String,
    val status: Int
)
