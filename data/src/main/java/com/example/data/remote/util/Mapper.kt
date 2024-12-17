package com.example.data.remote.util

import com.example.data.local.entity.TestEntity
import com.example.domain.model.TestData

object Mapper {
    fun TestData.toEntity(): TestEntity {
        return TestEntity(
            id = id,
            firstName = title
        )
    }
}