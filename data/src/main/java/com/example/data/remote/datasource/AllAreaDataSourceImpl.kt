package com.example.data.remote.datasource

import com.example.data.BuildConfig
import com.example.data.remote.di.RetrofitModule
import com.example.data.remote.model.AllAreaResponse
import com.example.data.remote.util.APIResponseState
import com.example.data.remote.util.apiErrorHandler
import timber.log.Timber
import javax.inject.Inject

class AllAreaDataSourceImpl @Inject constructor(
    @RetrofitModule.All private val allRetrofit: AllAreaDataSource,
) {
    suspend fun getDataSource(): APIResponseState<AllAreaResponse> {
        Timber.d("start")
        return allRetrofit.getDataSource(BuildConfig.API_KEY_ALL," ",3000, "인천","JSON").apiErrorHandler()
    }
}