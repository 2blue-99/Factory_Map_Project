package com.example.domain.model

data class FactoryInfo(
    val id: Int,

    val companyName: String, // 회사명

    val roadAddress: String, // 도로명주소

    val representativeName: String, // 대표자

    val companyPhoneNumber: String, // 회사전화번호 (null 허용)

    val totalEmployees: Int, // 고용인원

    val factoryRegistrationDate: String, // 공장 등록일자

    val industryName: String, // 업종명

    val mainProduct: String, // 주생산품
)