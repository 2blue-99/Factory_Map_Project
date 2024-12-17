package com.example.data.remote.util

import com.example.domain.util.ExceptionType
import com.example.domain.util.ResourceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber

inline fun <reified T: Any> Response<T>.apiErrorHandler(): APIResponseState<T> {
    return try {
        Timber.d("responseHandler : $this")
        when(this.code()){
            200 -> {
                if(this.body() != null){
                    APIResponseState.Success(code = this.code(), message = this.message(), body = this.body() as T)
                }else{
                    APIResponseState.Exception(type = ExceptionType.NonDataException)
                }
            }
            else -> {
                APIResponseState.Failure(code = this.code(), message = this.message(), body = this.errorBody().toString())
            }
        }
    } catch (e: Exception) {
        APIResponseState.Exception(type = ExceptionType.UnknownHostException)
    }
}


inline fun <reified T: Any, R: Any> APIResponseState<T>.toDomainFlow(mapper: (T) -> R): Flow<ResourceState<R>> {
    Timber.d("toDomainFlow : $this")
    val resource = when(this){
        is APIResponseState.Success -> ResourceState.Success(
            code = this.code,
            message = this.message,
            body = mapper(this.body)
        )
        is APIResponseState.Failure -> ResourceState.Failure(
            code = this.code,
            message = this.message,
        )
        is APIResponseState.Exception ->
            ResourceState.Exception(this.type)
    }
    Timber.d("toDomainFlow : $resource ")
    return flow {
        emit(ResourceState.Loading())
        delay(2000)
        emit(resource)
    }
}