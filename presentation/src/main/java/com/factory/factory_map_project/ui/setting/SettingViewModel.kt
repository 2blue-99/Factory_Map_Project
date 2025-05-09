package com.factory.factory_map_project.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.factory.data.datastore.UserDataStore
import com.factory.domain.type.ClusterTriggerType
import com.factory.factory_map_project.ui.base.BaseViewModel
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    val clusterTriggerType = userDataStore.clusterTriggerTypePositionFlow.map {
        ClusterTriggerType.toType(it).title
    }.asLiveData()
    val exclusionWord = MutableLiveData("")

    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickBack(){
        emitEvent(AppEvent.GoBack)
    }

    fun onClickClusterTriggerType(){
        emitEvent(AppEvent.ShowSpinnerDialog(
            content = ClusterTriggerType.toList(),
            position = ClusterTriggerType.toPosition(clusterTriggerType.value),
            onSelect = { updateClusterTriggerType(it) }
        ))
    }

    fun onClickExclusionWord(){
        emitEvent(AppEvent.Action(ActionType.EXCLUSION, null))
    }

    fun onClickLogout(){
        modelScope.launch {
            userDataStore.setUserCode("")
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun updateClusterTriggerType(typePosition: Int){
        ioScope.launch {
            userDataStore.setClusterTriggerTypePosition(typePosition)
        }
    }
}