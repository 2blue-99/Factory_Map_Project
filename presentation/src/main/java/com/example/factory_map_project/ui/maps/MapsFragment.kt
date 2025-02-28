package com.example.factory_map_project.ui.maps

import android.location.Geocoder
import android.os.Build
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.domain.type.AreaType
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.FragmentMapsBinding
import com.example.factory_map_project.ui.MainActivity
import com.example.factory_map_project.ui.MainViewModel
import com.example.factory_map_project.ui.base.BaseFragment
import com.example.factory_map_project.util.PermissionUtil
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.CommonUtil.moveSettingIntent
import com.example.factory_map_project.util.CommonUtil.repeatOnFragmentStarted
import com.example.factory_map_project.util.CommonUtil.toNoCountry
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.BitmapHelper
import com.example.factory_map_project.util.map.CustomClusterRenderer
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(
    FragmentMapsBinding::inflate
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: MapsViewModel by viewModels()
    val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<FactoryCluster>
    private var currentMarker: Marker? = null
    private var longClickItem: Boolean = false

    var isInit:Boolean = false

    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        setClusterManager()
        setDaoListener()

        if(!isInit) { // 최초 진입 한정하여 설정
            setUserMarker()
            initMap(false)
            initSetting()
            isInit = true
        }
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
        repeatOnFragmentStarted {
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
                        findNavController().navigate(event.id)
                    }
                    is AppEvent.Action<*> -> {
                        when(event.type){
                            ActionType.MY_LOCATION -> {
                                moveMyLocation()
                            }
                            ActionType.POSITION_INIT -> {
                                initMap(true)
                            }
                            else -> {}
                        }
                    }
                    is AppEvent.GetLocation -> {
                        val gap = googleMap.projection.visibleRegion.latLngBounds.center
                        Timber.d("googleMap.cameraPosition.zoom : ${googleMap.cameraPosition.zoom}")
                        event.tryEmit(Pair(gap, googleMap.cameraPosition.zoom))
                    }
                    else -> {}
                }
            }
        }

        repeatOnFragmentStarted {
            mainViewModel.currentLocation.collectLatest {
                currentMarker = addUserMarker(it)
            }
        }

        binding.disconnectIcon.setOnClickListener {
            mainActivity().showSnackBar(it, PopupContent.MAP_DISCONNECT.content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        googleMap.clear()
    }

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun initMap(smooth: Boolean){
        val location = LatLng(36.65829047550215, 127.87665870040657)
        moveCamera(location, 7.4f, smooth)
    }

    private fun setClusterManager() {
        clusterManager = ClusterManager<FactoryCluster>(requireContext(), googleMap)

        lifecycleScope.launch {
            val customClusterRenderer = CustomClusterRenderer(
                context = requireContext(),
                map = googleMap,
                clusterManager = clusterManager,
                clusterSize = viewModel.getClusterTriggerSize()
            )

            clusterManager.renderer = customClusterRenderer

            googleMap.setOnCameraIdleListener(clusterManager)
            googleMap.setOnMarkerClickListener(clusterManager)

            clusterManager.setOnClusterClickListener { onClickCluster(it) }
            clusterManager.setOnClusterItemClickListener { onClickMarker(it) }
        }

        googleMap.setOnCameraMoveListener {
//            googleMap.projection.visibleRegion.latLngBounds.center
//            Timber.d("현재 줌 : ${googleMap.cameraPosition.zoom}")
//            Timber.d("현재 위치 : ${googleMap.cameraPosition.target}")
            viewModel.isRefresh.value = true
        }

        googleMap.setOnMapLongClickListener {
            mainActivity().dialogManager.showMessageDialog(PopupContent.MAP_ADD_ITEM){
                addItemHandler(it)
            }
        }
    }

    private fun setDaoListener() {
        repeatOnFragmentStarted {
            viewModel.factoryData.collect { list ->
                    Timber.d("fragmen list size : ${list.size}")
                    clusterManager.clearItems()
                    clusterManager.addItems(list)
                    clusterManager.cluster()

                    // 롱클릭 추가로 인한 변경일 경우 리스트 마지막 값을 바텀 시트에 노출
                    if(longClickItem){
                        onClickMarker(list.last())
                        longClickItem = false
                    }
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
        setCamera(item.position)
        mainActivity().showMarkerBottomDialog(
            item = item,
            updateCluster = { updateItem ->
                clusterManager.clearItems()
                viewModel.updateFactory(updateItem)
            },
            deleteCluster = {
                clusterManager.clearItems()
                viewModel.deleteFactory(item.id)
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

    /**
     * 현재 Zoom 이 10보다 작을경우 11로 줌
     * 아닐 경우 줌 없이 카메라 이동
     */
    private fun setCamera(item: LatLng?){
        item?.let {
            val currentZoom = googleMap.cameraPosition.zoom
            val targetZoom = if(currentZoom < 11) 11f else currentZoom
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(item, targetZoom))
        }
    }

    private fun addUserMarker(location: LatLng): Marker? {
        Timber.d("addUserMarker : $currentMarker")
        currentMarker?.remove()
        return googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("현위치")
                .alpha(0.85f)
                .zIndex(100f)
                .icon(BitmapHelper(requireContext(), R.drawable.icon_current_location))
        )
    }

    private fun moveCamera(location: LatLng, zoom: Float, smooth: Boolean){
        if(smooth){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        }else{
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        }
    }

    private fun moveMyLocation(){
        with(PermissionUtil(requireActivity())){
            if(!checkLocationPermission()){
                // TODO 이거 재활용시킬 수 있을 듯
                mainActivity().dialogManager.showMessageDialog(
                    popup = PopupContent.NOTICE_PERMISSION,
                    positiveCallBack = { mainActivity().settingLauncher.launch(moveSettingIntent()) },
                    negativeCallBack = {},
                    args = arrayOf("위치정보 접근")
                )
            }else{
                currentMarker?.let { marker ->
                    val currentZoom = googleMap.cameraPosition.zoom
                    val targetZoom = if(currentZoom > 14f) currentZoom else 14f
                    moveCamera(marker.position, targetZoom, true)
                } ?: mainActivity().showToast("잠시만 기다려주세요.")
            }
        }
    }

    /**
     * 다른 화면으로 복귀 시, 유저 위치가 늦게 찍히는 이슈 보완
     */
    private fun setUserMarker(){
        currentMarker?.let { addUserMarker(it.position) }
    }

    /**
     *  업체 추가 핸들러 | 버전에 따른 Geocode 함수 분기 처리
     */
    private fun addItemHandler(latLng: LatLng){
        val geocoder = Geocoder(requireContext())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1){
                // View 작업을 위해 Main Thread 변경
                lifecycleScope.launch {
                    val noCountryAddress = it[0].getAddressLine(0).toNoCountry()
                    addItem(noCountryAddress, latLng)
                }
            }
        } else {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.let {
                // View 작업을 위해 Main Thread 변경
                lifecycleScope.launch {
                    val noCountryAddress = it[0].getAddressLine(0).toNoCountry()
                    addItem(noCountryAddress, latLng)
                }
            }
        }
    }

    /**
     * 업체 추가 | 선택 위치에 벗어날 경우 다이알로그
     *
     */
    private fun addItem(itemAddress: String, latLng: LatLng){
        val selectedOptionArea = viewModel.selectedAreaType.value?.title ?: AreaType.ALL.title
        val item = FactoryCluster.toLongClickItem(itemAddress, latLng)
        if(selectedOptionArea != AreaType.ALL.title && !itemAddress.startsWith(selectedOptionArea)){
            val itemFirstArea = itemAddress.split(" ")[0]
            mainActivity().dialogManager.showMessageDialog(
                popup = PopupContent.MAP_NO_MATCH_AREA,
                positiveCallBack = {
                    longClickItem = true
                    viewModel.updateFactory(item)
                    viewModel.changeOptionArea(itemFirstArea)
                },
                selectedOptionArea,itemFirstArea,itemFirstArea
            )
        }else{
            longClickItem = true
            viewModel.updateFactory(item)
        }
    }

    private fun initSetting(){
        val koreaRange = LatLngBounds(
            LatLng(33.0, 125.0), // 하단, 왼쪽
            LatLng(38.5, 130.0), // 상단 , 오른쪽
        )
        googleMap.setLatLngBoundsForCameraTarget(koreaRange)
        googleMap.setMinZoomPreference(7.1f)
        googleMap.setMaxZoomPreference(19.5f)
    }
}

