package com.example.factory_map_project.util.map

import com.example.domain.model.FactoryInfo
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

data class FactoryCluster(
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

    val isCheck: Boolean,

    val memo: String,

    val category: Int, // 아이콘 카테고리

    val isDelete: Boolean, // 삭제 여부

) : ClusterItem, Serializable {
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    override fun getTitle(): String = companyName
    override fun getSnippet(): String = product

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
            category = category,
            isDelete = isDelete
        )
    }

    companion object {
        fun toLongClickItem(loadAddress: String, latLng: LatLng): FactoryCluster {
            return FactoryCluster(
                id = 0,
                companyName = "",
                companyScale = "",
                lotArea = "",
                employeeCount = "",
                businessType = "",
                product = "",
                contact = "",
                loadAddress = loadAddress,
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                isCheck = false,
                memo = "",
                category = 0,
                isDelete = false
            )
        }
    }
}

