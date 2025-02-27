package com.example.data

import com.example.data.local.entity.FactoryEntity
import com.example.data.local.entity.FilterEntity
import com.example.domain.model.FactoryInfo
import com.example.domain.model.FilterData

object Mapper {
    fun String?.toDateStringFormat(): String {
        return if(this?.length == 8){
            val year = this.substring(0,4)
            val month = this.substring(4,6)
            val day = this.substring(6,8)
            "${year}. ${month}. ${day}"
        }else{
            "확인 불가"
        }
    }

    fun FactoryInfo.toEntity(): FactoryEntity {
        return FactoryEntity(
            id = id,
            companyName = companyName,
            companyScale = companyScale,
            lotArea = lotArea,
            contact = contact,
            employeeCount = employeeCount,
            businessType = businessType,
            product = product,
            loadAddress = loadAddress,
            latitude = latitude,
            longitude = longitude,
            isCheck = isCheck,
            memo = memo,
            category = category
        )
    }

    fun FilterData.toEntity(): FilterEntity {
        return FilterEntity(
            id = id,
            target = target,
            keyword = keyword
        )
    }
}