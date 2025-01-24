package com.example.domain.type

enum class SensitiveType(
    val title: String
) {
    LOW(title = "약함"),
    LOW_LITTLE(title = "다소 약함"),
    MID(title = "보통"),
    LARGE_LITTLE(title = "다소 강함"),
    LARGE(title = "강함"),
    ;

    companion object {
        fun toPosition(type: SensitiveType?): Int{
            return entries.indexOf(type)
        }

        fun toType(position: Int): SensitiveType {
            return entries[position]
        }

        fun toList(): List<String>{
            return entries.map { it.title }
        }
    }
}