package com.example.data.remote.datasource

import com.example.data.BuildConfig
import com.example.data.remote.di.RetrofitModule
import com.example.data.remote.model.AllAreaResponse
import com.example.data.remote.model.GyeonggiResponse
import com.example.data.remote.util.APIResponseState
import com.example.data.remote.util.apiErrorHandler
import timber.log.Timber
import javax.inject.Inject

class GyeonggiDataSourceImpl @Inject constructor(
    @RetrofitModule.Gyeonggi private val gyeonggiRetrofit: GyeonggiDataSource
) {
    suspend fun getDataSource(count : Int): APIResponseState<GyeonggiResponse> {
        Timber.d("GyeonggiDataSourceImpl Start")
        return gyeonggiRetrofit.getDataSource(BuildConfig.API_KEY_GYEONGGI,"json",count, 1000).apiErrorHandler()
    }
}