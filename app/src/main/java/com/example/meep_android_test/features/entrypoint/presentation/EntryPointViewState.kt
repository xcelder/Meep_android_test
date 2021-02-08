package com.example.meep_android_test.features.entrypoint.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.meep_android_test.data.domainmodels.ResourcesMapBounds
import com.example.meep_android_test.data.utils.Consumable

class EntryPointViewState {

    val navigationTrigger: LiveData<Consumable<ResourcesMapBounds>>
        get() = _navigationTrigger

    private val _navigationTrigger = MutableLiveData<Consumable<ResourcesMapBounds>>()

    fun navigate(locationBounds: ResourcesMapBounds) {
        _navigationTrigger.value = Consumable(locationBounds)
    }
}