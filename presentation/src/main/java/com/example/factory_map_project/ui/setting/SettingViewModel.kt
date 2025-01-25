package com.example.factory_map_project.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.data.datastore.UserDataStore
import com.example.domain.type.SensitiveType
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    val exclusionWord = MutableLiveData("")
    val sensitiveTypePosition = userDataStore.clusterSensitiveFlow.asLiveData()

    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickBack(){
        emitEvent(AppEvent.GoBack)
    }

    fun onClickExclusionWord(){
        emitEvent(AppEvent.Action(ActionType.EXCLUSION, null))
    }

    fun onClickClusterSensitivity(){
        emitEvent(AppEvent.ShowSpinnerDialog(
            content = SensitiveType.toList(),
            position = sensitiveTypePosition.value?:0,
            onSelect = { updateSensitive(it) }
        ))
//        emitEvent(AppEvent.Action(ActionType.SENSITIVITY, null))
    }

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun updateSensitive(typePosition: Int){
        ioScope.launch {
            userDataStore.setClusterSensitive(typePosition)
        }
    }
}