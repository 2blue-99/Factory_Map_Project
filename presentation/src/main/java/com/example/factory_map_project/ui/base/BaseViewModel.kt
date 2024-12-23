package com.example.factory_map_project.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.factory_map_project.util.event.AppEvent
import com.example.factory_map_project.util.event.EventDelegator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.coroutineContext

abstract class BaseViewModel: ViewModel() {

    protected val _eventFlow = MutableSharedFlow<AppEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isLoading = MutableLiveData(false)

    private val job = SupervisorJob()

    protected val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e("Exception : $throwable")
        coroutineContext.job.cancel()
    }

    protected val modelScope = viewModelScope + job + exceptionHandler
    protected val ioScope = CoroutineScope(Dispatchers.IO) + job + exceptionHandler



    override fun onCleared() {
        Timber.i("[ onCleared ${this::class.simpleName} ]")
        super.onCleared()
    }



    protected suspend fun emitEvent(event: AppEvent){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    protected suspend fun <T> awaitEvent(event: EventDelegator<T>): T? {
        if(event is AppEvent){
            emitEvent(event)
        }
        return withContext(coroutineContext){
            val gap = event.result()
            return@withContext gap
        }
    }
}