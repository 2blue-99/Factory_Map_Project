package com.example.domain.model

data class FactoryInfo(
    val id: Int,

    val companyName: String, // 기업 그룹 이름

    val scaleDivisionName: String, // 공장 규모 구분 이름

    val lotArea: String, // 부지 면적

    val contact: String, // 전화번호

    val employeeCount: String, // 고용 인원

    val registrationDate: String, // 공장 등록 일자

    val productInfo: String, // 제품 정보

    val loadAddress: String, // 정제 도로명 주소

    val latitude: Double, // 정제 위도

    val longitude: Double, // 정제 경도

    val isClick: Boolean,

    val memo: String,
)

