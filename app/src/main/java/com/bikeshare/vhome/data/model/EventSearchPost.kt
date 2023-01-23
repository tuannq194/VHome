package com.bikeshare.vhome.data.model

data class EventSearchPost(
    val startTime: Long,
    val endTime: Long,
    val deviceIds: ArrayList<String>,
    val eventType: Int,
    val searchString: String,
    val category: Int,
    var page: Int = 1,
    val pageSize: Int,
    val isReads: Boolean
)
