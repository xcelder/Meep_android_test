package com.example.meep_android_test.features.resources_viewer_map.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.meep_android_test.R
import com.example.meep_android_test.data.ui_models.*
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment

private const val LOWER_LEFT_ARG = "lower_left_arg"
private const val UPPER_RIGHT_ARG = "upper_right_arg"

class ResourcesViewerMapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun withArguments(locationBounds: ResourcesMapBounds): ResourcesViewerMapFragment =
            ResourcesViewerMapFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(LOWER_LEFT_ARG, locationBounds.lowerLeftLatLng.toString())
                        putSerializable(UPPER_RIGHT_ARG, locationBounds.upperRightLatLng.toString())
                    }
                }
    }

    private val mapView: SupportMapFragment by lazy { childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment }

    private lateinit var gMap: GoogleMap

    private val lowerLeft: LatLng? by lazy { arguments?.getString(LOWER_LEFT_ARG)?.toLatLng() }
    private val upperRight: LatLng? by lazy { arguments?.getString(UPPER_RIGHT_ARG)?.toLatLng() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.resources_viewer_map_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
    }
}