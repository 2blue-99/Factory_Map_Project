package com.example.factory_map_project.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.GyeonggiInfo
import com.example.domain.repo.DataStoreRepo
import com.example.domain.usecase.GyeonggiDaoUseCase
import com.example.domain.usecase.GetGyenggiUseCase
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val useCase: GetGyenggiUseCase,
    private val daoUseCase: GyeonggiDaoUseCase,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {
    private var _allAreaLiveData = MutableLiveData<List<AllAreaInfo>>()
    val allAreaLiveData: LiveData<List<AllAreaInfo>> = _allAreaLiveData

    private var _gyeonggiLiveData = MutableLiveData<List<GyeonggiInfo>>()
    val gyeonggiLiveData: LiveData<List<GyeonggiInfo>> = _gyeonggiLiveData

    init {
//        onClickGetGyeonggiData()
//        viewModelScope.launch {
//            useCase.getAllAreaData().collect{
//                Timber.d("data : $it")
//            }
//        }
    }

    private fun onClickGetGyeonggiData(){
        modelScope.launch {
            useCase.getGyeonggiData().collect {
                when(it){
                    is ResourceState.Success -> {
                        Timber.d("viewModel result : ${it.body}")
                        _gyeonggiLiveData.postValue(it.body)
                        _eventFlow.emit(AppEvent.ShowLoading(false))
                    }
                    is ResourceState.Loading -> {
                        _eventFlow.emit(AppEvent.ShowLoading(true))
                    }
                    else -> {}
                }
            }
        }
    }
}