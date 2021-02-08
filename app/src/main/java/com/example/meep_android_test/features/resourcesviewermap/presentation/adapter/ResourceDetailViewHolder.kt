package com.example.meep_android_test.features.resourcesviewermap.presentation.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.meep_android_test.databinding.ResourceDetailPropertyItemBinding

class ResourceDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(detailItem: Pair<Int, String>) {
        DataBindingUtil.bind<ResourceDetailPropertyItemBinding>(itemView)
            ?.apply {
                name = "${root.context.getString(detailItem.first)}:"
                value = detailItem.second
            }
    }
}