package com.factory.domain.model

import java.io.Serializable

data class FactoryInfo(
    val id: Int = 0,

    val companyName: String = "companyName" , // 기업 그룹 이름

    val companyScale: String = "companyScale", // 공장 규모 구분 이름

    val lotArea: String = "lotArea", // 부지 면적

    val contact: String = "contact", // 전화번호

    val employeeCount: String = "employeeCount", // 고용 인원

    val businessType: String = "businessType", // 업종

    val product: String = "product", // 대표 상품

    val loadAddress: String = "loadAddress", // 정제 도로명 주소

    val latitude: Double = 0.0, // 정제 위도

    val longitude: Double = 0.0, // 정제 경도

    val isCheck: Boolean = false, // 0 : Unknown, 1 : Success, 2: Fail

    val memo: String = "memo",

    val category: Int = 0, // 아이콘 카테고리

    val isDeleted: Boolean = false, // 삭제 여부

    val lastTime: String = "lastTime"
): Serializable