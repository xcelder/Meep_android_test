package com.example.meep_android_test.data.repository

import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.data.ui_models.ResourcesMapBounds
import com.example.meep_android_test.data.ui_models.lowerLeftLatLng
import com.example.meep_android_test.data.ui_models.upperRightLatLng
import com.example.meep_android_test.network.api.ResourcesApi

class ResourcesRepository(
    private val resourcesApi: ResourcesApi
) {

    suspend fun retrieveResourcesInBounds(resourcesMapBounds: ResourcesMapBounds): List<ResourceResponseItem> {
        val (lowerLeft, upperRight) = resourcesMapBounds.run {
            lowerLeftLatLng.toString() to upperRightLatLng.toString()
        }
        return resourcesApi.getLisbonResourcesInBounds(lowerLeft, upperRight)
    }
}