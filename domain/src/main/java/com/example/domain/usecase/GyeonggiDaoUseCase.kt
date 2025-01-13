package com.example.domain.usecase

import com.example.domain.repo.FactoryRepository

class GyeonggiDaoUseCase(
    private val repo: FactoryRepository,
)  {
    fun delete(id: Int){
        return repo.deleteTestDao(id)
    }
    fun deleteAll(){
        return repo.deleteAllDao()
    }
}