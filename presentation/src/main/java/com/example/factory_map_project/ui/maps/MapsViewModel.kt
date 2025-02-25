package com.example.factory_map_project.ui.maps

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.example.data.datastore.UserDataStore
import com.example.domain.type.AreaType
import com.example.domain.repo.FactoryRepository
import com.example.domain.type.ClusterTriggerType
import com.example.factory_map_project.R
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.util.CommonUtil.toCluster
import com.example.factory_map_project.util.event.ActionType
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repo: FactoryRepository,
    private val dataStore: UserDataStore
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    var selectedPosition = dataStore.areaPositionFlow.asLiveData().map { AreaType.toType(it) }

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
                        ioScope.launch {
                            dataStore.setArea(position)
                            withContext(Dispatchers.Main){
                                emitEvent(AppEvent.Action(ActionType.POSITION_INIT, null))
                            }
                        }
                    }
                )
            )
        }
    }

    fun onClickSetting(){
        emitEvent(AppEvent.MovePage(id = R.id.moveToSetting))
    }

    fun onClickMyLocation(){
        emitEvent(AppEvent.Action(ActionType.MY_LOCATION, null))
    }

    fun onClickRefresh(){
        modelScope.launch {
            awaitEvent(AppEvent.GetLocation)?.let { (latLng, zoom) ->
                Timber.d("latLng : $latLng, zoom: $zoom")
                val gap = when {
                    zoom >= 15f -> 0.01
                    zoom >= 14f -> 0.02
                    zoom >= 13f -> 0.03
                    zoom >= 12f -> 0.04
                    zoom >= 11f -> 0.1
                    zoom >= 10f -> 0.2
                    zoom >= 9f -> 0.3
                    zoom >= 8f -> 0.4
                    else -> 0.5
                }
                dataStore.setCurrentLocation(Triple(latLng.latitude, latLng.longitude, gap))
            }
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

    suspend fun getClusterTriggerSize(): Int {
        val triggerType = dataStore.clusterTriggerTypePositionFlow.first()
        return ClusterTriggerType.toType(triggerType).size
    }

    fun changeOptionArea(area: String){
        modelScope.launch {
            val gap = AreaType.toPosition(area)
            dataStore.setArea(gap)
        }
    }

}