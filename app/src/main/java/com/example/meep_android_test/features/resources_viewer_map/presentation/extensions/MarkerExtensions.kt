package com.example.meep_android_test.features.resources_viewer_map.presentation.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.meep_android_test.R
import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView

internal fun ResourceResponseItem.toMarkerView(
    context: Context,
    drawable: Drawable?,
    onMarkerClick: (ResourceResponseItem) -> Unit = {}
): MarkerView {
    val imageView = ImageView(context).apply {
        val markerSize = context.resources.getDimensionPixelSize(R.dimen.marker_size)
        layoutParams = FrameLayout.LayoutParams(markerSize, markerSize)
        setImageDrawable(drawable)
    }

    imageView.setOnClickListener { onMarkerClick(this@toMarkerView) }

    return MarkerView(LatLng(y, x), imageView)
}