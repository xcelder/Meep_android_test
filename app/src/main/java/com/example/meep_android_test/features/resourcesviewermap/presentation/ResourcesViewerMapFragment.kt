package com.example.meep_android_test.features.resourcesviewermap.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.meep_android_test.BuildConfig
import com.example.meep_android_test.R
import com.example.meep_android_test.data.domainmodels.*
import com.example.meep_android_test.databinding.ResourcesViewerMapFragmentBinding
import com.example.meep_android_test.features.resourcesviewermap.business.ResourcesViewerMapViewModel
import com.example.meep_android_test.features.resourcesviewermap.business.createResourcesViewerMapViewModel
import com.example.meep_android_test.features.resourcesviewermap.presentation.extensions.buildResourcesDetailBottomSheet
import com.example.meep_android_test.features.resourcesviewermap.presentation.widget.CustomMarkerView
import com.example.meep_android_test.utils.tint
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager

private const val RESOURCES_MAP_BOUNDS_ARG = "resources_map_bounds_arg"
private const val CITY_ZOOM_VALUE = 15.0
private const val CAMERA_ANIMATION_DURATION = 1000

class ResourcesViewerMapFragment : Fragment() {

    companion object {
        fun withArguments(locationBounds: ResourcesMapBounds): ResourcesViewerMapFragment =
            ResourcesViewerMapFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RESOURCES_MAP_BOUNDS_ARG, locationBounds)
                }
            }
    }

    private val viewModel: ResourcesViewerMapViewModel by viewModels { createResourcesViewerMapViewModel() }

    private val mapView: MapView? by lazy { view?.findViewById<MapView>(R.id.map) }
    private var mapBoxMap: MapboxMap? = null
    private var markerViewManager: MarkerViewManager? = null

    private val resourcesMapBounds: ResourcesMapBounds? by lazy {
        arguments?.getSerializable(RESOURCES_MAP_BOUNDS_ARG) as? ResourcesMapBounds
    }

    private var resourceDetailBottomSheet: BottomSheetDialog? = null

    private val stateObserver = Observer<ResourcesViewerMapState> { handleStateChange(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { Mapbox.getInstance(it, BuildConfig.MAPS_ACCESS_TOKEN) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<ResourcesViewerMapFragmentBinding>(
        inflater,
        R.layout.resources_viewer_map_fragment,
        container,
        false
    )
        .apply {
            lifecycleOwner = this@ResourcesViewerMapFragment
            viewState = viewModel.viewState
            presenter = viewModel.presenter
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewInteractions(view)

        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(::onMapReady)
    }

    private fun setupViewInteractions(view: View) {
        val searchButton: Button = view.findViewById(R.id.searchButton)
        searchButton.setOnClickListener { onRequestNewResources() }
    }

    private fun handleStateChange(state: ResourcesViewerMapState) = when (state) {
        is ResourcesViewerMapState.AreaResourcesLoaded -> onAreaResourcesLoaded(state.resources)
        is ResourcesViewerMapState.ResourceDetail -> showResourceDetails(state.resourceResponseItem)
        is ResourcesViewerMapState.Error -> showErrorDialog()
        else -> { /*no-op*/ }
    }

    private fun onMapReady(mapBoxMap: MapboxMap) {
        this.mapBoxMap = mapBoxMap
        this.markerViewManager = MarkerViewManager(mapView, mapBoxMap)
        this.mapBoxMap?.run {
            setStyle(Style.MAPBOX_STREETS) {
                initCameraPosition()
            }
        }
    }

    private fun MapboxMap.initCameraPosition() {
        resourcesMapBounds?.center?.let { boundsCenter ->
            addOnCameraIdleListener(::onCameraIdle)

            val cameraPosition = CameraPosition.Builder()
                .target(boundsCenter.toMapBoxLatLng())
                .zoom(CITY_ZOOM_VALUE)
                .build()

            animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                CAMERA_ANIMATION_DURATION
            )
        }
    }

    private fun onCameraIdle() {
        viewModel.presenter.onMapMovedToNewPosition()
    }

    private fun onRequestNewResources() {
        val bounds = this.mapBoxMap?.projection?.visibleRegion?.latLngBounds?.run {
            ResourcesMapBounds(
                left = lonWest,
                top = latNorth,
                right = lonEast,
                bottom = latSouth
            )
        }
        bounds?.let { viewModel.presenter.onRequireResourcesInBounds(it) }
    }

    private fun onAreaResourcesLoaded(resources: List<Resource>) {
        var pickerColorPosition = 0
        val availableColors = getResources().obtainTypedArray(R.array.marker_colors)
        val alreadyUsedColors = mutableMapOf<Int, Int>()
        resources.forEach {
            val colorTint = alreadyUsedColors[it.companyZoneID]
                ?: availableColors.getColor(pickerColorPosition, 0).also { color ->
                    alreadyUsedColors[it.companyZoneID] = color
                    pickerColorPosition++
                }

            val drawable = getDrawableTintedWithColor(colorTint)

            val markerView = CustomMarkerView(
                requireContext(),
                drawable,
                it,
                viewModel.presenter::onMarkerSelected
            )
            markerViewManager?.addMarker(markerView)
        }
        availableColors.recycle()
    }

    private fun getDrawableTintedWithColor(@ColorInt colorTint: Int) =
        ResourcesCompat.getDrawable(resources, R.drawable.icon_maps_maker, context?.theme)
            ?.tint(colorTint)

    private fun showResourceDetails(resourceResponseItem: Resource) {
        dismissResourcesDetails()
        resourceDetailBottomSheet = buildResourcesDetailBottomSheet(resourceResponseItem)
        resourceDetailBottomSheet?.show()
    }

    private fun dismissResourcesDetails() {
        resourceDetailBottomSheet?.dismiss()
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.resources_error_dialog_msg)
            .setPositiveButton(R.string.resources_error_dialog_ok_btn) { dialog, _ -> dialog.dismiss()  }
            .show()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        viewModel.viewState.state.observe(this, stateObserver)
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
        viewModel.viewState.state.removeObserver(stateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        markerViewManager?.onDestroy()
        mapView?.onDestroy()
        this.mapBoxMap?.removeOnCameraIdleListener(::onCameraIdle)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}