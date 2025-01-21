package com.example.factory_map_project.ui

import android.os.Build
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.domain.usecase.GyeonggiDaoUseCase
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.AppEvent
import com.example.domain.model.AreaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataStoreRepo: UserDataStore
): BaseViewModel() {


    init {
//        modelScope.launch {
//            useCase.test().resourceHandler(_eventFlow) {
//                Timber.d("viewModel : ${it.size}")
//                ioScope.launch {
//                    it.map { daoUseCase.upsert(it) }
//                    Timber.d("룸 끝났어용")
//                }
//            }
//        }
//        ioScope.launch {
//            daoUseCase.getAll().collect {
//                Timber.d("room : $it")
//            }
//        }
//        modelScope.launch {
//            dataStoreRepo.dataStoreFlow.collect {
//                Timber.d("data store : $it")
//            }
//        }
    }

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
        val lastDownloadDate = userDataStoreRepo.downloadFlow.first()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val format = LocalDate.parse(lastDownloadDate)
            if(format.compareTo(LocalDate.now()) > 30 ){
                false
            }else{
                true
            }
        } else {
            true
        }
    }


    private suspend fun requestAPIData(): String {
        delay(2000)
        return "Test"
    }
}