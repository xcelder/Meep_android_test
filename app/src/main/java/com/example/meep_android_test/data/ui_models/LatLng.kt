package com.example.meep_android_test.data.ui_models

data class LatLng(
    val lat: Double,
    val lng: Double
) {
    override fun toString() = "$lat,$lng"
}