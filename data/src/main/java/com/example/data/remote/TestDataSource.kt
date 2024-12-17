package com.example.data.remote

import com.example.data.remote.model.FactoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestDataSource {
    @GET("getFctryPrdctnService")
    suspend fun getDataSource(
        @Query("serviceKey") key: String,
        @Query("cmpnyNm") name: String,
        @Query("numOfRows") count: Int,
        @Query("adres") address: String,
        @Query("type") type: String,
    ): Response<FactoryResponse>
}