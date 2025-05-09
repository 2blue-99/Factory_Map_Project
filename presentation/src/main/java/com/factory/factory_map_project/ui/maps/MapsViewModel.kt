package com.factory.factory_map_project.ui.maps

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.factory.data.datastore.UserDataStore
import com.factory.data.util.NetworkInterface
import com.factory.domain.model.FactoryInfo
import com.factory.domain.repo.FactoryRepository
import com.factory.domain.type.AreaType
import com.factory.domain.type.ClusterTriggerType
import com.factory.factory_map_project.R
import com.factory.factory_map_project.ui.base.BaseViewModel
import com.factory.factory_map_project.util.ARG_CONTENT
import com.factory.factory_map_project.util.CommonUtil.toCluster
import com.factory.factory_map_project.util.CommonUtil.toDoubleRange
import com.factory.factory_map_project.util.InitialMutableLiveData
import com.factory.factory_map_project.util.event.ActionType
import com.factory.factory_map_project.util.event.AppEvent
import com.factory.factory_map_project.util.map.FactoryCluster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val factoryRepo: FactoryRepository,
    private val userData: UserDataStore,
    private val networkUtil: NetworkInterface,
) : BaseViewModel() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    var selectedAreaType = userData.areaPositionFlow.asLiveData().map { AreaType.toType(it) }

    var isRefresh = InitialMutableLiveData(true)

    private var _factoryData = MutableStateFlow<List<FactoryCluster>>(emptyList())
    val factoryData: StateFlow<List<FactoryCluster>> = _factoryData

    /**
     * 회사이름 입력 EditText
     */
    var inputText = MutableLiveData("")

    var connectedState = networkUtil.networkState.asLiveData().map { InitialMutableLiveData(it) }


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
                    position = AreaType.toPosition(selectedAreaType.value),
                    onSelect = { position ->
                        ioScope.launch {
                            userData.setArea(position)
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
        ioScope.launch {
            userData.setSearchName("")
        }
        modelScope.launch {
            // 검색 Text 초기화
            inputText.value = ""
            isRefresh.value = false
            awaitEvent(AppEvent.GetLocation)?.let { (latLng, zoom) ->
                Timber.d("latLng : $latLng, zoom: $zoom")
                userData.setCurrentLocation(Triple(latLng.latitude, latLng.longitude, zoom.toDoubleRange()))
            }
        }
    }

    fun onClickSearch(){
        // 1. 업체 이름을 통해 개수 체크
        // 2. 1개일 경우, 네임 플로우 변경처리, 해당 위치로 이동
        // 3. 2개 이상일 경우, 네임 플로우 변경처리, 지도 축소 처리
        // 4. 0개일 경우 토스트 메시지
        ioScope.launch {
            if(inputText.value.isNullOrBlank()) {
                Timber.d("null or blank")
                onClickRefresh()
            } else {
                val targetName = inputText.value ?: ""
                val count = factoryRepo.getTargetFactoryDao(targetName)
                Timber.d("count : $count")
                if(count > 0){
                    userData.setSearchName(targetName)
                    emitEvent(AppEvent.Action(ActionType.POSITION_INIT, null))
                }else{
                    emitEvent(AppEvent.ShowToast("존재하지 않는 업체에요."))
                }
            }
        }
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    private fun loadFactoryData() {
        modelScope.launch {
            factoryRepo.getFactoryDao().collect {
                _factoryData.emit(it.map { it.toCluster() })
            }
        }
    }

    fun updateFactory(data: FactoryCluster) {
        ioScope.launch {
            factoryRepo.upsertFactoryDao(data.toDomain())
        }
    }

    fun deleteFactory(id: Int) {
        ioScope.launch {
            factoryRepo.deleteFactoryDao(id)
        }
    }

    suspend fun getClusterTriggerSize(): Int {
        val triggerType = userData.clusterTriggerTypePositionFlow.first()
        return ClusterTriggerType.toType(triggerType).size
    }

    fun changeOptionArea(area: String){
        modelScope.launch {
            val gap = AreaType.toPosition(area)
            userData.setArea(gap)
        }
    }

    suspend fun syncRemoteData() {
        // 서버 데이터
        emitEvent(AppEvent.ShowLoading(true))
        factoryRepo.localSync()?.let { remoteList ->
            Timber.d("remoteList :$remoteList")

            // 최초 1회에 한정하여 서버값 모두 적용
            if(!userData.isInitFlow.first()){
                withContext(Dispatchers.IO){   
                    factoryRepo.upsertFactoryListDao(remoteList)
                    userData.setInit(true)
                }
                return@let
            }

            val localList = ioScope.async {
                factoryRepo.getTargetFactoryDao(remoteList.map { it.id }).toMutableSet()
            }.await()

            val pairList = arrayListOf<Pair<FactoryInfo, FactoryInfo?>>()

            for(remote in remoteList){
                val localOrNull = localList.firstOrNull { remote.id == it.id }
                pairList.add(remote to localOrNull)
            }

            Timber.d("pairList : $pairList")

            val input = Bundle().apply {
                putSerializable(ARG_CONTENT, pairList.toTypedArray())
            }
//            val input = Bundle().apply {
//                val test = arrayOf(FactoryInfo() to FactoryInfo(), FactoryInfo() to null, FactoryInfo() to null, FactoryInfo() to FactoryInfo())
//                putSerializable(ARG_CONTENT, test)
//            }
            emitEvent(AppEvent.MovePage(R.id.moveToCompare, input))
        }.runCatching {
            emitEvent(AppEvent.ShowLoading(false))
        }
        emitEvent(AppEvent.ShowLoading(false))
    }
}