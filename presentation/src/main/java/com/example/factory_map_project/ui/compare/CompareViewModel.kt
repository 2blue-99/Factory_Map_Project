package com.example.factory_map_project.ui.compare

import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.InitialMutableLiveData
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(

) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    lateinit var compareList: InitialMutableLiveData<Array<Pair<FactoryInfo, FactoryInfo>>>



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
    fun setData(list: Array<Pair<FactoryInfo, FactoryInfo>>){
        compareList = InitialMutableLiveData(list)
    }
}