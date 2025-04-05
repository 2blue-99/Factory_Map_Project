package com.example.factory_map_project.ui

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.factory_map_project.ui.base.BaseViewModel
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
     * Access Code & 자동 로그인 여부로 판단
     * 아직 Access Code 로 특별한 것 없이 단순히 가지고 있음
     */
    var isLogin = userDataStore.userCodeFlow.asLiveData().map { it.isNotBlank() }
//    var isLogin = userDataStore.userCodeFlow.combine(userDataStore.autoLoginFlow){ code, auto ->
//        Timber.d("code : $code / auto : $auto")
//        code.isNotBlank() && auto
//    }.distinctUntilChanged().asLiveData()

    var isAuto = userDataStore.autoLoginFlow

    var currentLocation = MutableSharedFlow<LatLng>()




    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    /**
     * 앱 초기세팅 : 전체 지역
     */
    override fun onCleared() {
        super.onCleared()
        modelScope.launch {
            userDataStore.setArea(0)
        }
    }
}