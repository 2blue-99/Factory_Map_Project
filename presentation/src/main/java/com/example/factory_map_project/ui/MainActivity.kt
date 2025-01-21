package com.example.factory_map_project.ui

import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.AllAreaInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ActivityMainBinding
import com.example.factory_map_project.ui.base.BaseActivity
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.AppEvent

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
) {
    override val viewModel: MainViewModel by viewModels()

    override fun setData() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 회전 불가 처리

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        binding.navBottom.setupWithNavController(navHostFragment.navController)

        lifecycleScope.launch {
            if(viewModel.checkDownload()){
                openDownloadBottomDialog()
            }
        }
    }

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is AppEvent.ShowPopup -> showDialog(event)
                    is AppEvent.ShowSpinnerDialog -> {
                        openSpinnerDialog(event.content, event.position, event.onSelect)
                    }
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

}