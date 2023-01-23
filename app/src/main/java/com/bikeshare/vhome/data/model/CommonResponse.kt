package com.bikeshare.vhome.data.model

data class CommonResponse(
    val data: Any? = null,
    val error: Boolean,
    val errorCode: Int,
    val errorName: String,
    val status: Int
)
