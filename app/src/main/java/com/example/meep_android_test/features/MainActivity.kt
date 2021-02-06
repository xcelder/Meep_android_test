package com.example.meep_android_test.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.meep_android_test.R
import com.example.meep_android_test.data.ui_models.ResourcesMapBounds
import com.example.meep_android_test.features.entry_point.presentation.EntryPointFragment
import com.example.meep_android_test.features.resources_viewer_map.presentation.ResourcesViewerMapFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onStart() {
        super.onStart()
        loadInitialFragment()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is EntryPointFragment -> fragment.configure(onNavigateToMap = ::loadResourcesMapFragment)
        }
    }

    private fun loadInitialFragment() {
        loadFragment(EntryPointFragment())
    }

    private fun loadResourcesMapFragment(locationBounds: ResourcesMapBounds) {
        val resourcesViewerMapFragment = ResourcesViewerMapFragment.withArguments(locationBounds)
        loadFragment(resourcesViewerMapFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}