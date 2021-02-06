package com.example.meep_android_test.features.entry_point.business

import com.example.meep_android_test.data.mocks.getLisbonBounds
import com.example.meep_android_test.features.entry_point.presentation.EntryPointViewState

class EntryPointPresenter(private val viewState: EntryPointViewState) {

    fun onNavigateToLisbon() {
        val lisbonResourcesBounds = getLisbonBounds()
        viewState.navigate(lisbonResourcesBounds)
    }
}