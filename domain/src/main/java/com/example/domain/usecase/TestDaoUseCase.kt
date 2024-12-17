package com.example.domain.usecase

import com.example.domain.model.TestData
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestDaoUseCase(
    private val repo: TestRepository,
)  {
    fun getAll(): Flow<List<TestData>> {
        return repo.getTestDao()
    }
    fun upsert(data: TestData) {
        return repo.upsertTestDao(data)
    }
    fun delete(id: Int){
        return repo.deleteTestDao(id)
    }
}