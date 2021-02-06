package com.example.meep_android_test.data.ui_models

data class LatLng(
    val lat: Double,
    val lng: Double
) {
    override fun toString() = "$lat,$lng"
}

fun String.toLatLng(): LatLng? = runCatching {
    val values = split(",")
    val lat = values[0].toDouble()
    val lng = values[1].toDouble()
    LatLng(lat, lng)
}.getOrNull()