package com.example.factory_map_project.util.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class FactoryCluster(
    private val title: String,
    private val add: String,
    private val lat: Double,
    private val lng: Double
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(lat, lng)
    override fun getTitle(): String = title
    override fun getSnippet(): String = add
}