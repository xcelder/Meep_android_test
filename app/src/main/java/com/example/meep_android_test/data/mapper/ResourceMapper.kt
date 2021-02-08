package com.example.meep_android_test.data.mapper

import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.data.networkmodels.ResourceResponseItem
import com.example.meep_android_test.data.networkmodels.isBikeStation
import com.example.meep_android_test.data.networkmodels.isMoped

class ResourceMapper {

    fun toDomain(resourceResponseItem: ResourceResponseItem) = with(resourceResponseItem) {
        when {
            isMoped -> Resource.Moped(id, name, x, y, companyZoneID, licencePlate, batteryLevel, helmets, model, resourceType)
            isBikeStation -> Resource.BikeStation(id, name, x, y, companyZoneID, spacesAvailable, allowDropoff, bikesAvailable)
            else -> Resource.Station(id, name, x, y, companyZoneID)
        }
    }
}