package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.FactoryInfo

@Entity(tableName = "test")
data class FactoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val companyName: String,
    val roadAddress: String,
    val representativeName: String,
    val companyPhoneNumber: String,
    val totalEmployees: Int,
    val factoryRegistrationDate: String,
    val industryName: String,
    val mainProduct: String,
){
    fun toDomain(): FactoryInfo {
        return FactoryInfo(
            id = id,
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
