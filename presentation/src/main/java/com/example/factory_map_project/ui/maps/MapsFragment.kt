package com.example.factory_map_project.ui.maps

import androidx.fragment.app.viewModels
import com.example.domain.model.GyeonggiInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.FragmentMapsBinding
import com.example.factory_map_project.ui.MainActivity
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.Util.toCluster
import com.example.factory_map_project.util.map.CustomClusterRenderer
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(
    FragmentMapsBinding::inflate
) {
    override val viewModel: MapsViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<FactoryCluster>

    private val callback = OnMapReadyCallback { map ->
        Timber.d("OnMapReadyCallback")
        googleMap = map
        initMap()
        setClusterManager()
    }


    /**
     * //////////////////////////////////////////////////////////////////////////////
     * //////////////////////////////    Lifecycle     //////////////////////////////
     * //////////////////////////////////////////////////////////////////////////////
     */
    override fun setData() {}

    override fun setUI() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun setObserver() {
        viewModel.gyeonggiLiveData.observe(this) {
            inputData(it)
        }
    }





    private fun initMap(){
        val test = LatLng(37.5073218717, 127.6164271659)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 8.1f))
    }

    private fun setClusterManager() {
        clusterManager = ClusterManager<FactoryCluster>(requireContext(), googleMap)
        val customClusterRenderer = CustomClusterRenderer(requireContext(), googleMap, clusterManager)
        clusterManager.renderer = customClusterRenderer

        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)

        clusterManager.setOnClusterClickListener { onClickCluster(it) }
        clusterManager.setOnClusterItemClickListener { onClickMarker(it) }
    }

    private fun inputData(list: List<GyeonggiInfo>) {
        clusterManager.addItems(list.map { it.toCluster() })
    }

    private fun onClickCluster(cluster: Cluster<FactoryCluster>): Boolean {
        Timber.d("setOnClusterClickListener")
        val clusterCenter = cluster.position
        val currentZoom = googleMap.cameraPosition.zoom
        val targetZoom = if(currentZoom >= 13) 15f else currentZoom + 2
        if(currentZoom < 15f) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(clusterCenter, targetZoom))
        }
        return true
    }

    private fun onClickMarker(item: FactoryCluster): Boolean {
        val targetMarker = clusterManager.markerCollection.markers.find { it.position  == item.position }
        targetMarker?.let { marker ->
            (activity as MainActivity).openMarkerBottomSheet(item, marker)
        }
        return false
    }
}