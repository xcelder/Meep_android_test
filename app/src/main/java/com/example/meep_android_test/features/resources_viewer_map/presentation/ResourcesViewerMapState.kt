package com.example.meep_android_test.features.resources_viewer_map.presentation

import com.example.meep_android_test.data.network_models.ResourceResponseItem

sealed class ResourcesViewerMapState {

    object Busy : ResourcesViewerMapState()

    object Moved : ResourcesViewerMapState()

    object Error : ResourcesViewerMapState()

    class AreaResourcesLoaded(val resources: List<ResourceResponseItem>) : ResourcesViewerMapState()
}