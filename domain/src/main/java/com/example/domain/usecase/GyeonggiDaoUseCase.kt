package com.example.domain.usecase

import com.example.domain.repo.TestRepository

class GyeonggiDaoUseCase(
    private val repo: TestRepository,
)  {
    fun delete(id: Int){
        return repo.deleteTestDao(id)
    }
    fun deleteAll(){
        return repo.deleteAllDao()
    }
}