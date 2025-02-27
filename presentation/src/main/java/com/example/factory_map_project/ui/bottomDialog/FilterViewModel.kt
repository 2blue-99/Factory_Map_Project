package com.example.factory_map_project.ui.bottomDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.domain.model.FilterData
import com.example.domain.repo.FilterRepository
import com.example.domain.type.SelectType
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.CommonUtil.isContain
import com.example.factory_map_project.util.event.AppEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val filterRepository: FilterRepository
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    val filterList = filterRepository.getAllFilterDao().asLiveData()
    val inputFilterText = MutableLiveData<String>()
    val targetWord = MutableLiveData(SelectType.toTitleList()[0])

    var targetPickerList = SelectType.toTitleList()


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun onClickAddFilter() {
        ioScope.launch {
            if(inputFilterText.value.isNullOrBlank()){
                emitEvent(AppEvent.ShowToast("키워드를 입력해주세요."))
            }else if(filterList.value?.isContain(targetWord.value?:"", inputFilterText.value?:"") == true){
                emitEvent(AppEvent.ShowToast("존재하는 키워드에요."))
            }else{
                filterRepository.upsertFilterDao(
                    FilterData(
                        id = 0,
                        target = targetWord.value ?: "",
                        keyword = inputFilterText.value ?: ""
                    )
                )
                emitEvent(AppEvent.ShowToast("저장 되었어요."))
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun deleteTarget(id: Int){
        ioScope.launch {
            filterRepository.deleteFilterDao(id)
        }
    }
}