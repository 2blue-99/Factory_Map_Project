package com.example.factory_map_project.ui

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.data.util.NetworkInterface
import com.example.domain.repo.FireStoreRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.InitialMutableLiveData
import com.example.factory_map_project.util.NetworkUtil
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkUtil: NetworkInterface,
    private val repository: FireStoreRepository
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
//                requestAPIData()
                emitEvent(
                    AppEvent.ShowToast("로그아웃 완료")
                )
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun getRemoteData(){
        modelScope.launch {
            if(networkUtil.networkState.first()){
                if(repository.getRemoteFactory()){
                    emitEvent(AppEvent.ShowInputDialog())
                }
            }
        }
    }
}