package com.example.factory_map_project.ui.setting

import androidx.lifecycle.MutableLiveData
import com.example.data.datastore.UserDataStore
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataStore: UserDataStore
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
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

    fun onClickClusterSensitivity(){
        emitEvent(AppEvent.Action(ActionType.SENSITIVITY, null))
    }

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
}