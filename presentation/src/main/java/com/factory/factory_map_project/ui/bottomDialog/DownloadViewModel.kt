package com.factory.factory_map_project.ui.bottomDialog

import androidx.lifecycle.MutableLiveData
import com.factory.factory_map_project.ui.base.BaseViewModel
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor() : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    var downloadPercentage = MutableLiveData<Int>()


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun onClickDownload() {
//        modelScope.launch {
//            if(downloadPercentage.value == null){
//                gyeonggiRepository.saveGyeonggiData().collect {
//                    when(it){
//                        is ResourceState.Loading -> emitEvent(AppEvent.ShowLoading(true))
//                        is ResourceState.Success -> {
//                            val count = it.body
//                            val percentage = (count.toDouble() / GYEONGGI_DOWNLOAD_COUNT * 100).toInt()
//                            downloadPercentage.value = percentage
//                            if (it.body == GYEONGGI_DOWNLOAD_COUNT) {
//                                emitEvent(AppEvent.ShowLoading(false))
//                            }
//                        }
//                        else -> {
//                            emitEvent(AppEvent.ShowLoading(false))
//                            downloadPercentage.value = 100
//                            cancel()
//                        }
//                    }
//                }
//            }
//        }
    }

    fun onClickComplete(){
        emitEvent(AppEvent.Action(ActionType.CONFIRM, null))
    }
}