package com.example.domain.type

enum class SelectType(
    val title: String
) {
    COMPANY(title = "상호"),
    BUSINESS_TYPE(title = "업종"),
    PRODUCT(title = "대표 상품"),
    ;

    companion object {
        fun toPosition(type: SelectType?): Int{
            return entries.indexOf(type)
        }

        fun toType(position: Int): SelectType {
            return entries[position]
        }

        fun toTitleList(): List<String>{
            return entries.map { it.title }
        }
    }
}