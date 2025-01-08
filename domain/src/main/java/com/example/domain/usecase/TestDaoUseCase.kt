package com.example.domain.usecase

import com.example.domain.model.AllAreaInfo
import com.example.domain.repo.TestRepository
import kotlinx.coroutines.flow.Flow

class TestDaoUseCase(
    private val repo: TestRepository,
)  {
    fun delete(id: Int){
        return repo.deleteTestDao(id)
    }
    fun deleteAll(){
        return repo.deleteAllDao()
    }
}