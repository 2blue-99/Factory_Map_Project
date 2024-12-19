package com.example.factory_map_project.ui

import android.content.pm.ActivityInfo
import android.location.Geocoder
import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.ActivityMainBinding
import com.example.factory_map_project.ui.base.BaseActivity
import com.example.factory_map_project.util.Util.repeatOnStarted
import com.example.factory_map_project.util.event.BaseEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main
) {
    override val viewModel: MainViewModel by viewModels()

    override fun setData() {
//        setStatusBarTransparent() // 상태 바 투명으로 설정(전체화면)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.navBottom.setupWithNavController(navHostFragment.navController)
    }

    override fun setObserver() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                Timber.d("event : $event")
                when(event){
                    is BaseEvent.ShowPopup -> showDialog(event)
                    is BaseEvent.ShowToast -> showToast(event.message)
                    is BaseEvent.ShowLoading -> {
                        Timber.d("loading state : ${event.state}")
                        setLoading(event.state)
                    }
                    is BaseEvent.MovePage -> {}
                }
            }
        }

        viewModel.testLiveData.observe(this){
            Timber.d("관찰 완료 : ${it}")
            geoTest(it)
        }
    }

    override fun setListener() {  }

    private fun geoTest(list: List<FactoryInfo>){
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