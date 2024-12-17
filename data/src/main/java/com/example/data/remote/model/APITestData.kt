package com.example.data.remote.model

import com.example.domain.model.TestData
import com.google.gson.annotations.SerializedName

data class APITestData(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("UserId")
    val userId: Int
){
    fun mapper(): TestData {
        return TestData(
            id, title, content, createdAt, updatedAt, userId
        )
    }
}

