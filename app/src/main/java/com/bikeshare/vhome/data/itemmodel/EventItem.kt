package com.bikeshare.vhome.data.itemmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventItem(
    var createDate: Long,
    var crypto: String?,
    var description : EventItemDescription,
    var deviceId: String,
    var gatewayId: String,
    var id: String,
    val isRead: Boolean,
    var lastModified: Long,
    val read: Boolean,
    var startTime: Long?,
    var stopTime: Long?,
    var stringDescription: String,
    var thumbnail: Thumbnail,
    var type: Int,
    var videoId: String,
    var videoUploaded: Boolean
) : Parcelable{
    @Parcelize
    data class EventItemDescription(
        var bboxes: String?,
        var msgId: String?,
        var time: Long
    ): Parcelable

    @Parcelize
    data class Thumbnail(
        var bucketName: String,
        var fileChecksum: String,
        var fileExtension: String,
        var fileName: String,
        var fileSize: Long,
        var fileURL: String,
        var fileURLExpirationDate: String,
        var fileURLExpired: Boolean,
        var status: Int
    ): Parcelable
}
