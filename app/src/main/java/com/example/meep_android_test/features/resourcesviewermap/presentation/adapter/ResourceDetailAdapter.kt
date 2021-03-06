package com.example.meep_android_test.features.resourcesviewermap.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meep_android_test.R

class ResourceDetailAdapter: RecyclerView.Adapter<ResourceDetailViewHolder>() {

    private var detailItems: List<Pair<Int, String>> = emptyList()

    fun bindItems(items: List<Pair<Int, String>>) {
        detailItems = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceDetailViewHolder =
        ResourceDetailViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.resource_detail_property_item, parent, false)
        )

    override fun getItemCount(): Int = detailItems.count()

    override fun onBindViewHolder(holder: ResourceDetailViewHolder, position: Int) {
        holder.bind(detailItems[position])
    }
}
