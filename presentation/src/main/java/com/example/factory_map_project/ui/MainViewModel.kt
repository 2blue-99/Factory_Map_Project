package com.example.factory_map_project.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.BaseEvent
import com.example.factory_map_project.util.event.resourceHandler
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.DataStoreRepo
import com.example.domain.usecase.TestDaoUseCase
import com.example.domain.usecase.TestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: TestUseCase,
    private val daoUseCase: TestDaoUseCase,
    private val dataStoreRepo: DataStoreRepo
): BaseViewModel() {

    private var _testLiveData = MutableLiveData<FactoryInfo>()
    val testLiveData: LiveData<FactoryInfo> = _testLiveData

    private var count = 0

    init {
        modelScope.launch {
            useCase.test().resourceHandler(_eventFlow) {
                Timber.d("viewModel : ${it.size}")
                ioScope.launch {
                    it.map { daoUseCase.upsert(it) }
                    Timber.d("룸 끝났어용")
                }
            }
        }
        ioScope.launch {
            daoUseCase.getAll().collect {
                Timber.d("room : $it")
            }
        }
//        modelScope.launch {
//            dataStoreRepo.dataStoreFlow.collect {
//                Timber.d("data store : $it")
//            }
//        }
    }

//    private fun new(count: Int):FactoryInfo =
//        FactoryInfo(
//            id = 0,
//            title = "${count} test title",
//            content = "test content",
//            createdAt = "test createdAt",
//            updatedAt = "test updatedAt",
//            userId = 0
//        )

    fun onClickLogout(){
        modelScope.launch {
            // 팝업창 띄어줌
            val isSuccess = awaitEvent(
                BaseEvent.ShowPopup(content = PopupContent.NETWORK_ERR)
            )
            // 멈춰있음
            if(isSuccess == true){
                requestAPIData()
                emitEvent(
                    BaseEvent.ShowToast("로그아웃 완료")
                )
            }
        }
    }

    fun onClickToast(){
        modelScope.launch {
            emitEvent(BaseEvent.ShowToast("Test"))
        }
    }

    fun onClickBack(){
        modelScope.launch {
            val actionId = requestAPIData()
//            emitEvent(BaseEvent.MovePage(R.id.testestest))
        }
    }

    fun onClickInsert(){
        ioScope.launch {
            isLoading.postValue(true)
            val result = repeat(100000){
//                daoUseCase.upsert(new(count++))
            }
            Timber.d("done")
            isLoading.postValue(false)
        }
    }

    fun onClickDeleteAll(){
        ioScope.launch {
            isLoading.postValue(true)
            daoUseCase.deleteAll()
            Timber.d("done")
            isLoading.postValue(false)
        }
    }

    fun onClickGetAPI(){
        modelScope.launch {
            useCase.test()
        }
    }

    private suspend fun requestAPIData(): String{
        delay(2000)
        return "Test"
    }
}