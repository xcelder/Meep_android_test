package com.example.meep_android_test.features.resourcesviewermap.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.meep_android_test.data.domainmodels.Resource

class ResourcesViewerMapViewState {

    val state: LiveData<ResourcesViewerMapState>
        get() = _state

    private val _state = MutableLiveData<ResourcesViewerMapState>()

    fun changeStateToBusy() {
        updateState(ResourcesViewerMapState.Busy)
    }

    fun changeStateToMoved() {
        updateState(ResourcesViewerMapState.Moved)
    }

    fun changeStateToResourcesLoaded(resources: List<Resource>) {
        updateState(ResourcesViewerMapState.AreaResourcesLoaded(resources))
    }

    fun changeStateToResourceDetail(resourceResponseItem: Resource) {
        updateState(ResourcesViewerMapState.ResourceDetail(resourceResponseItem))
    }

    fun changeStateToError() {
        updateState(ResourcesViewerMapState.Error)
    }

    private fun updateState(state: ResourcesViewerMapState) {
        _state.value = state
    }
}