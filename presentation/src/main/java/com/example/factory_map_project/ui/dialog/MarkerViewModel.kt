package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import kotlinx.coroutines.launch
import timber.log.Timber

class MarkerViewModel: BaseViewModel() {

    private var _uiData = MutableLiveData<FactoryCluster>()
    val uiData: MutableLiveData<FactoryCluster> get() = _uiData

    fun loadData(data: FactoryCluster){
        _uiData.value = data
    }

    fun onClickNegative(){
        viewModelScope.launch {
            Timber.d("ne")
            _eventFlow.emit(AppEvent.Action(ActionType.NEGATIVE))
        }
    }

    fun onClickConfirm(){
        viewModelScope.launch {
            _eventFlow.emit(AppEvent.Action(ActionType.CONFIRM))
        }
    }
}