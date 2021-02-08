package com.example.meep_android_test.features.resources_viewer_map.business

import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.data.repository.ResourcesRepository
import com.example.meep_android_test.data.ui_models.ResourcesMapBounds
import com.example.meep_android_test.features.resources_viewer_map.presentation.ResourcesViewerMapViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResourcesViewerMapPresenter(
    private val viewState: ResourcesViewerMapViewState,
    private val resourcesRepository: ResourcesRepository,
    private val foregroundDispatcher: CoroutineContext = Dispatchers.Main,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = foregroundDispatcher

    fun onMapMovedToNewPosition() {
        viewState.changeStateToMoved()
    }

    fun onRequireResourcesInBounds(resourcesMapBounds: ResourcesMapBounds) {
        launch {
            viewState.changeStateToBusy()

            runCatching {
                withContext(backgroundDispatcher) {
                    resourcesRepository.retrieveResourcesInBounds(resourcesMapBounds)
                }
            }
                .onSuccess { viewState.changeStateToResourcesLoaded(it) }
                .onFailure {
                    viewState.changeStateToError()
                }
        }
    }

    fun onMarkerSelected(resourceResponseItem: ResourceResponseItem) {
        viewState.changeStateToResourceDetail(resourceResponseItem)
    }
}
