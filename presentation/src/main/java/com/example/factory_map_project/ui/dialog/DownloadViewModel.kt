package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.AllAreaInfo
import com.example.domain.usecase.TestUseCase
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val useCase: TestUseCase
) : BaseViewModel() {

    private var _uiData = MutableLiveData<List<AllAreaInfo>>()
    val uiData: MutableLiveData<List<AllAreaInfo>> get() = _uiData

    fun onClickDownload() {
        modelScope.launch {
            useCase.getGyeonggiData().collect {
                when(it){
                    is ResourceState.Loading -> emitEvent(AppEvent.ShowLoading(true))
                    is ResourceState.Success -> {

                    }
                    else -> {}
                }
            }

        }
    }
}