package com.bikeshare.vhome.data.model

import com.google.gson.JsonArray

data class CommonArrayResponse(
    val data: JsonArray? = null,
    val error: Boolean,
    val errorCode: Int,
    val errorName: String,
    val status: Int
)
