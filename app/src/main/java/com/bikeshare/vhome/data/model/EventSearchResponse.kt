package com.bikeshare.vhome.data.model

import com.bikeshare.vhome.data.itemmodel.EventItem

data class EventSearchResponse(
    val data: EventData,
    val error: Boolean,
    val errorCode: Int,
    val errorName: String,
    val status: Int
){
    data class EventData(
        var currentPage: Int,
        var events: MutableList<EventItem>,
        var totalEvents: Int
    )
}
