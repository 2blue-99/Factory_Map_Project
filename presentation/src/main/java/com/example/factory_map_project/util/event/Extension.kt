package com.example.factory_map_project.util.event

import com.example.factory_map_project.util.PopupContent
import com.example.domain.util.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

suspend inline fun <reified T> Flow<ResourceState<T>>.resourceHandler(
    eventFlow: MutableSharedFlow<AppEvent>,
    crossinline onSuccess: (T) -> Unit,
){
    this.collect { resource ->
        when(resource){
            is ResourceState.Success -> {
                eventFlow.emit(AppEvent.ShowLoading(false))
                onSuccess(resource.body)
            }
            is ResourceState.Failure -> {
                eventFlow.emit(AppEvent.ShowLoading(false))
                eventFlow.emit(AppEvent.ShowToast(resource.message))
            }
            is ResourceState.Exception -> {
                eventFlow.emit(AppEvent.ShowLoading(false))
                eventFlow.emit(AppEvent.ShowPopup(PopupContent.NETWORK_ERR))
            }
            is ResourceState.Loading -> {
                eventFlow.emit(AppEvent.ShowLoading(true))
            }
        }
    }
}