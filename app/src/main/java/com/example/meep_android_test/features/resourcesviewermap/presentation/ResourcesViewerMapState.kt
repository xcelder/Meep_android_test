package com.example.meep_android_test.features.resourcesviewermap.presentation

import com.example.meep_android_test.data.domainmodels.Resource

sealed class ResourcesViewerMapState {

    object Busy : ResourcesViewerMapState()

    object Moved : ResourcesViewerMapState()

    object Error : ResourcesViewerMapState()

    class AreaResourcesLoaded(val resources: List<Resource>) : ResourcesViewerMapState()

    class ResourceDetail(val resourceResponseItem: Resource) : ResourcesViewerMapState()
}