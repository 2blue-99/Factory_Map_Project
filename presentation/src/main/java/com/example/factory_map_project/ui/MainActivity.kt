package com.example.factory_map_project.ui

import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.domain.model.AllAreaInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ActivityMainBinding
import com.example.factory_map_project.ui.base.BaseActivity
import com.example.factory_map_project.util.CommonUtil.moveSettingIntent
import com.example.factory_map_project.util.CommonUtil.repeatOnStarted
import com.example.factory_map_project.util.DialogUtil
import com.example.factory_map_project.util.PermissionUtil
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
) {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    override val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    lateinit var dialogManager: DialogUtil

    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ resultList ->
        var permissionState = true
        for(result in resultList){
            if(!result.value){
                permissionState = false
            }
        }
        if(permissionState){
            startObserveLocation()
        }
    }

    val settingLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(PermissionUtil(this).checkLocationPermission()){
            startObserveLocation()
        }else{
            Timber.d("권한 거부")
        }
    }

    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun setData() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 회전 불가 처리

        setBackPressListener()
        dialogManager = DialogUtil(this)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        binding.navBottom.setupWithNavController(navHostFragment.navController)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkMapPermission()

        lifecycleScope.launch {
            if(viewModel.connectedState.first()){
                // 데이터 가져오기
                viewModel.getRemoteData()
            }
        }

//        lifecycleScope.launch {
//            if(!viewModel.checkDownload()){
//                showDownloadBottomDialog()
//            }
//        }
    }

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowPopup -> dialogManager.showMessageDialog(event)
                    is AppEvent.ShowToast -> showToast(event.message)
                    is AppEvent.ShowLoading -> {
                        Timber.d("loading state : ${event.state}")
                        setLoading(event.state)
                    }
                    is AppEvent.MovePage -> {}
                    else -> {}
                }
            }
        }
    }

    override fun setListener() {  }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun geoTest(list: List<AllAreaInfo>){
        val geo = Geocoder(this)
        lifecycleScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val start = System.currentTimeMillis()
                for(data in list){
                    val address = data.roadAddress
                    if(address.isNotBlank()){
                        geo.getFromLocationName(address, 1){
                            Timber.d("geo : ${it[0]}")
                        }
                    }
                }
                val end = System.currentTimeMillis()
                Timber.d("결과 ${end - start}ms")
            }
        }
    }

    private fun setBackPressListener(){
        onBackPressedDispatcher.addCallback(this) {
            var gap = findNavController(R.id.nav_host).currentDestination
            Timber.d("gap : $gap")
            if(findNavController(R.id.nav_host).currentDestination?.id == R.id.mapsFragment){
                onBackPressedFinish()
            }else{
                findNavController(R.id.nav_host).popBackStack()
            }
            gap = findNavController(R.id.nav_host).currentDestination
            Timber.d("gap : $gap")
        }
    }

    private fun startObserveLocation(){
        Timber.d("startObserveLocation")
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).apply {
            setMinUpdateIntervalMillis(1000L) // 최소 업데이트 간격 2초
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setWaitForAccurateLocation(true)
        }.build()

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                lifecycleScope.launch {
                    result.lastLocation?.let { location ->
                        Timber.i("location : $location")
                        viewModel.currentLocation.emit(
                            value = LatLng(location.latitude, location.longitude)
                        )
                    }
                }
            }
        }

        if(PermissionUtil(this).checkLocationPermission()){
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun checkMapPermission(){
        with(PermissionUtil(this)){
            if(checkLocationPermission()){
                startObserveLocation()
            }else if(!checkLocationRejectPermission()){
                try {
                    dialogManager.showMessageDialog(
                        popup = PopupContent.NOTICE_PERMISSION,
                        positiveCallBack = { settingLauncher.launch(moveSettingIntent()) },
                        negativeCallBack = {},
                        args = arrayOf("위치정보 접근")
                    )
                }catch (e: Exception){
                    Timber.d("err : $e")
                }
            }else{
                locationPermissionLauncher.launch(locationPermission)
            }
        }
    }
}