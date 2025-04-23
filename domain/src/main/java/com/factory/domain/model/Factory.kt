package com.factory.domain.model

data class Factory(
    val resultCode: Int, // 결과코드

    val resultMsg: String, // 결과메시지

    val totalCount: Int, // 총 개수

    val items: List<AllAreaInfo>
)