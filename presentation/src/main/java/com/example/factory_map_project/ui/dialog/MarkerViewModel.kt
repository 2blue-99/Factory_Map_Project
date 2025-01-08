package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import kotlinx.coroutines.launch
import timber.log.Timber

class MarkerViewModel : BaseViewModel() {

    private var _uiData = MutableLiveData<FactoryCluster>()
    val uiData: MutableLiveData<FactoryCluster> get() = _uiData

    fun loadData(data: FactoryCluster) {
        _uiData.value = data
    }

    fun onClickContact(number: String) {
        modelScope.launch {
            _eventFlow.emit(AppEvent.Action(ActionType.CALL, number))
        }
    }

    fun onClickAddress() {
        modelScope.launch {
            _eventFlow.emit(AppEvent.Action(ActionType.MAP, uiData.value))
        }
    }

    fun onClickNegative() {
        modelScope.launch {
            _eventFlow.emit(AppEvent.Action(ActionType.NEGATIVE, null))
        }
    }

    fun onClickConfirm() {
        modelScope.launch {
            _eventFlow.emit(AppEvent.Action(ActionType.CONFIRM, null))
        }
    }

    fun onClickCheckBox(state: Boolean) {
        Timber.d("state : $state")
    }
}