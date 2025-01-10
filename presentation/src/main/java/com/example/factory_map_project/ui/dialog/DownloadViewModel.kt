package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.AllAreaInfo
import com.example.domain.repo.TestRepository
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val gyeonggiRepository: TestRepository,
) : BaseViewModel() {

    private var _uiData = MutableLiveData<List<AllAreaInfo>>()
    val uiData: MutableLiveData<List<AllAreaInfo>> get() = _uiData

    init {
        modelScope.launch {
            gyeonggiRepository.getGyeonggiDaoData().collect {
                Timber.d("room : ${it.size}")
            }
        }
    }

    fun onClickDownload() {
        modelScope.launch {
            gyeonggiRepository.saveGyeonggiData().collect {
                when(it){
                    is ResourceState.Loading -> emitEvent(AppEvent.ShowLoading(true))
                    is ResourceState.Success -> {
//                        Timber.d("data : ${it.body}")
                        if(it.body == 5){
                            emitEvent(AppEvent.ShowLoading(false))
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}