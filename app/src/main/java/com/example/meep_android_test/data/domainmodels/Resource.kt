package com.example.meep_android_test.data.domainmodels

sealed class Resource(
    val id: String,
    val name: String,
    val lng: Double,
    val lat: Double,
    val companyZoneID: Int
) {

    class Moped(
        id: String,
        name: String,
        lng: Double,
        lat: Double,
        companyZoneID: Int,
        val licencePlate: String? = null,
        val batteryLevel: Int? = null,
        val helmets: Int? = null,
        val model: String? = null,
        val resourceType: String? = null
    ) : Resource(id, name, lng, lat, companyZoneID)

    class BikeStation(
        id: String,
        name: String,
        lng: Double,
        lat: Double,
        companyZoneID: Int,
        val spacesAvailable: Int? = null,
        val allowDropoff: Boolean? = null,
        val bikesAvailable: Int? = null
    ) : Resource(id, name, lng, lat, companyZoneID)

    class Station(
        id: String,
        name: String,
        lng: Double,
        lat: Double,
        companyZoneID: Int
    ) : Resource(id, name, lng, lat, companyZoneID)
}
