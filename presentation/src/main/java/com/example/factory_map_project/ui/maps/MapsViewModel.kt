package com.example.factory_map_project.ui.maps

import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FactoryRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.Util.toCluster
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repo: FactoryRepository
) : BaseViewModel() {

    private var _localFactory = MutableStateFlow<List<FactoryCluster>>(emptyList())
    val localFactory: StateFlow<List<FactoryCluster>> = _localFactory

    /**
     * 1회만 조회
     */
    init {
        ioScope.launch {
            repo.upsertFactoryDao(
                FactoryInfo(
                    0,
                    "test",
                    "test",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    37.5073218717,
                    127.6164271659,
                    false,
                    "Test"
                )
            )
        }
        modelScope.launch {
            repo.getFactoryDao().collect {
                Timber.d("viewModel : $it")
                _localFactory.emit(it.map { it.toCluster() })
            }
        }
    }

    fun updateFactory(data: FactoryCluster){
        ioScope.launch {
            repo.upsertFactoryDao(data.toDomain())
        }
    }

    fun deleteFactory(id: Int){
        repo.deleteFactoryDao(id)
    }


//    private fun onClickGetGyeonggiData(){
//        modelScope.launch {
//            useCase.getGyeonggiData().collect {
//                when(it){
//                    is ResourceState.Success -> {
//                        Timber.d("viewModel result : ${it.body}")
//                        _gyeonggiLiveData.postValue(it.body)
//                        _eventFlow.emit(AppEvent.ShowLoading(false))
//                    }
//                    is ResourceState.Loading -> {
//                        _eventFlow.emit(AppEvent.ShowLoading(true))
//                    }
//                    else -> {}
//                }
//            }
//        }
//    }
}