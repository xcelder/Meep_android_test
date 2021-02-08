package com.example.meep_android_test.features.resourcesviewermap.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meep_android_test.data.repository.ResourcesRepository
import com.example.meep_android_test.features.resourcesviewermap.presentation.ResourcesViewerMapViewState
import com.example.meep_android_test.network.RetroClient

class ResourcesViewerMapViewModel(
    val viewState: ResourcesViewerMapViewState,
    val presenter: ResourcesViewerMapPresenter
) : ViewModel() {
    class Factory(
        private val viewState: ResourcesViewerMapViewState,
        private val presenter: ResourcesViewerMapPresenter
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ResourcesViewerMapViewModel(viewState, presenter) as T
    }
}

fun createResourcesViewerMapViewModel(): ResourcesViewerMapViewModel.Factory {
    val viewState = ResourcesViewerMapViewState()
    val resourcesApi = RetroClient().resourcesApi
    val resourcesRepository = ResourcesRepository(resourcesApi)
    val presenter = ResourcesViewerMapPresenter(viewState, resourcesRepository)
    return ResourcesViewerMapViewModel.Factory(viewState, presenter)
}