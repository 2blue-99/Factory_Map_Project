package com.example.factory_map_project.util.map

import android.content.Context
import com.example.factory_map_project.R
import com.example.factory_map_project.util.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import timber.log.Timber

class CustomClusterRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<FactoryCluster>,
    clusterSize: Int
) : DefaultClusterRenderer<FactoryCluster>(context, map, clusterManager) {
    init {
        Timber.d("size : $clusterSize")
        this.minClusterSize = clusterSize
    }

    // 클러스터 아이템 렌더링 전 처리
    override fun onBeforeClusterItemRendered(item: FactoryCluster, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        // 예시: 마커의 색상이나 이미지 변경 가능
        with(markerOptions){
            icon(setIcon(item.category, item.isCheck))
            alpha(if(item.isCheck) CHECK_ALPHA else NONE_CHECK_ALPHA)
        }
    }

    // 클러스터 아이템이 업데이트될 때 처리
    override fun onClusterItemUpdated(item: FactoryCluster, marker: Marker) {
        super.onClusterItemUpdated(item, marker)
        with(marker){
            setIcon(setIcon(item.category, item.isCheck))
            alpha = if(item.isCheck) CHECK_ALPHA else NONE_CHECK_ALPHA
        }
    }

    // 클러스터 렌더링 전 처리
    override fun onBeforeClusterRendered(cluster: Cluster<FactoryCluster>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
    }

    // 클러스터 렌더링 후 처리
    override fun onClusterRendered(cluster: Cluster<FactoryCluster>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
    }

    private fun setIcon(category: Int, check: Boolean): BitmapDescriptor {
        return when(category){
            MARKER_UNKNOWN -> BitmapHelper(context, if(check) R.drawable.icon_unknown_check_marker else R.drawable.icon_unknown_marker)
            MARKER_HOTEL -> BitmapHelper(context, if(check) R.drawable.icon_hotel_check else R.drawable.icon_hotel)
            MARKER_RESORT -> BitmapHelper(context, if(check) R.drawable.icon_resort_check else R.drawable.icon_resort)
            else -> BitmapHelper(context, R.drawable.icon_unknown_marker)
        }?: BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    }

    private fun setAlpha(){

    }
}