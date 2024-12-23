package com.example.factory_map_project.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.map.FactoryCluster

class DownLoadViewModel: BaseViewModel() {

    private var _uiData = MutableLiveData<FactoryCluster>()
    val uiData = MutableLiveData<FactoryCluster>()

    fun loadData(data: FactoryCluster){
        _uiData.value = data
    }
}