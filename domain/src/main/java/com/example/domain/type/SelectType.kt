package com.example.domain.type

enum class SelectType(
    val title: String
) {
    COMPANY_NAME(title = "상호명"),
    PRODUCT(title = "대표상품"),
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