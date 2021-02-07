package com.example.meep_android_test.data.network_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

private const val MOPED_VALUE = "MOPED"

@JsonClass(generateAdapter = true)
data class ResourceResponseItem(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "x") val x: Double,
    @Json(name = "y") val y: Double,
    @Json(name = "scheduledArrival") val scheduledArrival: Int? = null,
    @Json(name = "locationType") val locationType: Int? = null,
    @Json(name = "companyZoneId") val companyZoneID: Int,
    @Json(name = "lat") val lat: Double? = null,
    @Json(name = "lon") val lon: Double? = null,
    @Json(name = "licencePlate") val licencePlate: String? = null,
    @Json(name = "range") val range: Long? = null,
    @Json(name = "batteryLevel") val batteryLevel: Int? = null,
    @Json(name = "helmets") val helmets: Int? = null,
    @Json(name = "model") val model: String? = null,
    @Json(name = "resourceImageId") val resourceImageID: String? = null,
    @Json(name = "realTimeData") val realTimeData: Boolean? = null,
    @Json(name = "resourceType") val resourceType: String? = null,
    @Json(name = "station") val station: Boolean? = null,
    @Json(name = "availableResources") val availableResources: Int? = null,
    @Json(name = "spacesAvailable") val spacesAvailable: Int? = null,
    @Json(name = "allowDropoff") val allowDropoff: Boolean? = null,
    @Json(name = "bikesAvailable") val bikesAvailable: Int? = null
)

val ResourceResponseItem.isStation: Boolean
    get() = locationType != null && scheduledArrival != null

val ResourceResponseItem.isMoped: Boolean
    get() = resourceType == MOPED_VALUE

val ResourceResponseItem.isBikeStation: Boolean
    get() = station == true && bikesAvailable != null