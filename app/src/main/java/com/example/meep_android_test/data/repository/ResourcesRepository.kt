package com.example.meep_android_test.data.repository

import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.data.domainmodels.ResourcesMapBounds
import com.example.meep_android_test.data.domainmodels.lowerLeftLatLng
import com.example.meep_android_test.data.domainmodels.upperRightLatLng
import com.example.meep_android_test.data.mapper.ResourceMapper
import com.example.meep_android_test.network.api.ResourcesApi

class ResourcesRepository(
    private val resourcesApi: ResourcesApi,
    private val resourceMapper: ResourceMapper
) {

    suspend fun retrieveResourcesInBounds(resourcesMapBounds: ResourcesMapBounds): List<Resource> {
        val (lowerLeft, upperRight) = resourcesMapBounds.run {
            lowerLeftLatLng.toString() to upperRightLatLng.toString()
        }
        return resourcesApi.getLisbonResourcesInBounds(lowerLeft, upperRight)
            .map { resourceMapper.toDomain(it) }
    }
}