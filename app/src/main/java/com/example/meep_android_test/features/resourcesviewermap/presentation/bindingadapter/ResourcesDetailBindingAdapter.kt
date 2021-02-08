package com.example.meep_android_test.features.resourcesviewermap.presentation.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.features.resourcesviewermap.presentation.adapter.ResourceDetailAdapter
import com.example.meep_android_test.features.resourcesviewermap.presentation.mapper.getBikeStationDetailItems
import com.example.meep_android_test.features.resourcesviewermap.presentation.mapper.getMopedDetailItems

@BindingAdapter("resourceTitle")
fun setResourceTitle(textView: TextView, resource: Resource) {
    val title = if (resource is Resource.Moped) {
        with(resource) { "$resourceType $model $name" }
    } else {
        resource.name
    }

    textView.text = title
}

@BindingAdapter("resourcesDataSource")
fun setResourcesDataSource(recyclerView: RecyclerView, resource: Resource) {
    val detailItems: List<Pair<Int, String>> = when (resource) {
        is Resource.Moped -> resource.getMopedDetailItems()
        is Resource.BikeStation -> resource.getBikeStationDetailItems(recyclerView.context)
        else -> emptyList()
    }

    if (recyclerView.adapter == null) {
        recyclerView.adapter = ResourceDetailAdapter()
    }

    (recyclerView.adapter as ResourceDetailAdapter).bindItems(detailItems)
}