package com.example.factory_map_project.ui.compare

import com.example.domain.model.FactoryInfo
import com.example.domain.repo.FactoryRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.InitialMutableLiveData
import com.example.factory_map_project.util.SELECT_NONE
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val factoryRepository: FactoryRepository
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    /**
     * 비교 데이터
     */
    var compareList = InitialMutableLiveData<List<Pair<FactoryInfo, FactoryInfo?>>>(emptyList())
    /**
     * 체크 여부
     */
    var selectList = InitialMutableLiveData<Array<Pair<Int, FactoryInfo>>>(emptyArray())

    /**
     * 확인 버튼 클릭 가능 여부
     */
    var isSelectAll = InitialMutableLiveData(false)


    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickNegative() {
        modelScope.launch {
            emitEvent(AppEvent.Action(ActionType.NEGATIVE, null))
        }
    }

    fun onClickConfirm() {
        modelScope.launch {
            if(!isSelectAll.value){
                emitEvent(AppEvent.ShowToast("데이터를 모두 선택해주세요."))
            }else{
                factoryRepository.upsertFactoryListDao(selectList.value.map { it.second })
                emitEvent(AppEvent.Action(ActionType.CONFIRM, null))
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun setData(list: List<Pair<FactoryInfo, FactoryInfo?>>){
        Timber.d("list : $list")
        compareList.value = list
        selectList.value = Array(list.size){ Pair(SELECT_NONE, FactoryInfo()) }
    }
}