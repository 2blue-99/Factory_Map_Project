package com.example.data.remote.model

import com.example.data.local.entity.FactoryEntity
import com.example.data.Mapper.toDateStringFormat
import com.example.domain.model.GyeonggiInfo
import com.google.gson.annotations.SerializedName

data class GyeonggiResponse(
    @SerializedName("FACTRYREGISTTM")
    val factoryRegistTm: List<FactoryRegistTm>
)

data class FactoryRegistTm(
    @SerializedName("head")
    val head: List<Head>,

    @SerializedName("row")
    val rows: List<GyeonggiInfoResponse>
)

data class Head(
    @SerializedName("list_total_count")
    val listTotalCount: Int, // 전체 항목 수

    @SerializedName("RESULT")
    val result: Result, // 처리 결과

    @SerializedName("api_version")
    val apiVersion: String // API 버전
)

data class Result(
    @SerializedName("CODE")
    val code: String, // 결과 코드

    @SerializedName("MESSAGE")
    val message: String // 결과 메시지
)

data class GyeonggiInfoResponse(
    @SerializedName("COMPNY_GRP_NM")
    val companyGroupName: String, // 기업 그룹 이름

    @SerializedName("ADMINIST_INST_NM")
    val administrativeInstitutionName: String, // 행정 기관 이름

    @SerializedName("FOUND_DIV_NM")
    val foundationDivisionName: String, // 설립 구분 이름

    @SerializedName("LOT_AR")
    val lotArea: Double, // 부지 면적

    @SerializedName("SUBFACLT_AR")
    val subFacilityArea: Double, // 부속 시설 면적

    @SerializedName("EMPLY_CNT")
    val employeeCount: Int, // 고용 인원

    @SerializedName("FACTRY_SCALE_DIV_NM")
    val factoryScaleDivisionName: String, // 공장 규모 구분 이름

    @SerializedName("FACTRY_REGIST_DE")
    val factoryRegistrationDate: String, // 공장 등록 일자

    @SerializedName("PURPOS_REGION_NM")
    val purposeRegionName: String, // 목적 지역 이름

    @SerializedName("LANDCGR_NM")
    val landCategoryName: String, // 토지 구분 이름

    @SerializedName("INDUTYPE_DESC_DTCONT")
    val industryTypeDescription: String, // 업종명

    @SerializedName("PRDT_INFO")
    val productInfo: String?, // 생산품 정보

    @SerializedName("TELNO")
    val telephoneNumber: String?, // 전화번호

    @SerializedName("REFINE_ZIPNO")
    val refineZipNumber: String, // 정제 우편번호

    @SerializedName("REFINE_LOTNO_ADDR")
    val refineLotNumberAddress: String, // 정제 지번 주소

    @SerializedName("REFINE_ROADNM_ADDR")
    val refineRoadNameAddress: String, // 정제 도로명 주소

    @SerializedName("REFINE_WGS84_LOGT")
    val refineLongitude: Double, // 정제 경도

    @SerializedName("REFINE_WGS84_LAT")
    val refineLatitude: Double, // 정제 위도

    @SerializedName("INDUTYPE_CD_INFO")
    val industryTypeCodeInfo: String // 산업 유형 코드 정보
){
    fun toDomain(): GyeonggiInfo {
        return GyeonggiInfo(
            id = 0,
            companyName = companyGroupName ?: "확인 불가",
            lotArea = "${lotArea} m^2" ?: "확인 불가",
            employeeCount = "${employeeCount}명" ?: "확인 불가",
            scaleDivisionName = factoryScaleDivisionName ?: "확인 불가",
            registrationDate = factoryRegistrationDate.toDateStringFormat(),
            productInfo = productInfo ?: "확인 불가",
            contact = telephoneNumber ?: "확인 불가",
            loadAddress = refineRoadNameAddress ?: "확인 불가",
            longitude = refineLongitude ?: -1.0,
            latitude = refineLatitude ?: -1.0
        )
    }

    fun toEntity(): FactoryEntity {
        return FactoryEntity(
            id = 0,
            companyName = companyGroupName ?: "확인 불가",
            lotArea = "${lotArea} m^2" ?: "확인 불가",
            employeeCount = "${employeeCount}명" ?: "확인 불가",
            scaleDivisionName = factoryScaleDivisionName ?: "확인 불가",
            registrationDate = factoryRegistrationDate.toDateStringFormat(),
            category = "",
            productInfo = productInfo ?: "확인 불가",
            contact = telephoneNumber ?: "확인 불가",
            loadAddress = refineRoadNameAddress ?: "확인 불가",
            longitude = refineLongitude ?: -1.0,
            latitude = refineLatitude ?: -1.0,
            isClick = 0,
            memo = ""
        )
    }
}

