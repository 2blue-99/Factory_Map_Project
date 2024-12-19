package com.example.factory_map_project.ui.home

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.R
import com.example.factory_map_project.databinding.FragmentMapsBinding
import com.example.factory_map_project.ui.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
class MapsFragment : BaseFragment<FragmentMapsBinding, MapsViewModel>(
    FragmentMapsBinding::inflate
) {
    override val viewModel: MapsViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        Timber.d("OnMapReadyCallback")
        this.googleMap = googleMap
        val test = LatLng(37.6065018, 126.762868)
        googleMap.addMarker(MarkerOptions().position(test).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(test, 5f))
    }

    override fun setData() {}

    override fun setUI() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun setObserver() {
        viewModel.testLiveData.observe(this) {
            Timber.d("관찰 완료 : ${it}")
            geoTest(it)
        }
    }

    override fun setListener() {}

    private fun geoTest(list: List<FactoryInfo>) {
        val geo = Geocoder(requireContext())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (data in list) {
                val address = data.roadAddress
                if (address.isNotBlank()) {
                    geo.getFromLocationName(address, 1) {
                        val result = it[0]
                        val test = LatLng(result.latitude, result.longitude)
                        Timber.d("결과 : ${it[0]}")
                        lifecycleScope.launch {
                            googleMap.addMarker(
                                MarkerOptions().position(test).title("Marker in Sydney")
                            )
//                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(test))
                        }
                    }
                }
            }
        }
    }
}