package com.example.factory_map_project.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.data.datastore.UserDataStore
import com.example.domain.type.ClusterTriggerType
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
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

    fun onClickExclusionWord(){
        emitEvent(AppEvent.Action(ActionType.EXCLUSION, null))
    }

    fun onClickClusterTriggerType(){
        emitEvent(AppEvent.ShowSpinnerDialog(
            content = ClusterTriggerType.toList(),
            position = ClusterTriggerType.toPosition(clusterTriggerType.value),
            onSelect = { updateClusterTriggerType(it) }
        ))
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