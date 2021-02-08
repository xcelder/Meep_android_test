package com.example.meep_android_test.data.domainmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class LatLng(
    val lat: Double,
    val lng: Double
) : Parcelable, Serializable {
    override fun toString() = "$lat,$lng"
}

fun String.toLatLng(): LatLng? = runCatching {
    val values = split(",")
    val lat = values[0].toDouble()
    val lng = values[1].toDouble()
    LatLng(lat, lng)
}.getOrNull()

fun LatLng.toMapBoxLatLng() = com.mapbox.mapboxsdk.geometry.LatLng(lat, lng)