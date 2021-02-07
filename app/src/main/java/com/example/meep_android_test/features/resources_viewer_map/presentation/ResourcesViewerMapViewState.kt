package com.example.meep_android_test.features.resources_viewer_map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ResourcesViewerMapViewState {

    val state: LiveData<ResourcesViewerMapState>
        get() = _state

    private val _state = MutableLiveData<ResourcesViewerMapState>(ResourcesViewerMapState.Moved)

    fun changeStateToBusy() {
        updateState(ResourcesViewerMapState.Busy)
    }

    fun changeStateToMoved() {
        updateState(ResourcesViewerMapState.Moved)
    }

    fun changeStateToResourcesLoaded() {
        updateState(ResourcesViewerMapState.AreaResourcesLoaded(emptyList()))
    }

    fun changeStateToError() {
        updateState(ResourcesViewerMapState.Error)
    }

    private fun updateState(state: ResourcesViewerMapState) {
        _state.value = state
    }
}