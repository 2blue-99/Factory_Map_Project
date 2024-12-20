package com.example.factory_map_project.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.DataStoreRepo
import com.example.domain.usecase.TestDaoUseCase
import com.example.domain.usecase.TestUseCase
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val useCase: TestUseCase,
    private val daoUseCase: TestDaoUseCase,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {
    private var _allAreaLiveData = MutableLiveData<List<AllAreaInfo>>()
    val allAreaLiveData: LiveData<List<AllAreaInfo>> = _allAreaLiveData

    private var _gyeonggiLiveData = MutableLiveData<List<GyeonggiInfo>>()
    val gyeonggiLiveData: LiveData<List<GyeonggiInfo>> = _gyeonggiLiveData

    init {
        onClickGetGyeonggiData()
    }

    private fun onClickGetAreaData(){
        modelScope.launch {
            useCase.getAllAreaData().collect {
                when(it){
                    is ResourceState.Success -> {
//                        Timber.d("viewModel result : ${it.body.map { it.mainProduct }}")
                        _allAreaLiveData.postValue(it.body)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onClickGetGyeonggiData(){
        modelScope.launch {
            useCase.getGyeonggiData().collect {
                when(it){
                    is ResourceState.Success -> {
//                        Timber.d("viewModel result : ${it.body.map { it.mainProduct }}")
                        _gyeonggiLiveData.postValue(it.body)
                    }
                    else -> {}
                }
            }
        }
    }
}