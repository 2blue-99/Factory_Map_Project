package com.example.data.remote.util

import com.example.data.local.entity.TestEntity
import com.example.domain.model.FactoryInfo

object Mapper {
    fun FactoryInfo.toEntity(): TestEntity {
        return TestEntity(
            id = 1,
            firstName = "title"
        )
    }
}