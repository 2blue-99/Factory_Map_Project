package com.factory.data.util

import com.factory.data.local.entity.FilterEntity

/**
 * 컴바인 구조를 위한 데이터 클래스
 */
data class FactoryCombine(
    val areaTitle: String,
    val longitude: Double,
    val latitude: Double,
    val zoomLevel: Double,
    val filterList: List<FilterEntity>,
    val searchName: String
)