package com.factory.data.remote.datasource

import com.factory.data.BuildConfig
import com.factory.data.remote.di.RetrofitModule
import com.factory.data.remote.model.GyeonggiResponse
import com.factory.data.remote.util.APIResponseState
import com.factory.data.remote.util.apiErrorHandler
import javax.inject.Inject

class GyeonggiDataSourceImpl @Inject constructor(
    @RetrofitModule.Gyeonggi private val gyeonggiRetrofit: GyeonggiDataSource
) {
    suspend fun getDataSource(count : Int): APIResponseState<GyeonggiResponse> {
        return gyeonggiRetrofit.getDataSource(BuildConfig.API_KEY_GYEONGGI,"json",count, 1000).apiErrorHandler()
    }
}