package com.example.meep_android_test.features.resourcesviewermap.business

import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.data.domainmodels.ResourcesMapBounds
import com.example.meep_android_test.data.repository.ResourcesRepository
import com.example.meep_android_test.features.resourcesviewermap.presentation.ResourcesViewerMapViewState
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
        val expectedResource = Resource.Station(
            id = "id",
            name = "resource",
            lng = 0.0,
            lat = 0.0,
            companyZoneID = 0
        )

        // When
        presenter.onMarkerSelected(expectedResource)

        // Then
        verify(viewState).changeStateToResourceDetail(expectedResource)
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
                Resource.Station(
                    id = "id",
                    name = "resource",
                    lng = 0.0,
                    lat = 0.0,
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