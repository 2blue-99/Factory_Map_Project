package com.example.data.remote.model

import com.example.domain.model.FactoryInfo
import com.google.gson.annotations.SerializedName

data class FactoryResponse(
    @SerializedName("response")
    val response: Response
)

data class Response(
    @SerializedName("header")
    val header: Header,
    @SerializedName("body")
    val body: Body
)

data class Header(
    @SerializedName("resultCode")
    val resultCode: String, // 결과코드
    @SerializedName("resultMsg")
    val resultMsg: String   // 결과메시지
)

data class Body(
    @SerializedName("items")
    val items: Items,
    @SerializedName("numOfRows")
    val numOfRows: Int, // 한 페이지당 표출 데이터 수
    @SerializedName("pageNo")
    val pageNo: Int, // 페이지 수
    @SerializedName("totalCount")
    val totalCount: Int // 데이터 총 개수
)

data class Items(
    @SerializedName("item")
    val item: List<FactoryInfoResponse>
)

data class FactoryInfoResponse(

    @SerializedName("fctryManageNo")
    val factoryManageNo: String, // 공장관리번호

    @SerializedName("irsttNm")
    val industryComplexName: String, // 산업단지명

    @SerializedName("cmpnyNm")
    val companyName: String, // 회사명

    @SerializedName("rnAdres")
    val roadAddress: String, // 도로명주소

    @SerializedName("rprsntvNm")
    val representativeName: String, // 대표자

    @SerializedName("cvplChrgOrgnztNm")
    val administrativeAgency: String, // 행정기관

    @SerializedName("cmpnyTelno")
    val companyPhoneNumber: String?, // 회사전화번호 (null 허용)

    @SerializedName("cmpnyFxnum")
    val faxNumber: String?, // 팩스번호 (null 허용)

    @SerializedName("allEmplyCo")
    val totalEmployees: Int, // 고용인원

    @SerializedName("frstFctryRegistDe")
    val factoryRegistrationDate: String?, // 공장 등록일자

    @SerializedName("indutyNm")
    val industryName: String?, // 업종명

    @SerializedName("mainProductCn")
    val mainProduct: String?, // 주생산품

    @SerializedName("hmpadr")
    val homepage: String? // 홈페이지 (null 허용)
){
    fun mapper(): FactoryInfo {
        return FactoryInfo(
            id = 0,
            companyName = companyName ?: "확인 불가",
            roadAddress = roadAddress ?: "확인 불가",
            representativeName = representativeName ?: "확인 불가",
            companyPhoneNumber = companyPhoneNumber ?: "확인 불가",
            totalEmployees = totalEmployees ?: -1,
            factoryRegistrationDate = factoryRegistrationDate ?: "확인 불가",
            industryName = industryName ?: "확인 불가",
            mainProduct = mainProduct ?: "확인 불가"
        )
    }
}

