package com.factory.data.remote.util

import com.factory.domain.util.ExceptionType
import com.factory.domain.util.ResourceState
import retrofit2.Response
import timber.log.Timber

inline fun <reified T: Any> Response<T>.apiErrorHandler(): APIResponseState<T> {
    return try {
//        Timber.d("responseHandler : ${this}")
        when(this.code()){
            200 -> {
                if(this.body() != null){
                    val gap = APIResponseState.Success(code = this.code(), message = this.message(), body = this.body() as T)
                    Timber.d("responseHandler : $gap")
                    gap
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


inline fun <reified T: Any, R: Any> APIResponseState<T>.toDomain(mapper: (T) -> R): ResourceState<R> {
    Timber.d("toDomainFlow : $this")
    return when(this){
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
//    return flow {
//        emit(ResourceState.Loading())
//        delay(2000)
//        emit(resource)
//    }
}