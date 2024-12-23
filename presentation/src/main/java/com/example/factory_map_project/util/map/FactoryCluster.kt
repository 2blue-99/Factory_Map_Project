package com.example.factory_map_project.util.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class FactoryCluster(
    val id: Int,

    val companyName: String, // 기업 그룹 이름

    val lotArea: Double, // 부지 면적

    val employeeCount: Int, // 고용 인원

    val scaleDivisionName: String, // 공장 규모 구분 이름

    val registrationDate: String, // 공장 등록 일자

    val description: String, // 산업 유형 설명

    val productInfo: String, // 제품 정보

    val contact: String, // 전화번호

    val numberAddress: String, // 정제 지번 주소

    val loadNameAddress: String, // 정제 도로명 주소

    val latitude: Double, // 정제 위도

    val longitude: Double, // 정제 경도
) : ClusterItem {
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    override fun getTitle(): String = companyName
    override fun getSnippet(): String = description
}