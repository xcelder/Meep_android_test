package com.example.meep_android_test.features.resources_viewer_map.presentation.binding_adapter

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}