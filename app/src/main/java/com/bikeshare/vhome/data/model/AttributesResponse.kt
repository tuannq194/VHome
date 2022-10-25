package com.bikeshare.vhome.data.model

import com.google.gson.JsonArray
import org.json.JSONObject

data class AttributesResponse(
    val total: Int,
    val offset: Int,
    val limit: Int,
    val devices: ArrayList<Any>
)
