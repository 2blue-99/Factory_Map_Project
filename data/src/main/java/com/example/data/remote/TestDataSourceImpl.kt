package com.example.data.remote

import com.example.data.remote.model.APITestData
import com.example.data.remote.util.APIResponseState
import com.example.data.remote.util.apiErrorHandler
import javax.inject.Inject

class TestDataSourceImpl @Inject constructor(
    private val testAPI: TestDataSource
) {
    suspend fun getDataSource(): APIResponseState<APITestData> {
        return testAPI.getDataSource().apiErrorHandler()
    }
}