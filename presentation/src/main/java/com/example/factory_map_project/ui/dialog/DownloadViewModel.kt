package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.domain.usecase.TestUseCase
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val useCase: TestUseCase
) : BaseViewModel() {

    private var _uiData = MutableLiveData<FactoryCluster>()
    val uiData: MutableLiveData<FactoryCluster> get() = _uiData

    fun loadData(data: FactoryCluster) {
        _uiData.value = data
    }

    fun onClickDownload(number: String) {
        modelScope.launch {
            useCase.getAllAreaData().collect {
                when(it){
                    is ResourceState.Loading -> emitEvent(AppEvent.ShowLoading(true))
                    else -> {}
                }
            }

        }
    }
}