package com.example.meep_android_test.features.resourcesviewermap.presentation.extensions

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.meep_android_test.R
import com.example.meep_android_test.data.domainmodels.Resource
import com.example.meep_android_test.databinding.ResourceDetailBottomSheetBinding
import com.example.meep_android_test.features.resourcesviewermap.presentation.ResourcesViewerMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

fun ResourcesViewerMapFragment.buildResourcesDetailBottomSheet(resource: Resource) =
    BottomSheetDialog(requireContext()).apply {
        val view = DataBindingUtil.inflate<ResourceDetailBottomSheetBinding>(
            LayoutInflater.from(context),
            R.layout.resource_detail_bottom_sheet,
            null,
            false
        )
            .apply { this.resource = resource }
            .root
        setContentView(view)
    }