package com.example.meep_android_test.features.resources_viewer_map.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.meep_android_test.BuildConfig
import com.example.meep_android_test.R
import com.example.meep_android_test.data.ui_models.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.resources_viewer_map_fragment.*

private const val LOWER_LEFT_ARG = "lower_left_arg"
private const val UPPER_RIGHT_ARG = "upper_right_arg"
private const val CITY_ZOOM_VALUE = 14.0
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

    private val mapView: MapView? by lazy { view?.findViewById<MapView>(R.id.map) }
    private var mapBoxMap: MapboxMap? = null

    private val lowerLeft: LatLng? by lazy { arguments?.getString(LOWER_LEFT_ARG)?.toLatLng() }
    private val upperRight: LatLng? by lazy { arguments?.getString(UPPER_RIGHT_ARG)?.toLatLng() }
    
    private val resourcesMapBounds: ResourcesMapBounds? by lazy {
        val lowerLeftVal = lowerLeft
        val upperRightVal = upperRight
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { Mapbox.getInstance(it, BuildConfig.MAPS_ACCESS_TOKEN) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.resources_viewer_map_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView?.onCreate(savedInstanceState)

        mapView?.getMapAsync(::onMapReady)
    }

    private fun onMapReady(mapBoxMap: MapboxMap) {
        this.mapBoxMap = mapBoxMap
        this.mapBoxMap?.setStyle(Style.MAPBOX_STREETS) {
            initCameraPosition()
        }
    }

    private fun initCameraPosition() {
        resourcesMapBounds?.center?.let { boundsCenter ->
            val cameraPosition = CameraPosition.Builder()
                .target(boundsCenter.toMapBoxLatLng())
                .zoom(CITY_ZOOM_VALUE)
                .build()

            this.mapBoxMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                CAMERA_ANIMATION_DURATION
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}