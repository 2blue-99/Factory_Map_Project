package com.example.factory_map_project.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.FactoryInfo
import com.example.domain.repo.DataStoreRepo
import com.example.domain.usecase.TestDaoUseCase
import com.example.domain.usecase.TestUseCase
import com.example.domain.util.ResourceState
import com.example.factory_map_project.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val useCase: TestUseCase,
    private val daoUseCase: TestDaoUseCase,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {
    private var _testLiveData = MutableLiveData<List<FactoryInfo>>()
    val testLiveData: LiveData<List<FactoryInfo>> = _testLiveData

    init {
        onClickGetAPI()
    }

    fun onClickGetAPI(){
        modelScope.launch {
            useCase.test().collect {
                when(it){
                    is ResourceState.Success -> {
//                        Timber.d("viewModel result : ${it.body.map { it.mainProduct }}")
                        _testLiveData.postValue(it.body)
                    }
                    else -> {}
                }
            }
        }
    }
}