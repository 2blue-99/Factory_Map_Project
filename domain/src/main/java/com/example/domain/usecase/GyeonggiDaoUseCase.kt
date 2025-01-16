package com.example.domain.usecase

import com.example.domain.repo.FactoryRepository

class GyeonggiDaoUseCase(
    private val repo: FactoryRepository,
)  {
    fun delete(id: Int){
        return repo.deleteFactoryDao(id)
    }
    fun deleteAll(){
        return repo.deleteAllFactoryDao()
    }
}