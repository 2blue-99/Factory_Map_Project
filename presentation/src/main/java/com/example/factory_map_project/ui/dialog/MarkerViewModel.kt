package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.domain.repo.FactoryRepository
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.PopupContent
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarkerViewModel @Inject constructor(
    private val repo: FactoryRepository
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    private var _uiData = MutableLiveData<FactoryCluster>()
    val uiData: MutableLiveData<FactoryCluster> get() = _uiData


    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun loadData(data: FactoryCluster) {
        _uiData.value = data
    }

    fun onClickContact(number: String) {
        modelScope.launch {
            emitEvent(AppEvent.Action(ActionType.CALL, number))
        }
    }

    fun onClickAddress() {
        modelScope.launch {
            emitEvent(AppEvent.Action(ActionType.MAP, uiData.value))
        }
    }

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

    fun onClickDelete(id: Int) {
        ioScope.launch {
            val result = awaitEvent(AppEvent.ShowPopup(content = PopupContent.MAP_MARKER_DELETE))
            if(result==true){
                repo.deleteFactoryDao(id)
                emitEvent(AppEvent.Action(ActionType.DELETE, null))
            }
        }
    }
}