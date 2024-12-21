package com.example.domain.model

data class GyeonggiInfo(
    val id: Int,

    val companyGroupName: String, // 기업 그룹 이름

    val lotArea: Double, // 부지 면적

    val employeeCount: Int, // 고용 인원

    val factoryScaleDivisionName: String, // 공장 규모 구분 이름

    val factoryRegistrationDate: String, // 공장 등록 일자

    val industryTypeDescription: String, // 산업 유형 설명

    val productInfo: String?, // 제품 정보

    val telephoneNumber: String?, // 전화번호

    val refineLotNumberAddress: String, // 정제 지번 주소

    val refineRoadNameAddress: String, // 정제 도로명 주소

    val refineLatitude: Double, // 정제 위도

    val refineLongitude: Double, // 정제 경도
)