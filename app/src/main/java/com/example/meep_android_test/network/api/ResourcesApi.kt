package com.example.meep_android_test.network.api

import com.example.meep_android_test.data.networkmodels.ResourceResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ResourcesApi {

    @GET("tripplan/api/v1/routers/lisboa/resources")
    suspend fun getLisbonResourcesInBounds(
        @Query("lowerLeftLatLon") lowerLeftLatLon: String,
        @Query("upperRightLatLon") upperRightLatLon: String
    ): List<ResourceResponseItem>
}