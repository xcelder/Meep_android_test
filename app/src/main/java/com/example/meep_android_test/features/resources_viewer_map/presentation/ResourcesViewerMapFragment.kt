package com.example.meep_android_test.features.resources_viewer_map.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.meep_android_test.BuildConfig
import com.example.meep_android_test.R
import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.data.ui_models.ResourcesMapBounds
import com.example.meep_android_test.data.ui_models.lowerLeftLatLng
import com.example.meep_android_test.data.ui_models.upperRightLatLng
import com.example.meep_android_test.data.ui_models.toLatLng
import com.example.meep_android_test.data.ui_models.center
import com.example.meep_android_test.data.ui_models.toMapBoxLatLng
import com.example.meep_android_test.databinding.ResourcesViewerMapFragmentBinding
import com.example.meep_android_test.features.resources_viewer_map.business.ResourcesViewerMapViewModel
import com.example.meep_android_test.features.resources_viewer_map.business.createResourcesViewerMapViewModel
import com.example.meep_android_test.utils.tint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

private const val LOWER_LEFT_ARG = "lower_left_arg"
private const val UPPER_RIGHT_ARG = "upper_right_arg"
private const val CITY_ZOOM_VALUE = 15.0
private const val CAMERA_ANIMATION_DURATION = 1000


class ResourcesViewerMapFragment : Fragment() {

    companion object {
        fun withArguments(locationBounds: ResourcesMapBounds): ResourcesViewerMapFragment =
            ResourcesViewerMapFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(LOWER_LEFT_ARG, locationBounds.lowerLeftLatLng.toString())
                    putSerializable(UPPER_RIGHT_ARG, locationBounds.upperRightLatLng.toString())
                }
            }
    }

    private val viewModel: ResourcesViewerMapViewModel by viewModels { createResourcesViewerMapViewModel() }

    private val mapView: MapView? by lazy { view?.findViewById<MapView>(R.id.map) }
    private var mapBoxMap: MapboxMap? = null
    private var markerViewManager: MarkerViewManager? = null

    private val resourcesMapBounds: ResourcesMapBounds? by lazy {
        val lowerLeftVal = arguments?.getString(LOWER_LEFT_ARG)?.toLatLng()
        val upperRightVal = arguments?.getString(UPPER_RIGHT_ARG)?.toLatLng()
        if (lowerLeftVal != null && upperRightVal != null) {
            ResourcesMapBounds(
                left = lowerLeftVal.lng,
                top = upperRightVal.lat,
                right = upperRightVal.lng,
                bottom = lowerLeftVal.lat
            )
        } else {
            null
        }
    }

    private val stateObserver = Observer<ResourcesViewerMapState> {
        when (it) {
            is ResourcesViewerMapState.AreaResourcesLoaded -> onAreaResourcesLoaded(it.resources)
            else -> { /*no-op*/
            }
        }
    }

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

    private fun onAreaResourcesLoaded(resources: List<ResourceResponseItem>) {
        var pickerColorPosition = 0
        val availableColors = getResources().obtainTypedArray(R.array.marker_colors)
        val alreadyUsedColors = mutableMapOf<Int, Int>()
        resources
            .groupBy { it.companyZoneID }
            .entries.forEach { (key, values) ->
                val colorTint = alreadyUsedColors[key]
                    ?: availableColors.getColor(pickerColorPosition, 0)
                        .also { pickerColorPosition++ }
                val drawable = getDrawableTintedWithColor(colorTint)

                values.forEach {
                    val imageView = ImageView(context).apply {
                        val markerSize = getResources().getDimensionPixelSize(R.dimen.marker_size)
                        layoutParams = FrameLayout.LayoutParams(markerSize, markerSize)
                        setImageDrawable(drawable)
                    }
                    val markerView = MarkerView(LatLng(it.y, it.x), imageView)
                    markerViewManager?.addMarker(markerView)
                }
            }
        availableColors.recycle()
    }

    private fun getDrawableTintedWithColor(@ColorInt colorTint: Int) =
        ResourcesCompat.getDrawable(getResources(), R.drawable.icon_maps_maker, context?.theme)
            ?.tint(colorTint)

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