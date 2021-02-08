package com.example.meep_android_test.features.resources_viewer_map.business

import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.data.repository.ResourcesRepository
import com.example.meep_android_test.data.ui_models.ResourcesMapBounds
import com.example.meep_android_test.features.resources_viewer_map.presentation.ResourcesViewerMapViewState
import com.google.gson.JsonParseException
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ResourcesViewerMapPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val viewState: ResourcesViewerMapViewState = mock()
    private val resourcesRepository: ResourcesRepository = mock()

    @Before
    fun setupTest() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownTest() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `on map moved change state to Move`() {
        // Given
        val presenter = ResourcesViewerMapPresenter(
            viewState,
            resourcesRepository,
            testDispatcher,
            testDispatcher
        )

        // When
        presenter.onMapMovedToNewPosition()

        // Then
        verify(viewState).changeStateToMoved()
    }

    @Test
    fun `on marker selected state change to ResourceDetail`() {
        // Given
        val presenter = ResourcesViewerMapPresenter(
            viewState,
            resourcesRepository,
            testDispatcher,
            testDispatcher
        )
        val expectedResourceResponseItem = ResourceResponseItem(
            id = "id",
            name = "resource",
            x = 0.0,
            y = 0.0,
            companyZoneID = 0
        )

        // When
        presenter.onMarkerSelected(expectedResourceResponseItem)

        // Then
        verify(viewState).changeStateToResourceDetail(expectedResourceResponseItem)
    }

    @Test
    fun `on require resources success change state to AreaResourcesLoaded`() {
        testDispatcher.runBlockingTest {
            // Given
            val presenter = ResourcesViewerMapPresenter(
                viewState,
                resourcesRepository,
                testDispatcher,
                testDispatcher
            )
            val mockResourcesMapBounds = ResourcesMapBounds(
                left = 0.0,
                top = 0.0,
                right = 0.0,
                bottom = 0.0
            )
            val expectedResources = listOf(
                ResourceResponseItem(
                    id = "id",
                    name = "resource",
                    x = 0.0,
                    y = 0.0,
                    companyZoneID = 0
                )
            )

            whenever(resourcesRepository.retrieveResourcesInBounds(mockResourcesMapBounds))
                .thenReturn(expectedResources)

            // When
            presenter.onRequireResourcesInBounds(mockResourcesMapBounds)

            // Then
            verify(viewState).changeStateToBusy()
            verify(viewState).changeStateToResourcesLoaded(expectedResources)
        }
    }

    @Test
    fun `on require resources fails change state to Error`() {
        testDispatcher.runBlockingTest {
            // Given
            val presenter = ResourcesViewerMapPresenter(
                viewState,
                resourcesRepository,
                testDispatcher,
                testDispatcher
            )
            val mockResourcesMapBounds = ResourcesMapBounds(
                left = 0.0,
                top = 0.0,
                right = 0.0,
                bottom = 0.0
            )

            whenever(resourcesRepository.retrieveResourcesInBounds(mockResourcesMapBounds))
                .thenAnswer { throw JsonParseException("") }

            // When
            presenter.onRequireResourcesInBounds(mockResourcesMapBounds)

            // Then
            verify(viewState).changeStateToBusy()
            verify(viewState).changeStateToError()
        }
    }
}