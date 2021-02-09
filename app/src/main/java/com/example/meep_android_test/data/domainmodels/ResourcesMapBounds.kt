package com.example.meep_android_test.data.domainmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ResourcesMapBounds(
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
): Parcelable, Serializable

val ResourcesMapBounds.lowerLeftLatLng: LatLng
    get() = LatLng(bottom, left)

val ResourcesMapBounds.upperRightLatLng: LatLng
    get() = LatLng(top, right)

val ResourcesMapBounds.center: LatLng
        get() = LatLng(
            lat = (top + bottom) / 2,
            lng = (left + right) / 2
        )