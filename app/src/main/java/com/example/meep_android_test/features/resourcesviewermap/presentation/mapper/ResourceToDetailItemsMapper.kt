package com.example.meep_android_test.features.resourcesviewermap.presentation.mapper

import android.content.Context
import com.example.meep_android_test.R
import com.example.meep_android_test.data.domainmodels.Resource

fun Resource.Moped.getMopedDetailItems(): List<Pair<Int, String>> {
    val detailItems = mutableListOf<Pair<Int, String>>()

    resourceType?.let { detailItems.add(R.string.resource_type to it) }
    model?.let { detailItems.add(R.string.resource_model to it) }
    batteryLevel?.let { detailItems.add(R.string.resource_battery_level to it.toString()) }
    helmets?.let { detailItems.add(R.string.resource_helmets to it.toString()) }
    licencePlate?.let { detailItems.add(R.string.resource_license_plate to it) }

    return detailItems
}

fun Resource.BikeStation.getBikeStationDetailItems(context: Context): List<Pair<Int, String>> {
    val detailItems = mutableListOf<Pair<Int, String>>()

    bikesAvailable?.let { detailItems.add(R.string.resource_bikes_available to it.toString()) }
    spacesAvailable?.let { detailItems.add(R.string.resource_space_available to it.toString()) }
    allowDropoff?.let {
        val value =
            context.getString(if (it) R.string.resource_allow_drop_off_yes else R.string.resource_allow_drop_off_no)
        detailItems.add(R.string.resource_battery_level to value)
    }

    return detailItems
}