package com.example.domain.usecase

import com.example.domain.model.FactoryInfo
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestDaoUseCase(
    private val repo: TestRepository,
)  {
    fun getAll(): Flow<List<FactoryInfo>> {
        return repo.getTestDao()
    }
    fun upsert(data: FactoryInfo) {
        return repo.upsertTestDao(data)
    }
    fun delete(id: Int){
        return repo.deleteTestDao(id)
    }
    fun deleteAll(){
        return repo.deleteAllDao()
    }
}