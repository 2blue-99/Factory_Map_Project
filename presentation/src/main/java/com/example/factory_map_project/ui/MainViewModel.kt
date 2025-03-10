package com.example.factory_map_project.ui

import com.example.data.util.NetworkInterface
import com.example.domain.repo.FactoryRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkUtil: NetworkInterface,
    private val factoryRepository: FactoryRepository
): BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    var currentLocation = MutableSharedFlow<LatLng>()

    var connectedState = networkUtil.networkState


    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickLogout(){
        modelScope.launch {
            // 팝업창 띄어줌
            val isSuccess = awaitEvent(
                AppEvent.ShowPopup(content = PopupContent.NETWORK_ERR)
            )
            // 멈춰있음
            if(isSuccess == true){
                emitEvent(
                    AppEvent.ShowToast("로그아웃 완료")
                )
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun syncRemoteData(){
        modelScope.launch {
            val gap = awaitEvent(AppEvent.ShowCompareDialog(emptyList(), emptyList()))?.let { resultList -> }
            gap

        }

//        modelScope.launch {
//            // 서버 데이터
//            val syncList = factoryRepository.localSync()
//            if (syncList.isNotEmpty()) {
//                // 서버 데이터에 대응되는 로컬 데이터
//                val localList = factoryRepository.getTargetFactoryDao(syncList.map { it.id })
//                awaitEvent(AppEvent.ShowCompareDialog(localList, syncList))?.let { resultList ->
//                    factoryRepository.upsertFactoryListDao(resultList)
//                }
//
//            }
//        }
    }
}