package com.example.factory_map_project.util

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.GyeonggiInfo
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object Util {

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    /**
     * 상태바 투명 처리
     */
    fun Activity.setStatusBarTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun GoogleMap.setMarker(latitude: Double, longitude: Double, name: String = ""): Marker? {
        val location = LatLng(latitude, longitude)
        return this.addMarker(MarkerOptions().position(location).title(name))
    }

    fun GyeonggiInfo.toCluster(): FactoryCluster =
        FactoryCluster(
            title = this.companyName,
            add = this.loadNameAddress,
            lat = this.latitude,
            lng = this.longitude
        )
}