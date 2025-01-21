package com.example.factory_map_project.util.event

import com.example.factory_map_project.util.PopupContent

sealed class AppEvent {
    data class ShowPopup(val content: PopupContent): AppEvent(), EventDelegator<Boolean> by DelegatedEvent()
    data class ShowSpinnerDialog(val content: List<String>, val position: Int, val onSelect: (Int) -> Unit): AppEvent(), EventDelegator<Boolean> by DelegatedEvent()
    data class ShowToast(val message: String): AppEvent()
    data class ShowLoading(val state: Boolean): AppEvent()
    data class MovePage(val id: Int): AppEvent()
    data class Action<T>(val type: ActionType, val input: T? = null): AppEvent()
}
