package com.example.meep_android_test.features.entrypoint.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.meep_android_test.R
import com.example.meep_android_test.data.domainmodels.ResourcesMapBounds
import com.example.meep_android_test.data.utils.ConsumableObserver
import com.example.meep_android_test.databinding.EntryPointFragmentBinding
import com.example.meep_android_test.features.entrypoint.business.EntryPointViewModel
import com.example.meep_android_test.features.entrypoint.business.createEntryPointViewModel

class EntryPointFragment : Fragment() {

    private lateinit var onNavigateToMap: (ResourcesMapBounds) -> Unit

    private val viewModel: EntryPointViewModel by viewModels { createEntryPointViewModel() }

    private val navigationTriggerObserver = ConsumableObserver<ResourcesMapBounds> {
        onNavigateToMap(it)
    }

    fun configure(onNavigateToMap: (ResourcesMapBounds) -> Unit) {
        this.onNavigateToMap = onNavigateToMap
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<EntryPointFragmentBinding>(inflater, R.layout.entry_point_fragment, container, false)
        .apply { presenter = viewModel.presenter }
        .root

    override fun onResume() {
        super.onResume()
        viewModel.viewState.navigationTrigger.observe(this, navigationTriggerObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.viewState.navigationTrigger.removeObserver(navigationTriggerObserver)
    }

}