package com.example.factory_map_project.ui

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.data.repo.FactoryRepositoryImpl
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataStore: UserDataStore
): BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    /**
     * 로그인 여부
     *
     * 아직 Access Code 로 특별한 것은 하지 않음
     * 단순히 가지고 있는 유무만 판단
     */
    var isLogin = userDataStore.userCodeFlow.asLiveData().map { it.isNotBlank() }

    var currentLocation = MutableSharedFlow<LatLng>()


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
}