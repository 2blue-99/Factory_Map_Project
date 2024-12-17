package com.example.factory_map_project.util.event

import com.example.factory_map_project.util.PopupContent

sealed class BaseEvent {
    data class ShowPopup(val content: PopupContent): BaseEvent(), EventDelegator<Boolean> by DelegatedEvent()
    data class ShowToast(val message: String): BaseEvent()
    data class ShowLoading(val state: Boolean): BaseEvent()
    data class MovePage(val id: Int): BaseEvent()
}
