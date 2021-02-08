package com.example.meep_android_test.features.resources_viewer_map.presentation.parser

import android.content.Context
import com.example.meep_android_test.R
import com.example.meep_android_test.data.network_models.ResourceResponseItem

fun ResourceResponseItem.getMopedDetailItems(context: Context): List<Pair<String, String>> {
    val detailItems = mutableListOf<Pair<String, String>>()

    with(context) {
        resourceType?.let { detailItems.add("${getString(R.string.resource_type)}:" to it) }
        model?.let { detailItems.add("${getString(R.string.resource_model)}:" to it) }
        batteryLevel?.let { detailItems.add("${getString(R.string.resource_battery_level)}:" to it.toString()) }
        helmets?.let { detailItems.add("${getString(R.string.resource_helmets)}:" to it.toString()) }
        licencePlate?.let { detailItems.add("${getString(R.string.resource_license_plate)}:" to it) }
    }

    return detailItems
}

fun ResourceResponseItem.getBikeStationDetailItems(context: Context): List<Pair<String, String>> {
    val detailItems = mutableListOf<Pair<String, String>>()

    with(context) {
        bikesAvailable?.let { detailItems.add("${getString(R.string.resource_bikes_available)}:" to it.toString()) }
        spacesAvailable?.let { detailItems.add("${getString(R.string.resource_space_available)}:" to it.toString()) }
        allowDropoff?.let {
            val value = getString(if (it) R.string.resource_allow_drop_off_yes else R.string.resource_allow_drop_off_no)
            detailItems.add("${getString(R.string.resource_battery_level)}:" to value)
        }
    }

    return detailItems
}