package com.bikeshare.vhome.data.model

import com.google.gson.JsonArray

data class DevicesResponse(
    val total: Int,
    val offset: Int,
    val limit: Int,
    val devices: JsonArray
)
