package com.example.factory_map_project.ui.maps

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.domain.model.AreaType
import com.example.domain.repo.FactoryRepository
import com.example.domain.usecase.GyeonggiDaoUseCase
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.Util.toCluster
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repo: FactoryRepository,
    private val userDataStoreRepo: UserDataStore
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    var selectedPosition = userDataStoreRepo.areaPositionFlow.asLiveData().map { AreaType.toType(it) }

    private var _factoryData = MutableStateFlow<List<FactoryCluster>>(emptyList())
    val factoryData: StateFlow<List<FactoryCluster>> = _factoryData


    //**********************************************************************************************
    // Mark: Initialization
    //**********************************************************************************************
    /**
     * 1회만 조회
     */
    init {
        loadFactoryData()
    }


    //**********************************************************************************************
    // Mark: DataBinding
    //**********************************************************************************************
    fun onClickAreaButton() {
        modelScope.launch {
            emitEvent(
                AppEvent.ShowSpinnerDialog(
                    content = AreaType.toTitleList(),
                    position = AreaType.toPosition(selectedPosition.value),
                    onSelect = { position ->
                        Timber.d("postion : $position")
                        ioScope.launch {
                            userDataStoreRepo.setArea(position)
                            withContext(Dispatchers.Main){
                                loadFactoryData()
                            }
                        }
                    }
                )
            )
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun loadFactoryData() {
        modelScope.launch {
            repo.getFactoryDao().collect {
                _factoryData.emit(it.map { it.toCluster() })
            }
        }
    }

    fun updateFactory(data: FactoryCluster) {
        ioScope.launch {
            repo.upsertFactoryDao(data.toDomain())
        }
    }

    fun deleteFactory(id: Int) {
        ioScope.launch {
            repo.deleteFactoryDao(id)
        }
    }
}