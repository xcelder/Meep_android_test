package com.example.meep_android_test.data.ui_models

data class ResourcesMapBounds(
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)

val ResourcesMapBounds.lowerLeftLatLng: LatLng
    get() = LatLng(bottom, left)

val ResourcesMapBounds.upperRightLatLng: LatLng
    get() = LatLng(top, right)