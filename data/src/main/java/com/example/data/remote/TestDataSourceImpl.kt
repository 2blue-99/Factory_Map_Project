package com.example.data.remote

import com.example.data.BuildConfig
import com.example.data.remote.model.FactoryInfoResponse
import com.example.data.remote.model.FactoryResponse
import com.example.data.remote.util.APIResponseState
import com.example.data.remote.util.apiErrorHandler
import timber.log.Timber
import javax.inject.Inject

class TestDataSourceImpl @Inject constructor(
    private val testAPI: TestDataSource
) {
    suspend fun getDataSource(): APIResponseState<FactoryResponse> {
        Timber.d("start")
        return testAPI.getDataSource(BuildConfig.API_KEY," ",100, "경기도","JSON").apiErrorHandler()
    }
}