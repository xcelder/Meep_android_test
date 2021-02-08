package com.example.meep_android_test.features.resources_viewer_map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.meep_android_test.data.network_models.ResourceResponseItem

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

    fun changeStateToResourcesLoaded(resources: List<ResourceResponseItem>) {
        updateState(ResourcesViewerMapState.AreaResourcesLoaded(resources))
    }

    fun changeStateToResourceDetail(resourceResponseItem: ResourceResponseItem) {
        updateState(ResourcesViewerMapState.ResourceDetail(resourceResponseItem))
    }

    fun changeStateToError() {
        updateState(ResourcesViewerMapState.Error)
    }

    private fun updateState(state: ResourcesViewerMapState) {
        _state.value = state
    }
}