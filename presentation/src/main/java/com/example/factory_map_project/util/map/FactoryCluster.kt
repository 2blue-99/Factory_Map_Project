package com.example.factory_map_project.util.map

import com.example.domain.model.FactoryInfo
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

data class FactoryCluster(
    val id: Int,

    val companyName: String, // 기업 그룹 이름

    val scaleDivisionName: String, // 공장 규모 구분 이름

    val lotArea: String, // 부지 면적

    val contact: String, // 전화번호

    val employeeCount: String, // 고용 인원

    val registrationDate: String, // 공장 등록 일자

    val category: String,

    val productInfo: String, // 제품 정보

    val loadAddress: String, // 정제 도로명 주소

    val latitude: Double, // 정제 위도

    val longitude: Double, // 정제 경도

    val isClick: Int, // 0 : Unknown, 1 : Success 2: Fail

    val memo: String,
) : ClusterItem, Serializable {
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
    override fun getTitle(): String = companyName
    override fun getSnippet(): String = productInfo

    fun toDomain(): FactoryInfo {
        return FactoryInfo(
            id = id,
            companyName = companyName,
            lotArea = lotArea,
            employeeCount = employeeCount,
            scaleDivisionName = scaleDivisionName,
            registrationDate = registrationDate,
            category = category,
            productInfo = productInfo,
            contact = contact,
            loadAddress = loadAddress,
            latitude = latitude,
            longitude = longitude,
            isClick = isClick,
            memo = memo,
        )
    }

    companion object {
        fun toLongClickItem(loadAddress: String, latLng: LatLng): FactoryCluster {
            return FactoryCluster(
                id = 0,
                companyName = "",
                lotArea = "",
                employeeCount = "",
                scaleDivisionName = "",
                registrationDate = "",
                category = "",
                productInfo = "",
                contact = "",
                loadAddress = loadAddress,
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                isClick = 0,
                memo = "",
            )
        }
    }
}

