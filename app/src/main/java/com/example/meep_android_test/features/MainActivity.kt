package com.example.meep_android_test.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.meep_android_test.R
import com.example.meep_android_test.data.domainmodels.ResourcesMapBounds
import com.example.meep_android_test.features.entrypoint.presentation.EntryPointFragment
import com.example.meep_android_test.features.resourcesviewermap.presentation.ResourcesViewerMapFragment

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