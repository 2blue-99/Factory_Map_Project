package com.example.data.remote.datasource

import com.example.data.remote.model.AllAreaResponse
import com.example.data.remote.model.GyeonggiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GyeonggiDataSource {
    @GET("FACTRYREGISTTM")
    suspend fun getDataSource(
        @Query("KEY") key: String,
        @Query("Type") name: String,
        @Query("pIndex") address: Int,
        @Query("pSize") count: Int
    ): Response<GyeonggiResponse>
}