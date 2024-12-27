package com.example.factory_map_project.util.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import timber.log.Timber

class CustomClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<FactoryCluster>
) : DefaultClusterRenderer<FactoryCluster>(context, map, clusterManager) {
    init {
        this.minClusterSize = 30
    }
    // 클러스터 아이템 렌더링 전 처리
    override fun onBeforeClusterItemRendered(item: FactoryCluster, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        // 예시: 마커의 색상이나 이미지 변경 가능
        if(item.isClick){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }else{
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }
    }

    // 클러스터 아이템이 업데이트될 때 처리
    override fun onClusterItemUpdated(item: FactoryCluster, marker: Marker) {
        super.onClusterItemUpdated(item, marker)
        // 예시: 마커의 스타일 업데이트
        Timber.d("onClusterItemUpdated")
//        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
    }

    // 클러스터 렌더링 전 처리
    override fun onBeforeClusterRendered(cluster: Cluster<FactoryCluster>, markerOptions: MarkerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions)
        // 예시: 클러스터의 크기나 스타일을 조정 가능
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    }

    // 클러스터 렌더링 후 처리
    override fun onClusterRendered(cluster: Cluster<FactoryCluster>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        // 예시: 클러스터 마커의 크기나 스타일을 변경할 수 있음
    }

    override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<FactoryCluster>?) {
        Timber.d("setOnClusterItemClickListener")
        super.setOnClusterItemClickListener(listener)
    }
}