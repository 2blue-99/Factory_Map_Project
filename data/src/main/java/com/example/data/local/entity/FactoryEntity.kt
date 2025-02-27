package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AllAreaInfo
import com.example.domain.model.FactoryInfo
import com.example.domain.model.GyeonggiInfo

@Entity(tableName = "factory")
data class FactoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val companyName: String, // 기업 그룹 이름

    val companyScale: String, // 공장 규모 구분 이름

    val lotArea: String, // 부지 면적

    val contact: String, // 전화번호

    val employeeCount: String, // 고용 인원

    val businessType: String, // 업종

    val product: String, // 대표 상품

    val loadAddress: String, // 정제 도로명 주소

    val latitude: Double, // 정제 위도

    val longitude: Double, // 정제 경도

    val isCheck: Boolean, // 0 : Unknown, 1 : Success, 2: Fail

    val memo: String,

    val category: Int, // 아이콘 카테고리
){
    fun toDomain(): FactoryInfo {
        return FactoryInfo(
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
}
