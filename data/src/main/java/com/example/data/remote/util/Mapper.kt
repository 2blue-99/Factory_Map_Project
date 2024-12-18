package com.example.data.remote.util

import com.example.data.local.entity.FactoryEntity
import com.example.domain.model.FactoryInfo

object Mapper {
    fun FactoryInfo.toEntity(): FactoryEntity {
        return FactoryEntity(
            id = 0,
            companyName = companyName,
            roadAddress = roadAddress,
            representativeName = representativeName,
            companyPhoneNumber = companyPhoneNumber,
            totalEmployees = totalEmployees,
            factoryRegistrationDate = factoryRegistrationDate,
            industryName = industryName,
            mainProduct = mainProduct
        )
    }
}