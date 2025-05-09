package com.factory.factory_map_project.ui.bottomDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.factory.domain.repo.FactoryRepository
import com.factory.factory_map_project.ui.base.BaseViewModel
import com.factory.factory_map_project.util.PopupContent
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import com.factory.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

//    var currentCheckState = MutableLiveData(0)

    /**
     *  데이터 변경 여부를 체크하기 위한 초기값
     */
    var originCluster: FactoryCluster? = null


    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
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

    fun onLongClickCompanyName(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.companyName ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(companyName = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickScaleDivisionName(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.companyScale ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(companyScale = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickLotArea(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.lotArea ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(lotArea = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickContact(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.contact ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(contact = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickEmployeeCount(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.employeeCount ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(employeeCount = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickBusinessType(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.businessType ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(businessType = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickProduct(): Boolean {
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.product ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(product = it))
                    }
                ))
            }
        }
        return true
    }

    fun onLongClickLoadAddress(): Boolean{
        viewModelScope.launch {
            uiData.value?.let { clusterData ->
                emitEvent(AppEvent.ShowInputDialog(
                    text = uiData.value?.loadAddress ?: "",
                    onSaveData = {
                        updateCluster(clusterData.copy(loadAddress = it))
                    }
                ))
            }
        }
        return true
    }

//    fun onClickCheckBox(){
//        var next = currentCheckState.value!! + 1
//        currentCheckState.value = if(next > 2) 0 else next
//    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun loadData(data: FactoryCluster) {
        originCluster = data
        _uiData.value = data
//        currentCheckState.value = data.isCheck
    }

    private fun updateCluster(cluster: FactoryCluster){
        _uiData.value = cluster
    }
}