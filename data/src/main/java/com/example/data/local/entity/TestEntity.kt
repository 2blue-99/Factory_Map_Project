package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.FactoryInfo

@Entity(tableName = "test")
data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstName: String
){
    fun toDomain(): FactoryInfo{
        return FactoryInfo(
            resultCode = "",
            resultMsg = "resultMsg",
            totalCount = 1,
            companyName = "companyName",
            roadAddress = "roadAddress",
            representativeName = "representativeName",
            companyPhoneNumber = "companyPhoneNumber",
            totalEmployees = 1,
            factoryRegistrationDate = "factoryRegistrationDate",
            industryName = "industryName",
            mainProduct = "mainProduct"
        )
    }
}
