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
        val gap = testAPI.getDataSource(BuildConfig.API_KEY," ",10, "서울","JSON").apiErrorHandler()
        if(gap is APIResponseState.Success){
            Timber.d("success : ${gap.body.response.body.items.item}")
            val gap = gap.body
        }
        Timber.d("result : $gap.")
        return gap
    }
}