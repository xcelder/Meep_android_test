package com.example.meep_android_test.features.resourcesviewermap.presentation.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.data.networkmodels.ResourceResponseItem
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView

class CustomMarkerView(
    context: Context,
    drawable: Drawable?,
    resourceResponseItem: Resource,
    onMarkerClick: (Resource) -> Unit = {}
) : MarkerView(
    LatLng(resourceResponseItem.lat, resourceResponseItem.lng),
    ImageView(context).apply {
        val markerSize = context.resources.getDimensionPixelSize(com.example.meep_android_test.R.dimen.marker_size)
        layoutParams = android.widget.FrameLayout.LayoutParams(markerSize, markerSize)
        setImageDrawable(drawable)
        setOnClickListener { onMarkerClick(resourceResponseItem) }
    }
)