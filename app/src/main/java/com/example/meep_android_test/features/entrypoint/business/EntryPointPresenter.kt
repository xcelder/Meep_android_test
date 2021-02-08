package com.example.meep_android_test.features.entrypoint.business

import com.example.meep_android_test.data.mocks.getLisbonBounds
import com.example.meep_android_test.features.entrypoint.presentation.EntryPointViewState

class EntryPointPresenter(private val viewState: EntryPointViewState) {

    fun onNavigateToLisbon() {
        val lisbonResourcesBounds = getLisbonBounds()
        viewState.navigate(lisbonResourcesBounds)
    }
}