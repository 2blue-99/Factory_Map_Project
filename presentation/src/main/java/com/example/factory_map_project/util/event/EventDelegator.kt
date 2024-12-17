package com.example.factory_map_project.util.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

interface EventDelegator<T> {
    suspend fun result(): T?

    fun tryEmit(value: T): Boolean

    fun cancel(): Boolean
}

class DelegatedEvent<T: Any>: EventDelegator<T> {
    private val flow = MutableSharedFlow<T?>(extraBufferCapacity = 1)

    override suspend fun result(): T? = flow.first()

    override fun tryEmit(value: T): Boolean = flow.tryEmit(value)

    override fun cancel(): Boolean = flow.tryEmit(null)
}