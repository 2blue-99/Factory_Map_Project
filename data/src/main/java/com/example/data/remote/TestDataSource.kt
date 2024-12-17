package com.example.data.remote

import com.example.data.remote.model.APITestData
import retrofit2.Response
import retrofit2.http.GET

interface TestDataSource {
    @GET("posts/1")
    suspend fun getDataSource(): Response<APITestData>
}