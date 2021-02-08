package com.example.meep_android_test.features.resources_viewer_map.presentation.binding_adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.data.network_models.isBikeStation
import com.example.meep_android_test.data.network_models.isMoped
import com.example.meep_android_test.features.resources_viewer_map.presentation.adapter.ResourceDetailAdapter
import com.example.meep_android_test.features.resources_viewer_map.presentation.parser.getBikeStationDetailItems
import com.example.meep_android_test.features.resources_viewer_map.presentation.parser.getMopedDetailItems

@BindingAdapter("resourceTitle")
fun setResourceTitle(textView: TextView, resourceResponseItem: ResourceResponseItem) {
    val title = if (resourceResponseItem.isMoped) {
        with(resourceResponseItem) { "$resourceType $model $name" }
    } else {
        resourceResponseItem.name
    }

    textView.text = title
}

@BindingAdapter("resourcesDataSource")
fun setResourcesDataSource(recyclerView: RecyclerView, resourceResponseItem: ResourceResponseItem) {
    val detailItems: List<Pair<String, String>> = when {
        resourceResponseItem.isMoped -> resourceResponseItem.getMopedDetailItems(recyclerView.context)
        resourceResponseItem.isBikeStation -> resourceResponseItem.getBikeStationDetailItems(recyclerView.context)
        else -> emptyList()
    }

    if (recyclerView.adapter == null) {
        recyclerView.adapter = ResourceDetailAdapter()
    }

    (recyclerView.adapter as ResourceDetailAdapter).bindItems(detailItems)
}