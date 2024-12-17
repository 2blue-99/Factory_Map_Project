package com.example.factory_map_project.util.event

import com.example.factory_map_project.util.PopupContent
import com.example.domain.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

suspend inline fun <reified T> Flow<ResourceState<T>>.resourceHandler(
    eventFlow: MutableSharedFlow<BaseEvent>,
    crossinline onSuccess: (T) -> Unit,
){
    this.collect { resource ->
        when(resource){
            is ResourceState.Success -> {
                eventFlow.emit(BaseEvent.ShowLoading(false))
                onSuccess(resource.body)
            }
            is ResourceState.Failure -> {
                eventFlow.emit(BaseEvent.ShowLoading(false))
                eventFlow.emit(BaseEvent.ShowToast(resource.message))
            }
            is ResourceState.Exception -> {
                eventFlow.emit(BaseEvent.ShowLoading(false))
                eventFlow.emit(BaseEvent.ShowPopup(PopupContent.NETWORK_ERR))
            }
            is ResourceState.Loading -> {
                eventFlow.emit(BaseEvent.ShowLoading(true))
            }
        }
    }
}