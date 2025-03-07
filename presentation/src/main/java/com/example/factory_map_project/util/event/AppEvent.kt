package com.example.factory_map_project.util.event

import com.example.domain.model.FactoryInfo
import com.example.factory_map_project.util.PopupContent
import com.google.android.gms.maps.model.LatLng

sealed class AppEvent {
    data class ShowPopup(val content: PopupContent): AppEvent(), EventDelegator<Boolean> by DelegatedEvent()
    data class ShowSpinnerDialog(val content: List<String>, val position: Int, val onSelect: (Int) -> Unit): AppEvent()
    data class ShowInputDialog(val text: String, val onSaveData: (String) -> Unit): AppEvent()
    data class ShowToast(val message: String): AppEvent()
    data class ShowLoading(val state: Boolean): AppEvent()
    data class ShowCompareDialog(val existList: List<FactoryInfo>, val newList: List<FactoryInfo>): AppEvent()
    data class MovePage(val id: Int): AppEvent()

    data class Action<T>(val type: ActionType, val input: T? = null): AppEvent()

    data object GetLocation : AppEvent(), EventDelegator<Pair<LatLng, Float>> by DelegatedEvent()

    data object GoBack : AppEvent()
}
