package com.example.data.remote.datasource

import com.example.data.remote.model.AllAreaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AllAreaDataSource {
    @GET("getFctryPrdctnService")
    suspend fun getDataSource(
        @Query("serviceKey") key: String,
        @Query("cmpnyNm") name: String,
        @Query("numOfRows") count: Int,
        @Query("adres") address: String,
        @Query("type") type: String,
    ): Response<AllAreaResponse>
}