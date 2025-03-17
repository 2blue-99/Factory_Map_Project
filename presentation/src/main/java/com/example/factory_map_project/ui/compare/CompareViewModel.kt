package com.example.factory_map_project.ui.compare

import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.InitialMutableLiveData
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(

) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    /**
     * 비교 데이터
     */
    var compareList = InitialMutableLiveData<List<Pair<FactoryInfo, FactoryInfo>>>(emptyList())
    /**
     * 체크 여부
     */
    var selectList = InitialMutableLiveData<Array<FactoryInfo?>>(emptyArray())



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
            emitEvent(AppEvent.Action(ActionType.CONFIRM, null))
        }
    }

    fun onClickBack(){

    }

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun setData(list: List<Pair<FactoryInfo, FactoryInfo>>){
        Timber.d("list : $list")
        compareList.value = list
        selectList.value = Array(list.size){ null }
    }
}