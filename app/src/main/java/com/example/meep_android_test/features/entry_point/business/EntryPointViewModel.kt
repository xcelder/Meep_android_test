package com.example.meep_android_test.features.entry_point.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.meep_android_test.features.entry_point.presentation.EntryPointViewState

class EntryPointViewModel(
    val viewState: EntryPointViewState,
    val presenter: EntryPointPresenter
) : ViewModel() {
    internal class Factory(
        private val viewState: EntryPointViewState,
        private val presenter: EntryPointPresenter
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            EntryPointViewModel(viewState, presenter) as T
    }
}

internal fun createEntryPointViewModel(): EntryPointViewModel.Factory {
    val viewState = EntryPointViewState()
    val presenter = EntryPointPresenter(viewState)
    return EntryPointViewModel.Factory(viewState, presenter)
}