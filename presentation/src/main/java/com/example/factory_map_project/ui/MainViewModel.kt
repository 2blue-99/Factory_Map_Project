package com.example.factory_map_project.ui

import android.location.Location
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.datastore.UserDataStore
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataStoreRepo: UserDataStore
): BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
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
                requestAPIData()
                emitEvent(
                    AppEvent.ShowToast("로그아웃 완료")
                )
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    /**
     * 다운로드한지 30일이 지났다면 최신화
     *
     * True : 다운로드 이미 했음
     * False : 다운로드 해야함
     *
     * API 26보다 클 경우, Local Date 비교
     * 작을 경우, false 반환
     */
    suspend fun checkDownload(): Boolean {
        return userDataStoreRepo.downloadFlow.first()
//        val lastDownloadDate = userDataStoreRepo.downloadFlow.first()
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val format = LocalDate.parse(lastDownloadDate)
//            if(format.compareTo(LocalDate.now()) > 30 ){
//                false
//            }else{
//                true
//            }
//        } else {
//            true
//        }
    }


    private suspend fun requestAPIData(): String {
        return "Test"
    }
}