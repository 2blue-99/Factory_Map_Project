package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputDialogViewModel @Inject constructor(

) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    private val _text = MutableLiveData("")
    val text: LiveData<String> get() = _text


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

    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun setData(textData: String){
        _text.value = textData
    }
}