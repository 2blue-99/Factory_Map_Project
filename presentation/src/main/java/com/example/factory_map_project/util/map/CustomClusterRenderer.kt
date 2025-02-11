package com.example.factory_map_project.util.map

import android.content.Context
import com.example.factory_map_project.R
import com.example.factory_map_project.util.STATE_SUCCESS
import com.example.factory_map_project.util.STATE_UNKNOWN
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
        markerOptions.icon(markIcon(item.isClick)).alpha(0.90f)
    }

    // 클러스터 아이템이 업데이트될 때 처리
    override fun onClusterItemUpdated(item: FactoryCluster, marker: Marker) {
        super.onClusterItemUpdated(item, marker)
        marker.setIcon(markIcon(item.isClick))
        marker.alpha = 0.90f
    }

    // 클러스터 렌더링 전 처리
    override fun onBeforeClusterRendered(cluster: Cluster<FactoryCluster>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
        // 예시: 클러스터의 크기나 스타일을 조정 가능
    }

    // 클러스터 렌더링 후 처리
    override fun onClusterRendered(cluster: Cluster<FactoryCluster>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        // 예시: 클러스터 마커의 크기나 스타일을 변경할 수 있음
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<FactoryCluster>?) {
//        Timber.d("setOnClusterItemClickListener")
        super.setOnClusterItemClickListener(listener)
    }

    private fun markIcon(state: Int): BitmapDescriptor {
        return when(state){
            STATE_UNKNOWN -> BitmapHelper(context, R.drawable.icon_unknown_marker)
            STATE_SUCCESS -> BitmapHelper(context, R.drawable.icon_success_marker)
            else -> BitmapHelper(context, R.drawable.icon_fail_marker)
        }?: BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
    }
}