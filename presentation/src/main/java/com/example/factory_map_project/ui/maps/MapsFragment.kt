package com.example.factory_map_project.ui.maps

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.FragmentMapsBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.Util.slideRightBaseNavOptions
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.CustomClusterRenderer
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(
    FragmentMapsBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: MapsViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<FactoryCluster>

    private val callback = OnMapReadyCallback { map ->
        Timber.d("OnMapReadyCallback")
        googleMap = map
        initMap()
        setClusterManager()
        setDaoListener()
    }


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {}

    override fun setUI() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST){}
        mapFragment?.getMapAsync(callback)
    }

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowSpinnerDialog -> {
                        mainActivity().showSpinnerDialog(
                            list = event.content,
                            position = event.position,
                            onSelect = {
                                event.onSelect(it)
                            }
                        )
                    }

                    is AppEvent.MovePage -> {
                        findNavController().navigate(R.id.settingFragment, slideRightBaseNavOptions())
                    }
                    else -> {}
                }
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
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

    private fun setDaoListener() {
        repeatOnStarted {
            viewModel.factoryData
                .filter { it.isNotEmpty() }
                .collect { list ->
                    Timber.d("list : ${list.size}")
                    clusterManager.clearItems()
                    clusterManager.addItems(list)
                    clusterManager.cluster()
                }
        }
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
        setCamera(item)
        mainActivity().showMarkerBottomDialog(
            item = item,
            updateCluster = { updateItem ->
                viewModel.updateFactory(updateItem)
//                updateCluster(item, updateItem)
            },
            deleteCluster = {
                viewModel.deleteFactory(item.id)
//                deleteCluster(item)
            }
        )
        return true
    }

//    private fun updateCluster(oldItem: FactoryCluster, newItem: FactoryCluster){
//        Timber.d("old: $oldItem")
//        Timber.d("new: $newItem")
//        clusterManager.removeItem(oldItem)
//        clusterManager.addItem(newItem)
//        clusterManager.cluster()
//    }

//    private fun deleteCluster(item: FactoryCluster){
//        Timber.d("삭제")
//        clusterManager.removeItem(item)
//        clusterManager.cluster()
//    }

    private fun setCamera(item: FactoryCluster){
        if(googleMap.cameraPosition.zoom < 13f) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(item.position, 15f))
        }
    }
}