package com.example.meep_android_test.features.resources_viewer_map.presentation.extensions

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.meep_android_test.R
import com.example.meep_android_test.data.network_models.ResourceResponseItem
import com.example.meep_android_test.databinding.ResourceDetailBottomSheetBinding
import com.example.meep_android_test.features.resources_viewer_map.presentation.ResourcesViewerMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

fun ResourcesViewerMapFragment.buildResourcesDetailBottomSheet(resourceResponseItem: ResourceResponseItem) =
    BottomSheetDialog(requireContext()).apply {
        val view = DataBindingUtil.inflate<ResourceDetailBottomSheetBinding>(
            LayoutInflater.from(context),
            R.layout.resource_detail_bottom_sheet,
            null,
            false
        )
            .apply { resourceItem = resourceResponseItem }
            .root
        setContentView(view)
    }